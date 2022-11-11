package com.khay.gestiondestock.dto;

import com.khay.gestiondestock.model.MvtStk;
import com.khay.gestiondestock.model.SourceMvtStock;
import com.khay.gestiondestock.model.TypeMvtStock;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
@Data
@Builder
public class MvtStkDto {
    private Integer id;
    private Instant dateMvt;
    private BigDecimal quantite;
    private ArticleDto article;
    private TypeMvtStock typeMvt;
    private Integer idEntreprise;
    private SourceMvtStock sourceMvtStock;
    public static MvtStkDto fromEntity(MvtStk mvtStk) {
        if (mvtStk == null) {
            return null;
        }
        return MvtStkDto.builder()
                .id(mvtStk.getId())
                .dateMvt(mvtStk.getDateMvt())
                .quantite(mvtStk.getQuantite())
                .article(ArticleDto.fromEntity(mvtStk.getArticle()))
                .typeMvt(mvtStk.getTypeMvt())
                .sourceMvtStock(mvtStk.getSourceMvtStock())
                .idEntreprise(mvtStk.getIdEntreprise())
                .build();
    }

    public static MvtStk toEntity(MvtStkDto mvtStkDto) {
        if (mvtStkDto == null) {
            return null;
        }

        MvtStk mvtStk = new MvtStk();
        mvtStk.setId(mvtStkDto.getId());
        mvtStk.setDateMvt(mvtStkDto.getDateMvt());
        mvtStk.setArticle(ArticleDto.toEntity(mvtStkDto.getArticle()));
        mvtStk.setQuantite(mvtStkDto.getQuantite());
        mvtStk.setTypeMvt(mvtStkDto.getTypeMvt());
        mvtStk.setSourceMvtStock(mvtStkDto.getSourceMvtStock());
        mvtStk.setIdEntreprise(mvtStkDto.getIdEntreprise());

        return mvtStk;
    }
}
