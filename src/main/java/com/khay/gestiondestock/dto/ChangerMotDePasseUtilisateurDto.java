package com.khay.gestiondestock.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangerMotDePasseUtilisateurDto {

    private Integer id;

//    private String email;

    private String motDePasse;

    private String confirmMotDePasse;
}
