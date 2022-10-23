package com.khay.gestiondestock.services;

import com.khay.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.khay.gestiondestock.dto.UtilisateurDto;

import java.util.List;

public interface UtilisateurService {

    UtilisateurDto save(UtilisateurDto utilisateurDto);

    UtilisateurDto findById(Integer id);

    List<UtilisateurDto> findAll();

    UtilisateurDto findByEmail(String email);

    UtilisateurDto changerMotDePasseUtiliasateur(ChangerMotDePasseUtilisateurDto dto);

    void delete(Integer id);
}
