package com.khay.gestiondestock.controller.api;

import com.khay.gestiondestock.dto.CommandeFournisseurDto;
import com.khay.gestiondestock.model.EtatCommande;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.khay.gestiondestock.utils.Constants.*;

@Api(COMMANDE_FOURNISSUER_ENDPOINT)
public interface CommandeFournisseurApi {

    @PatchMapping(COMMANDE_FOURNISSUER_ENDPOINT + "/update/etat/{idCommande}/{etatCommande}")
    CommandeFournisseurDto updateEtatCommande(@PathVariable("idCommande") Integer idCommande, @PathVariable("etatCommande") EtatCommande etatCommande);

    @PatchMapping(COMMANDE_FOURNISSUER_ENDPOINT + "/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
    CommandeFournisseurDto updateQuantiteCommande(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("quantite") BigDecimal quantite);

    @PatchMapping(COMMANDE_FOURNISSUER_ENDPOINT + "/update/fournisseur/{idCommande}/{idFournisseur}")
    CommandeFournisseurDto updateFournisseur(@PathVariable("idCommande") Integer idCommande, @PathVariable("idFournisseur") Integer idFournisseur);

    @PatchMapping(COMMANDE_FOURNISSUER_ENDPOINT + "/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
    CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle);

    @DeleteMapping(COMMANDE_FOURNISSUER_ENDPOINT + "/delete/article/{idCommande}/{idLigneCommande}")
    CommandeFournisseurDto deleteArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande);

    @PostMapping(CREATE_COMMANDE_FOURNISSUER_ENDPOINT)
    CommandeFournisseurDto save(@RequestBody CommandeFournisseurDto commandeFournisseurDto);

    @GetMapping(FIND_COMMANDE_FOURNISSUER_BY_ID_ENDPOINT)
    CommandeFournisseurDto findById(@PathVariable("idCommandeFournisseur") Integer id);

    @GetMapping(FIND_ALL_COMMANDE_FOURNISSUER)
    List<CommandeFournisseurDto> findAll();

    @GetMapping(FIND_COMMANDE_FOURNISSUER_BY_CODE)
    CommandeFournisseurDto findByCode(@PathVariable("codeCommandeFournisseur") String code);

    @DeleteMapping(DELETE_COMMANDE_FOURNISSUER_ENDPOINT)
    void delete(@PathVariable("idCommandeFournisseur") Integer id);
}
