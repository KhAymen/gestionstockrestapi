package com.khay.gestiondestock.services.impl;

import com.khay.gestiondestock.dto.MvtStkDto;
import com.khay.gestiondestock.exception.ErrorCodes;
import com.khay.gestiondestock.exception.InvalidEntityException;
import com.khay.gestiondestock.model.TypeMvtStock;
import com.khay.gestiondestock.repository.MvtStkRepository;
import com.khay.gestiondestock.services.ArticleService;
import com.khay.gestiondestock.services.MvtStkService;
import com.khay.gestiondestock.validator.MvtStockValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class MvtStkServiceImpl implements MvtStkService {


    private MvtStkRepository repository;
    private ArticleService articleService;

    @Autowired
    public MvtStkServiceImpl(MvtStkRepository repository, ArticleService articleService) {
        this.repository = repository;
        this.articleService = articleService;
    }

    @Override
    public BigDecimal stockReelArticle(Integer idArticle) {
        if (idArticle == null) {
            log.warn("ID article est null");
            return BigDecimal.valueOf(-1);
        }
        articleService.findById(idArticle);
        return repository.stockReelArticle(idArticle);
    }

    @Override
    public List<MvtStkDto> mvtStkArticle(Integer idArticle) {
        return repository.findAllByArticleId(idArticle)
                .stream()
                .map(MvtStkDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public MvtStkDto entreeStock(MvtStkDto dto) {
        return entreePostive(dto, TypeMvtStock.ENTREE);
    }

    @Override
    public MvtStkDto sortieStk(MvtStkDto dto) {
        return sortieNegative(dto, TypeMvtStock.SORTIE);
    }

    @Override
    public MvtStkDto correctionStockPos(MvtStkDto dto) {
        return entreePostive(dto, TypeMvtStock.CORRECTION_POS);
    }

    @Override
    public MvtStkDto correctionStockNeg(MvtStkDto dto) {
        return entreePostive(dto, TypeMvtStock.CORRECTION_NEG);
    }


    private MvtStkDto entreePostive(MvtStkDto dto, TypeMvtStock typeMvtStock) {
        List<String> errors = MvtStockValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("le stock id n'est pas valide", dto);
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
        }
        dto.setTypeMvt(typeMvtStock); // For making sure that is an entrance
        dto.setQuantite(BigDecimal.valueOf(Math.abs(dto.getQuantite().doubleValue())));
        return MvtStkDto.fromEntity(repository.save(MvtStkDto.toEntity(dto)));
    }

    private MvtStkDto sortieNegative(MvtStkDto dto, TypeMvtStock typeMvtStock) {
        List<String> errors = MvtStockValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("le stock id n'est pas valide");
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
        }
        dto.setTypeMvt(typeMvtStock); // For making sure that is exit
        dto.setQuantite(BigDecimal.valueOf(Math.abs(dto.getQuantite().doubleValue()) * -1));
        return MvtStkDto.fromEntity(repository.save(MvtStkDto.toEntity(dto)));
    }
}
