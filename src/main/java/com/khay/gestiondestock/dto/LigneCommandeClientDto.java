package com.khay.gestiondestock.dto;

import com.khay.gestiondestock.model.LigneCommandeClient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LigneCommandeClientDto {

    private Integer id;

    private ArticleDto article;

    @JsonIgnore
    private CommandeClientDto commandeClient;

    private BigDecimal quantite;

    private BigDecimal prixUnitaire;

    private Integer idEntreprise;

    public static LigneCommandeClientDto fromEntity(LigneCommandeClient ligneCmdClt) {
        if (ligneCmdClt == null) {
            return null;
        }

        return LigneCommandeClientDto.builder()
                .id(ligneCmdClt.getId())
                .article(ArticleDto.fromEntity(ligneCmdClt.getArticle()))
                .quantite(ligneCmdClt.getQuantite())
                .prixUnitaire(ligneCmdClt.getPrixUnitaire())
                .idEntreprise(ligneCmdClt.getIdEntreprise())
                //                .commandeClient(CommandeClientDto.fromEntity(ligneCmdClt.getCommandeClient()))
                .build();
    }

    public static LigneCommandeClient toEntity(LigneCommandeClientDto ligneCommandeClientDto) {

        if (ligneCommandeClientDto == null) {
            return null;
        }

        LigneCommandeClient ligneCommandeClient = new LigneCommandeClient();
        ligneCommandeClient.setId(ligneCommandeClientDto.getId());
        ligneCommandeClient.setArticle(ArticleDto.toEntity(ligneCommandeClientDto.getArticle()));
        ligneCommandeClient.setQuantite(ligneCommandeClientDto.getQuantite());
        ligneCommandeClient.setPrixUnitaire(ligneCommandeClientDto.getPrixUnitaire());
        ligneCommandeClient.setIdEntreprise(ligneCommandeClientDto.getIdEntreprise());

        return ligneCommandeClient;
    }
}
