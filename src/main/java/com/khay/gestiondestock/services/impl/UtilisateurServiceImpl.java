package com.khay.gestiondestock.services.impl;

import com.khay.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.khay.gestiondestock.dto.UtilisateurDto;
import com.khay.gestiondestock.exception.EntityNotFoundException;
import com.khay.gestiondestock.exception.ErrorCodes;
import com.khay.gestiondestock.exception.InvalidOperationException;
import com.khay.gestiondestock.model.Utilisateur;
import com.khay.gestiondestock.repository.UtilisateurRepository;
import com.khay.gestiondestock.services.UtilisateurService;
import jdk.jshell.execution.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {

    private UtilisateurRepository utilisateurRepository;

    @Autowired
    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UtilisateurDto save(UtilisateurDto dto) {
        return null;
    }

    @Override
    public UtilisateurDto findById(Integer id) {

        if (id == null) {
            log.error("L'id de l'utilistaeur est null");
            return null;
        }

        return utilisateurRepository.findById(id)
                .map(UtilisateurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Aucun utilisateur avec cet ID " + id, ErrorCodes.UTILISATEUR_NOT_FOUND));
        //.orElseThrow (() -> new EntityNotFoundException ("Aucun utilisateur avec cet ID "
        //      + id + "n'a été trouvé dans la base de donnée"), ErrorCodes.UTILISATEUR_NOT_FOUND);
    }

    @Override
    public List<UtilisateurDto> findAll() {
        return utilisateurRepository.findAll().stream()
                .map(UtilisateurDto::fromEntity).collect(Collectors.toList());
    }


    // TODO change the output to optioanl to fix the problem
    @Override
    public UtilisateurDto findByEmail(String email) {

       // if (StringUtils.hasLength(email)) {
        if (!StringUtils.hasLength(email)) {
            log.error("L'email est null");
            return null;
        }

        return utilisateurRepository.findUtilisateurByEmail(email)
                .map(UtilisateurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec cet email " + email + " n' a ete trouvé dans la base de données",
                        ErrorCodes.UTILISATEUR_NOT_FOUND)
                );
    }

    @Override
    public UtilisateurDto changerMotDePasseUtiliasateur(ChangerMotDePasseUtilisateurDto dto) {
        validte(dto);

        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(dto.getId());

        if (utilisateurOptional.isEmpty()) {
            log.warn("Aucun utilisateur n'a été trouvé dans la base de donné");
            throw new EntityNotFoundException("Aucun utilisateur n'a été trouvé dans la base de donné",
                    ErrorCodes.UTILISATEUR_NOT_FOUND);
        }

        Utilisateur utilisateur = utilisateurOptional.get();
        utilisateur.setMotDePasse(dto.getMotDePasse());
        return UtilisateurDto.fromEntity(utilisateurRepository.save(utilisateur));
    }

    private void validte(ChangerMotDePasseUtilisateurDto dto) {
        if (dto == null) {
            log.error("Impossible de modifier le mot de passe avec un objet null");
            throw new InvalidOperationException("Aucune information n'a été fournie pour changer le mot de passe ",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
        }
        if (dto.getId() == null) {
            log.error("Impossible de modifier le mot de passe avec un ID null");
            throw new InvalidOperationException("ID utilisateur null, Impossible de modifier le mot de passe ",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);

        }
        if (!StringUtils.hasLength(dto.getMotDePasse()) || !StringUtils.hasLength(dto.getConfirmMotDePasse())) {
            log.error("Impossible de modifier le mot de passe avec deux mot de passe différent");
            throw new InvalidOperationException("Mot de passe utilisateur non conforme ",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);

        }
        if (!dto.getMotDePasse().equals(dto.getConfirmMotDePasse())) {
            log.error("Impossible de modifier le mot de passe avec un ID null");
            throw new InvalidOperationException("ID utilisateur null, Impossible de modifier le mot de passe ",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);

        }
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Utilisateur Id est null");
            return;
        }
        utilisateurRepository.deleteById(id);
    }
}
