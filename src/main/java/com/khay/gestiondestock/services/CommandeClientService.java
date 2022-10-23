package com.khay.gestiondestock.services;

import com.khay.gestiondestock.dto.CommandeClientDto;
import com.khay.gestiondestock.dto.LigneCommandeClientDto;
import com.khay.gestiondestock.model.EtatCommande;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeClientService {

    CommandeClientDto save(CommandeClientDto commandeClientDto);

    CommandeClientDto findById(Integer id);

    CommandeClientDto updateEtatCommande(Integer id, EtatCommande etatCommande);

    CommandeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);

    CommandeClientDto updateClient(Integer idCommande, Integer idClient);

    // delete an article ==> delete ligneCommndeClient
    CommandeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande);

    CommandeClientDto updateArticle(Integer idCommande, Integer idLigneCommande,  Integer newIdArticle);

    CommandeClientDto findByCode(String code);

    List<LigneCommandeClientDto> findAlllignesCommandesClientByCommandeClientId(Integer idCommande);

    List<CommandeClientDto> findAll();

    void delete(Integer id);
}
