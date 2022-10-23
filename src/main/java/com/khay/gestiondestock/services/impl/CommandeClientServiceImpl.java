package com.khay.gestiondestock.services.impl;

import com.khay.gestiondestock.dto.*;
import com.khay.gestiondestock.exception.EntityNotFoundException;
import com.khay.gestiondestock.exception.ErrorCodes;
import com.khay.gestiondestock.exception.InvalidEntityException;
import com.khay.gestiondestock.exception.InvalidOperationException;
import com.khay.gestiondestock.model.*;
import com.khay.gestiondestock.repository.ArticleRepository;
import com.khay.gestiondestock.repository.ClientRepository;
import com.khay.gestiondestock.repository.CommandeClientRepository;
import com.khay.gestiondestock.repository.LigneCommandeClientRepository;
import com.khay.gestiondestock.services.CommandeClientService;
import com.khay.gestiondestock.services.MvtStkService;
import com.khay.gestiondestock.validator.ArticleValidator;
import com.khay.gestiondestock.validator.CommandeClientValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CommandeClientServiceImpl implements CommandeClientService {

    private CommandeClientRepository commandeClientRepository;
    private ArticleRepository articleRepository;
    private ClientRepository clientRepository;
    private LigneCommandeClientRepository ligneCommandeClientRepository;
    private MvtStkService mvtStkService;

    @Autowired
    public CommandeClientServiceImpl(
            CommandeClientRepository commandeClientRepository,
            LigneCommandeClientRepository ligneCommandeClientRepository,
            ArticleRepository articleRepository,
            ClientRepository clientRepository, MvtStkService mvtStkService
    ) {
        this.commandeClientRepository = commandeClientRepository;
        this.articleRepository = articleRepository;
        this.clientRepository = clientRepository;
        this.ligneCommandeClientRepository = ligneCommandeClientRepository;
        this.mvtStkService = mvtStkService;
    }


    @Override
    public CommandeClientDto save(CommandeClientDto commandeClientDto) {

        List<String> errors = CommandeClientValidator.validate(commandeClientDto);
        if (!errors.isEmpty()) {
            log.error("La commande client n'est pas valide");
            throw new InvalidEntityException("La commande client n'est pas valide ",
                    ErrorCodes.COMMANDE_CLIENT_NOT_VALID, errors);
        }

        if (commandeClientDto.getId() != null && commandeClientDto.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'il est livrée",
                    ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE);
        }

        Optional<Client> client = clientRepository.findById(commandeClientDto.getClient().getId());
        if (!client.isEmpty()) {
            log.warn("The client with the ID {} was not found in the database ", commandeClientDto.getClient().getId());
            throw new EntityNotFoundException("Aucun client avec l'ID " + commandeClientDto.getClient().getId()
                    + " n'a été trouvé dans la base de données", ErrorCodes.CLIENT_NOT_FOUND);
        }

        List<String> articleErrors = new ArrayList<>();
        if (commandeClientDto.getLigneCommandeClients() != null) {
            commandeClientDto.getLigneCommandeClients().forEach(ligCmdClt -> {
                if (ligCmdClt.getArticle() != null) {
                    Optional<Article> article = articleRepository.findById(ligCmdClt.getArticle().getId());
                    if (article.isEmpty()) {
                        articleErrors.add("L'article avec L'ID {} " + ligCmdClt.getArticle().getId() + " n'existe pas");
                    }
                } else {
                    articleErrors.add("Impossible d'enregistrer une commande avec un article NULL");
                }
            });
        }

        if (!articleErrors.isEmpty()) {
            log.warn("");
            throw new InvalidEntityException("Article n'existe pas dans la base de données",
                    ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
        }

        CommandeClient savedCmdClt = commandeClientRepository.save(CommandeClientDto.toEntity(commandeClientDto));
        if (commandeClientDto.getLigneCommandeClients() != null) {
            commandeClientDto.getLigneCommandeClients().forEach(ligCmdCLt -> {
                LigneCommandeClient ligneCommandeClient = LigneCommandeClientDto.toEntity(ligCmdCLt);
                ligneCommandeClient.setCommandeClient(savedCmdClt);
                ligneCommandeClientRepository.save(ligneCommandeClient);
            });

        }
        return CommandeClientDto.fromEntity(savedCmdClt);
    }

    @Override
    public CommandeClientDto findById(Integer id) {
        if (id == null) {
            log.error("Commande client ID is NULL");
            return null;
        }
        return commandeClientRepository.findById(id)
                .map(CommandeClientDto::fromEntity)
//                .orElseThrow(() -> new InvalidEntityException(
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande client n'a été trouvé avec l'ID " + id, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));
    }

    @Override
    public CommandeClientDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Commande client CODE is NULL");
            return null;
        }
        return commandeClientRepository.findCommandeClientByCode(code)
                .map(CommandeClientDto::fromEntity)
