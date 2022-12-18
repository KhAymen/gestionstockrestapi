package com.khay.gestiondestock.services;


import com.khay.gestiondestock.dto.CommandeFournisseurDto;
import com.khay.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.khay.gestiondestock.model.EtatCommande;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeFournisseurSerivce {

    CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande);

    CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);

    CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur);

    CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle);

    CommandeFournisseurDto save(CommandeFournisseurDto commandeFournisseurDto);

    // Delete article ===> delete LigneCommandeFournisseur
    CommandeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande);

    CommandeFournisseurDto findById(Integer id);

    List<CommandeFournisseurDto> findAll();

    CommandeFournisseurDto findByCode(String code);

    List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande);

    void delete(Integer id);
}
