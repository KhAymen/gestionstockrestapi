package com.khay.gestiondestock.services.impl;

import com.khay.gestiondestock.dto.ArticleDto;
import com.khay.gestiondestock.dto.LigneVenteDto;
import com.khay.gestiondestock.dto.MvtStkDto;
import com.khay.gestiondestock.dto.VentesDto;
import com.khay.gestiondestock.exception.EntityNotFoundException;
import com.khay.gestiondestock.exception.ErrorCodes;
import com.khay.gestiondestock.exception.InvalidEntityException;
import com.khay.gestiondestock.model.Article;
import com.khay.gestiondestock.model.LigneVente;
import com.khay.gestiondestock.model.TypeMvtStock;
import com.khay.gestiondestock.model.Ventes;
import com.khay.gestiondestock.repository.ArticleRepository;
import com.khay.gestiondestock.repository.LigneVenteRepository;
import com.khay.gestiondestock.repository.VentesRepository;
import com.khay.gestiondestock.services.MvtStkService;
import com.khay.gestiondestock.services.VentesService;

import com.khay.gestiondestock.validator.VentesValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class VentesServiceImpl implements VentesService {

    private ArticleRepository articleRepository;
    private VentesRepository ventesRepository;
    private LigneVenteRepository ligneVenteRepository;
    private MvtStkService mvtStkService;


    @Autowired
    public VentesServiceImpl(ArticleRepository articleRepository, VentesRepository ventesRepository, LigneVenteRepository ligneVenteRepository, MvtStkService mvtStkService) {
        this.articleRepository = articleRepository;
        this.ventesRepository = ventesRepository;
        this.ligneVenteRepository = ligneVenteRepository;
        this.mvtStkService = mvtStkService;
    }

    @Override
    public VentesDto save(VentesDto ventesDto) {
        List<String> errors = VentesValidator.validate(ventesDto);

        if (!errors.isEmpty()) {
            log.error("Ventes n'est pas valide ");
            throw new InvalidEntityException("L'objet vente n'est pas valide", ErrorCodes.VENTE_NOT_VALID, errors);
        }

        List<String> articleErrors = new ArrayList<>();

        ventesDto.getLigneVentes().forEach(ligneVenteDto -> {
            Optional<Article> article = articleRepository.findById(ligneVenteDto.getArticle().getId());
            if (article.isEmpty()) {
                articleErrors.add("Aucun article avec l'ID " + ligneVenteDto.getArticle().getId() + " n'a été trouvé dans la base de données");
            }
        });

        if (!articleErrors.isEmpty()) {
            log.error("One or more articles were not found in the DB, {}", errors);
            throw new InvalidEntityException("Un ou plusieurs articles n'ont pas été trouvé dans la base de données", ErrorCodes.VENTE_NOT_VALID, errors);
        }

        Ventes savedVente = ventesRepository.save(VentesDto.toEntity(ventesDto));

        ventesDto.getLigneVentes().forEach(ligneVenteDto -> {
            LigneVente ligneVente = LigneVenteDto.toEntity(ligneVenteDto);
            ligneVente.setVente(savedVente);
            ligneVenteRepository.save(ligneVente);
//            updateMvtStock(ligneVente);
        });
        return VentesDto.fromEntity(savedVente);
    }

    @Override
    public VentesDto findById(Integer id) {
        if (id == null) {
            log.warn("Vente ID is NULL");
            return null;
        }
        return ventesRepository.findById(id)
                .map(VentesDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException("Aucune vente n'a été trouvé dans la base de données", ErrorCodes.VENTE_NOT_FOUND));
    }

    @Override
    public VentesDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Vente CODE is NULL");
            return null;
        }
        return ventesRepository.findVentesByCode(code)
                .map(VentesDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException("Aucune vente n'a été trouvé avec ce code ", ErrorCodes.VENTE_NOT_FOUND));
    }

    @Override
    public List<VentesDto> findAll() {
        return ventesRepository.findAll().stream()
                .map(VentesDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Vente ID is NULL");
            return;
        }
        ventesRepository.deleteById(id);
    }


    private void updateMvtStock(LigneVente ligneVente) {

        MvtStkDto sortieDtk = MvtStkDto.builder()
                .article(ArticleDto.fromEntity(ligneVente.getArticle()))
                .dateMvt(Instant.now())
                .typeMvt(TypeMvtStock.SORTIE)
//                .sourceMvtStock(SourceMvtStock.VENTE)
                .quantite(ligneVente.getQuantite())
                .idEntreprise(ligneVente.getIdEntreprise())
                .build();
        mvtStkService.sortieStk(sortieDtk);
    }
}
