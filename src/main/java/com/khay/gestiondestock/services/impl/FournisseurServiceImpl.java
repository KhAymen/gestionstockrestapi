package com.khay.gestiondestock.services.impl;

import com.khay.gestiondestock.dto.FournisseurDto;
import com.khay.gestiondestock.exception.ErrorCodes;
import com.khay.gestiondestock.exception.InvalidEntityException;
import com.khay.gestiondestock.repository.FournisseurRepository;
import com.khay.gestiondestock.services.FournisseurService;
import com.khay.gestiondestock.validator.FournisseurValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FournisseurServiceImpl implements FournisseurService {

    private FournisseurRepository fournisseurRepository;

    @Autowired
    public FournisseurServiceImpl(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
    }


    @Override
    public FournisseurDto save(FournisseurDto fournisseur) {
        List<String> errors = FournisseurValidator.validate(fournisseur);
        if (!errors.isEmpty()) {
            log.error("Article is not valide {}", fournisseur);
            throw new InvalidEntityException("L'article n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        return FournisseurDto.fromEntity(fournisseurRepository.save(FournisseurDto.toEntity(fournisseur)));

    }

    @Override
    public FournisseurDto findById(Integer id) {
        return null;
    }

    @Override
    public List<FournisseurDto> findAll() {
        return null;
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Fourn isseur ID is null");
            return;
        }
        fournisseurRepository.deleteById(id);
    }
}
