package com.khay.gestiondestock.validator;

import com.khay.gestiondestock.dto.MvtStkDto;

import java.util.ArrayList;
import java.util.List;

public class MvtStockValidator {

    public static List<String> validate(MvtStkDto mvtStkDto) {
        List<String> errors = new ArrayList<>();
        if (mvtStkDto.getId() == null) {
            errors.add("Veuillez entrer l'ID du stock");
        }
        return errors;
    }
}
