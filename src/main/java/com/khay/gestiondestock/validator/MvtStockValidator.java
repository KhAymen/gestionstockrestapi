package com.khay.gestiondestock.validator;

import com.khay.gestiondestock.dto.MvtStkDto;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MvtStockValidator {

    public static List<String> validate(MvtStkDto mvtStkDto) {
        List<String> errors = new ArrayList<>();

        if (mvtStkDto == null) {
            errors.add("Veuillez renseigner la date du mouvement de stock");
            errors.add("Veuillez renseigner la quantite du mouvement de stock");
            errors.add("Veuillez renseigner l'article du mouvement de stock");
            errors.add("Veuillez renseigner le type du mouvement de stock");

            return errors;
        }

        if (mvtStkDto.getDateMvt() == null) {
            errors.add("Veuillez renseigner la date du mouvement de stock");
        }

        if (mvtStkDto.getQuantite() == null || mvtStkDto.getQuantite().compareTo(BigDecimal.ZERO) == 0) {
            errors.add("Veuillez renseigner la quantite du mouvement de stock");
        }

        if (mvtStkDto.getArticle() == null || mvtStkDto.getArticle().getId() == null) {
            errors.add("Veuillez renseigner l'article du mouvement de stock");
        }

        if (!StringUtils.hasLength(mvtStkDto.getTypeMvt().name())) {
            errors.add("Veuillez renseigner le type du mouvement de stock");
        }

        return errors;
    }
}
