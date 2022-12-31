package com.khay.gestiondestock.validator;

import com.khay.gestiondestock.dto.AdresseDto;
import com.khay.gestiondestock.dto.CategoryDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AdresseValidator {

    public static List<String> validate(AdresseDto adresseDto) {
        List<String> errors = new ArrayList<>();

        if (adresseDto == null) {
            errors.add("Veuillez renseigner l'adresse 1");
            errors.add("Veuillez renseigner la ville");
            errors.add("Veuillez renseigner le code postal");
            errors.add("Veuillez renseigner le pays");
            return errors;
        }

        if (!StringUtils.hasLength(adresseDto.getAdresse1())) {
            errors.add("Veuillez renseigner l'adresse 1");
        }

        if (!StringUtils.hasLength(adresseDto.getVille())) {
            errors.add("Veuillez renseigner la ville");
        }

        if (!StringUtils.hasLength(adresseDto.getCodePostale())) {
            errors.add("Veuillez renseigner le code postal");
        }

        if (!StringUtils.hasLength(adresseDto.getPays())) {
            errors.add("Veuillez renseigner le pays");
        }
        return errors;
    }
}
