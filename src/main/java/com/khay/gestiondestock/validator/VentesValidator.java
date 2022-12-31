package com.khay.gestiondestock.validator;


import com.khay.gestiondestock.dto.VentesDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


// TODO code,datevente
public class VentesValidator {

    public static List<String> validate(VentesDto ventesDto) {
        List<String> errors = new ArrayList<>();
        if (ventesDto == null) {
            errors.add("Veuillez renseigner le code de la vente");
            errors.add("Veuillez renseigner la date de la vente");

            return errors;
        }

        if (!StringUtils.hasLength(ventesDto.getCode())) {
            errors.add("Veuillez renseigner le code de la vente");
        }

        if (ventesDto.getDateVente() == null) {
            errors.add("Veuillez renseigner la date de la vente");
        }


        return errors;
    }
}