//                .orElseThrow(() -> new InvalidEntityException(
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande client n'a été trouvé avec le CODE " + code, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));
    }

    @Override
    public List<CommandeClientDto> findAll() {
        return commandeClientRepository.findAll().stream()
                .map(CommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CommandeClientDto updateEtatCommande(Integer id, EtatCommande etatCommande) {
        if (id == null) {
            log.error("LA commande Client avec Id null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un Id null",
                    ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE);
        }

        if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec id null",
                    ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE);
        }
        CommandeClientDto commandeClient = findById(id);

        if (commandeClient.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'il est livrée",
                    ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE);
        }

        commandeClient.setEtatCommande(etatCommande);
        CommandeClient savedCmd = commandeClientRepository.save(CommandeClientDto.toEntity(commandeClient));
        // We have to check that the commande is livree before updating the command
        if (commandeClient.isCommandeLivree()) {
            updateMvtStock(id);
        }

        return CommandeClientDto.fromEntity(savedCmd);
    }

    @Override
    public CommandeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {

        if (idCommande == null) {
            log.error("LA commande Client avec Id null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un Id null",
                    ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE);
        }

        if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
            log.error("la quantite est null");

            throw new InvalidOperationException("Impossible de modifier la commande avec une quantite null",
                    ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE);
        }

        if (idLigneCommande == null) {
            log.error("L'id de lA ligne commande est null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec une ligne de commande null",
                    ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE);
        }

        CommandeClientDto commandeClient = findById(idCommande);

        if (commandeClient.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec une ligne de commande null",
                    ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE);
        }


        Optional<LigneCommandeClient> ligneCommande = findLigneCommande(idLigneCommande);

        LigneCommandeClient ligneCommandeClient = ligneCommande.get();
        ligneCommandeClient.setQuantite(quantite);
        ligneCommandeClientRepository.save(ligneCommandeClient);

        return commandeClient;
    }


    @Override
    public CommandeClientDto updateClient(Integer idCommande, Integer idClient) {

        if (idCommande == null) {
            log.error("L'ID Commande est null");
            throw new InvalidOperationException("Impossible de modifier la commande avec ID null",
                    ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE);

        }

        if (idClient == null) {
            log.error("L'ID commande Client est null");
            throw new InvalidOperationException("Impossible de modifier la commande avec ID Client null",
                    ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE);

        }

        CommandeClientDto commandeClient = checkEtatCommande(idCommande);

        Optional<Client> clientOptional = clientRepository.findById(idClient);

        if (clientOptional.isEmpty()) {
            throw new EntityNotFoundException("Impossible de de trouver le client de cet ID " + idClient,
                    ErrorCodes.CLIENT_NOT_FOUND);
        }

        commandeClient.setClient(ClientDto.fromEntity(clientOptional.get()));

        return CommandeClientDto.fromEntity(commandeClientRepository.save(CommandeClientDto.toEntity(commandeClient)));
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Commande client ID is NULL");
            return;
        }
        commandeClientRepository.deleteById(id);

    }

    @Override
    public CommandeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande) {

        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        CommandeClientDto commandeClient = checkEtatCommande(idCommande);
        Optional<LigneCommandeClient> ligneCommandeClient = findLigneCommande(idLigneCommande);
        ligneCommandeClientRepository.deleteById(idLigneCommande);
        return commandeClient;
    }

    @Override
    public List<LigneCommandeClientDto> findAlllignesCommandesClientByCommandeClientId(Integer idCommande) {
        return ligneCommandeClientRepository.findAllByCommandeClientId(idCommande)
                .stream()
                .map(LigneCommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    // TODO findAllCommandeClientByCommandeClientId


    // TODO : Refactoring of verification process


    @Override
    public CommandeClientDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {

        checkIdCommande(idCommande);

        checkIdLigneCommande(idLigneCommande);

        checkIdArticle(idArticle, "ancien");

        CommandeClientDto commandeCLient = checkEtatCommande(idCommande);

        Optional<LigneCommandeClient> ligneCommandeClient = findLigneCommande(idLigneCommande);

        Optional<Article> articleOptional = articleRepository.findById(idArticle);

        if (articleOptional.isEmpty()) {
            throw new EntityNotFoundException("Impossible de de trouver l'article avec ID " + idArticle,
                    ErrorCodes.ARTICLE_NOT_FOUND);
        }

        List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(articleOptional.get()));
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("Article Invalid", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        LigneCommandeClient ligneCommandeClientToSaved = ligneCommandeClient.get();
        ligneCommandeClientToSaved.setArticle(articleOptional.get());
        ligneCommandeClientRepository.save(ligneCommandeClientToSaved);

        return commandeCLient;
    }

    private CommandeClientDto checkEtatCommande(Integer idCommande) {

        CommandeClientDto commandeClient = findById(idCommande);

        if (commandeClient.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec une ligne de commande null",
                    ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE);
        }
        return commandeClient;
    }

    private void checkIdCommande(Integer idCommande) {
        if (idCommande == null) {
            log.error("L'ID Commande est null");
            throw new InvalidOperationException("Impossible de modifier la commande avec ID null",
                    ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE);

        }
    }

    private void checkIdLigneCommande(Integer idLigneCommande) {
        if (idLigneCommande == null) {
            log.error("L'id de lA ligne commande est null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec une ligne de commande null",
                    ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE);
        }

    }

    private void checkIdArticle(Integer idArticle, String msg) {
        if (idArticle == null) {
            log.error("L'id de l'article est null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un " + msg + " id article null",
                    ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE);
        }
    }

    private Optional<LigneCommandeClient> findLigneCommande(Integer idLigneCommande) {

        Optional<LigneCommandeClient> ligneCommande = ligneCommandeClientRepository.findById(idLigneCommande);

        if (ligneCommande.isEmpty()) {
            throw new EntityNotFoundException("Impossible de de trouver la ligne de commande client de cet ID " + idLigneCommande,
                    ErrorCodes.LIGNE_COMMANDE_CLIENT_NOT_FOUND);
        }

        return ligneCommande;
    }

    private void updateMvtStock(Integer idCommande) {

        List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(idCommande);
        ligneCommandeClients.forEach(ligne -> {
            MvtStkDto sortieDtk = MvtStkDto.builder()
                    .article(ArticleDto.fromEntity(ligne.getArticle()))
                    .dateMvt(Instant.now())
                    .typeMvt(TypeMvtStock.SORTIE)
                    .sourceMvtStock(SourceMvtStock.COMMANDE_CLIENT)
                    .quantite(ligne.getQuantite())
                    //    .idEntreprise (ligne.getIdEntreprise())
                    .build();
            mvtStkService.sortieStk(sortieDtk);
        });
    }
}
