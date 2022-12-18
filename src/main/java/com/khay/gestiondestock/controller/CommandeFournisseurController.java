package com.khay.gestiondestock.controller;

import com.khay.gestiondestock.controller.api.CommandeFournisseurApi;
import com.khay.gestiondestock.dto.CommandeFournisseurDto;
import com.khay.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.khay.gestiondestock.model.EtatCommande;
import com.khay.gestiondestock.services.CommandeFournisseurSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class CommandeFournisseurController implements CommandeFournisseurApi {

    private CommandeFournisseurSerivce commandeFournisseurSerivce;

    @Autowired
    public CommandeFournisseurController(CommandeFournisseurSerivce commandeFournisseurService) {
        this.commandeFournisseurSerivce = commandeFournisseurService;
    }

    @Override
    public CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
        return commandeFournisseurSerivce.updateEtatCommande(idCommande, etatCommande);
    }

    @Override
    public CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        return commandeFournisseurSerivce.updateQuantiteCommande(idCommande, idLigneCommande, quantite);
    }

    @Override
    public CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur) {
        return commandeFournisseurSerivce.updateFournisseur(idCommande, idFournisseur);
    }

    @Override
    public CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
        return commandeFournisseurSerivce.updateArticle(idCommande, idLigneCommande, idArticle);
    }

    @Override
    public CommandeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
        return commandeFournisseurSerivce.deleteArticle(idCommande, idLigneCommande);
    }

    @Override
    public CommandeFournisseurDto save(CommandeFournisseurDto commandeFournisseurDto) {
        return commandeFournisseurSerivce.save(commandeFournisseurDto);
    }

    @Override
    public CommandeFournisseurDto findById(Integer id) {
        return commandeFournisseurSerivce.findById(id);
    }

    @Override
    public List<CommandeFournisseurDto> findAll() {
        return commandeFournisseurSerivce.findAll();
    }

    @Override
    public CommandeFournisseurDto findByCode(String code) {
        return commandeFournisseurSerivce.findByCode(code);
    }

    @Override
    public void delete(Integer id) {
        commandeFournisseurSerivce.delete(id);
    }

    @Override
    public ResponseEntity<List<LigneCommandeFournisseurDto>> findAlllignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande) {
        return ResponseEntity.ok(commandeFournisseurSerivce.findAllLignesCommandesFournisseurByCommandeFournisseurId(idCommande));
    }
}
