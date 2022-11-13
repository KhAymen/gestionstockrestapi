package com.khay.gestiondestock.validator;

import com.khay.gestiondestock.dto.EntrepriseDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EntrepriseValidator {

    public static List<String> validate(EntrepriseDto entrepriseDto) {
        List<String> errors = new ArrayList<>();
        if (entrepriseDto == null) {
            errors.add("Veuillez renseigner le nom d'entreprise");
            errors.add("Veuillez renseigner le prenom d'entreprise");
            errors.add("Veuillez renseigner le mot de passe d'entreprise");
            errors.add("Veuillez renseigner l'adresse d'entreprise");
            return errors;
        }
        if (!StringUtils.hasLength(entrepriseDto.getNom())) {
            errors.add("Veuillez renseigner le nom d'entreprise");
        }

//        if (!StringUtils.hasLength(entrepriseDto.getPrenom())) {
//            errors.add("Veuillez renseigner le prenom d'entreprise");
//        }

        if (!StringUtils.hasLength(entrepriseDto.getEmail())) {
            errors.add("Veuillez renseigner l'email d'entreprise");
        }

//        if (!StringUtils.hasLength(entrepriseDto.getMotDePasse())) {
//            errors.add("Veuillez renseigner le mot de passe d'entreprise");
//        }
//
//        if (entrepriseDto.getDateDeNaissance() == null) {
//            errors.add("Veuillez renseigner la date de naissance d'entreprise");
//        }

        if (entrepriseDto.getAdresse() == null) {
            errors.add("Veuillez renseigner l'adresse d'entreprise");
        } else {
            if (!StringUtils.hasLength(entrepriseDto.getAdresse().getAdresse1())) {
                errors.add("Le champ 'Adresse 1' est obligatoire");
            }
            if (!StringUtils.hasLength(entrepriseDto.getAdresse().getVille())) {
                errors.add("Le champ 'Ville' est obligatoire");
            }
            if (!StringUtils.hasLength(entrepriseDto.getAdresse().getCodePostale())) {
                errors.add("Le champ 'Code Postale' est obligatoire");
            }
            if (!StringUtils.hasLength(entrepriseDto.getAdresse().getPays())) {
                errors.add("Le champ 'Pays' est obligatoire");
            }
        }
        return errors;
    }
}
