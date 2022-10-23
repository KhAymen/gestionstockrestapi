package com.khay.gestiondestock.services.impl;

import com.khay.gestiondestock.dto.ArticleDto;
import com.khay.gestiondestock.dto.CommandeFournisseurDto;
import com.khay.gestiondestock.dto.FournisseurDto;
import com.khay.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.khay.gestiondestock.exception.EntityNotFoundException;
import com.khay.gestiondestock.exception.ErrorCodes;
import com.khay.gestiondestock.exception.InvalidEntityException;
import com.khay.gestiondestock.exception.InvalidOperationException;
import com.khay.gestiondestock.model.*;
import com.khay.gestiondestock.repository.ArticleRepository;
import com.khay.gestiondestock.repository.CommandeFournisseurRepository;
import com.khay.gestiondestock.repository.FournisseurRepository;
import com.khay.gestiondestock.repository.LigneCommandeFournisseurRepository;
import com.khay.gestiondestock.services.CommandeFournisseurSerivce;
import com.khay.gestiondestock.validator.ArticleValidator;
import com.khay.gestiondestock.validator.CommandeFournisseurValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommandeFournisseurServiceImpl implements CommandeFournisseurSerivce {

    private ArticleRepository articleRepository;
    private FournisseurRepository fournisseurRepository;
    private CommandeFournisseurRepository commandeFournisseurRepository;
    private LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;

    @Autowired
    public CommandeFournisseurServiceImpl(ArticleRepository articleRepository, FournisseurRepository fournisseurRepository, CommandeFournisseurRepository commandeFournisseurRepository, LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository) {
        this.articleRepository = articleRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.commandeFournisseurRepository = commandeFournisseurRepository;
        this.ligneCommandeFournisseurRepository = ligneCommandeFournisseurRepository;
    }

    private void checkIdCommande(Integer idCommande) {
        if (idCommande == null) {
            log.error("L'id de la commande est null");
            throw new InvalidOperationException("Impossible de modifier la commande avec ID null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE);

        }
    }

    private void checkIdLigneCommande(Integer idLigneCommande) {
        if (idLigneCommande == null) {
            log.error("L'id ligne commande est null");
            throw new InvalidOperationException("Impossible de modifier la commande par ce que l'id est null", ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE);
        }
    }

    private CommandeFournisseurDto checkEtatCommande(Integer idCommande) {

        CommandeFournisseurDto commandeFournisseur = findById(idCommande);

        if (commandeFournisseur.isCommandeLivree()) {
            throw new InvalidOperationException("On ne peut pas modifier une commande fournisseur déja livrée",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE);
        }
        return commandeFournisseur;
    }

    @Override
    public CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
        checkIdCommande(idCommande);
        if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
            log.error("L'état de la commande fournisseur est nulle ");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un état null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE);
        }
        CommandeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);
        commandeFournisseurDto.setEtatCommande(etatCommande);
        return CommandeFournisseurDto.fromEntity(commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseurDto)));
    }

    @Override
    public CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        if (quantite == null || (quantite.compareTo(BigDecimal.ZERO) == 0)) {
            log.error("la quantite est null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec une quantité null ou ZERO", ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE);
        }
        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = findLigneCommandeFournisseur(idLigneCommande);
        LigneCommandeFournisseur ligneCommandeFournisseur = ligneCommandeFournisseurOptional.get();
        ligneCommandeFournisseur.setQuantite(quantite);
        ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);
        return commandeFournisseur;
    }

    private Optional<LigneCommandeFournisseur> findLigneCommandeFournisseur(Integer id) {
        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = ligneCommandeFournisseurRepository.findById(id);
        if (ligneCommandeFournisseurOptional.isEmpty())
            throw new EntityNotFoundException("Aucune ligne commande fournissseur n'a été trouvé avec cet id " + id,
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND);
        return ligneCommandeFournisseurOptional;
    }

    @Override
    public CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur) {
        checkIdCommande(idCommande);
        if (idFournisseur == null) {
            log.error("L'id fournisseur est null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un id fournisseur null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE);
        }

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        Optional<Fournisseur> fournisseurOptional = fournisseurRepository.findById(idFournisseur);
        if (fournisseurOptional.isEmpty())
            throw new EntityNotFoundException("Aucun fournisseur n'a été trouvé avec cet id", ErrorCodes.FOURNISSEUR_NOT_FOUND);
        commandeFournisseur.setFournisseur(FournisseurDto.fromEntity(fournisseurOptional.get()));
        return CommandeFournisseurDto.fromEntity(commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseur)));
    }

    @Override
    public CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        if (idArticle == null) {
            log.error("L'id artcile est null");
            throw new InvalidEntityException("Impossible de modifier un article avec id null", ErrorCodes.ARTICLE_NON_MODIFABLLE);
        }
        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        Optional<LigneCommandeFournisseur> optionalLigneCommandeFournisseur = findLigneCommandeFournisseur(idLigneCommande);

        Optional<Article> article = articleRepository.findById(idArticle);
        if (article.isEmpty()) {
            throw new EntityNotFoundException("Aucun article n'a été trouvé avec cet id " + idArticle, ErrorCodes.ARTICLE_NOT_FOUND);
        }
        List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(article.get()));
        if (!errors.isEmpty())
            throw new InvalidEntityException("Article non valid ", ErrorCodes.ARTICLE_NOT_VALID, errors);

        LigneCommandeFournisseur ligneCommandeFournisseurToSave = optionalLigneCommandeFournisseur.get();
        ligneCommandeFournisseurToSave.setArticle(article.get());
        ligneCommandeFournisseurRepository.save(ligneCommandeFournisseurToSave);
        return commandeFournisseur;
    }

    @Override
    public CommandeFournisseurDto save(CommandeFournisseurDto dto) {
        List<String> errors = CommandeFournisseurValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Commande fournisseur n'est pas valide");
            throw new InvalidEntityException("La commande fournisseur n'est pas valide ", ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID, errors);
        }
        Optional<Fournisseur> fournisseur = fournisseurRepository.findById(dto.getFournisseur().getId());
        if (fournisseur.isEmpty()) {
            log.warn("Supllier with the ID {} was not found in the database ", dto.getFournisseur().getId());
            throw new EntityNotFoundException("Le fournisseur avec l'id {} n'éxiste pas " + dto.getFournisseur().getId() + " dans la base de données",
                    ErrorCodes.FOURNISSEUR_NOT_FOUND);
        }

        List<String> articleErrors = new ArrayList<>();

        if(dto.getLigneCommandeFournisseurs() != null) {
            dto.getLigneCommandeFournisseurs().forEach(ligCmdFrs -> {
                if (ligCmdFrs.getArticle() != null) {
                    Optional<Article> article = articleRepository.findById(ligCmdFrs.getArticle().getId());
                    if (article.isEmpty()) {
                        articleErrors.add("L'article avec l'ID " + ligCmdFrs.getArticle().getId() + " n'existe pas");
                    }
                } else {
                    articleErrors.add("Impossible d'enregistrer une commande avec un article NULL");
                }
                    }

            );
        }

        if (!articleErrors.isEmpty()) {
            log.warn("");
            throw new InvalidEntityException("Article n'existe pas dans la BDD", ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
        }

        CommandeFournisseur savedCmdFrs = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(dto));

        if (dto.getLigneCommandeFournisseurs() != null) {
            dto.getLigneCommandeFournisseurs().forEach(ligCmdFrs -> {
                LigneCommandeFournisseur ligneCommandeFournisseur = LigneCommandeFournisseurDto.toEntity(ligCmdFrs);
                ligneCommandeFournisseur.setCommandeFournisseur(savedCmdFrs);
                ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);
            });
        }

        return CommandeFournisseurDto.fromEntity(savedCmdFrs);
    }

    @Override
    public CommandeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);

        findLigneCommandeFournisseur(idLigneCommande);
        ligneCommandeFournisseurRepository.deleteById(idLigneCommande);
        return commandeFournisseur;
    }

    @Override
    public CommandeFournisseurDto findById(Integer id) {
        if (id == null) {
            log.error("Commande fournisseur ID is NULL");
            return null;
        }
        return commandeFournisseurRepository.findById(id)
                .map(CommandeFournisseurDto::fromEntity)
                .orElseThrow(() ->
//                        new InvalidEntityException(
                        new EntityNotFoundException(
                                "Aucune commande fournisseur n'a été trouvé avec l'ID " + id,
                        ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND));
    }


    @Override
    public CommandeFournisseurDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Commande fournisseur CODE is null");
            return null;
        }
        return commandeFournisseurRepository.findCommandeFournisseurByCode(code)
                .map(CommandeFournisseurDto::fromEntity)
                .orElseThrow(() ->
//                        new InvalidEntityException(
                        new EntityNotFoundException(
                                "Aucune commande fournisseur avec le CODE " + code,
                        ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND)
                );
    }

    @Override
    public List<CommandeFournisseurDto> findAll() {
        return commandeFournisseurRepository.findAll()
                .stream()
                .map(CommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande) {
        return null;
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Commande fournisseur ID is NULL");
            return;
        }
        commandeFournisseurRepository.deleteById(id);

    }
}
