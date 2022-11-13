package com.khay.gestiondestock.services.impl;

import com.khay.gestiondestock.dto.EntrepriseDto;
import com.khay.gestiondestock.dto.RolesDto;
import com.khay.gestiondestock.dto.UtilisateurDto;
import com.khay.gestiondestock.exception.EntityNotFoundException;
import com.khay.gestiondestock.exception.ErrorCodes;
import com.khay.gestiondestock.exception.InvalidEntityException;
import com.khay.gestiondestock.repository.EntrepriseRepository;
import com.khay.gestiondestock.repository.RolesRepository;
import com.khay.gestiondestock.services.EntrepriseService;
import com.khay.gestiondestock.services.UtilisateurService;
import com.khay.gestiondestock.validator.EntrepriseValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@Transactional(rollbackOn = Exception.class)
@Service
@Slf4j
public class EntrepriseServiceImpl implements EntrepriseService {

    EntrepriseRepository entrepriseRepository;
    UtilisateurService utilisateurService;
    RolesRepository rolesRepository;

    @Autowired
    public EntrepriseServiceImpl(EntrepriseRepository entrepriseRepository, UtilisateurService utilisateurService, RolesRepository rolesRepository) {
        this.entrepriseRepository = entrepriseRepository;
        this.utilisateurService = utilisateurService;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public EntrepriseDto save(EntrepriseDto entrepriseDto) {
        List<String> errors = EntrepriseValidator.validate(entrepriseDto);
        if (!errors.isEmpty()) {
            log.error("Entreprise is not valid{}", entrepriseDto);
            throw new InvalidEntityException("L'entreprise n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID);
        }

        EntrepriseDto savedEntreprise = EntrepriseDto.fromEntiy(
                entrepriseRepository.save(EntrepriseDto.toEntity(entrepriseDto))
        );

        UtilisateurDto utilisateurDto = fromEntrprise(savedEntreprise);

        UtilisateurDto savedUser = utilisateurService.save(utilisateurDto);

        RolesDto rolesDto = RolesDto.builder()
                .roleName("ADMIN")
                .utilisateur(savedUser)
                .build();

        rolesRepository.save(RolesDto.toEntity(rolesDto));

        return savedEntreprise;
    }

    private UtilisateurDto fromEntrprise(EntrepriseDto entrepriseDto) {
        return UtilisateurDto.builder()
                .adresse(entrepriseDto.getAdresse())
                .nom(entrepriseDto.getNom())
                .prenom(entrepriseDto.getCodeFiscal())
                .email(entrepriseDto.getEmail())
                .motDePasse(generateRandomPassword())
                .entreprise(entrepriseDto)
                .dateDeNaissance(Instant.now())
                .photo((entrepriseDto.getPhoto()))
                .build();
    }

    private String generateRandomPassword() {
        return "som3R@nd0mP@$$word";
    }

    @Override
    public EntrepriseDto findById(Integer id) {
        if(id == null) {
            log.error("Entreprise ID is null");
            return null;
        }

        return entrepriseRepository.findById(id)
                .map(EntrepriseDto::fromEntiy)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune entreprise avec l'ID = " + id + " n'a ete trouve dans la BDD",
                        ErrorCodes.ENTREPRISE_NOT_FOUND
                ))
                ;
    }

    @Override
    public List<EntrepriseDto> findAll() {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
