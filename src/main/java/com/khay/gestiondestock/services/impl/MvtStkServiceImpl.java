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


    private MvtStkRepository mvtStkRepository;
    private ArticleService articleService;

    @Autowired
    public MvtStkServiceImpl(MvtStkRepository repository, ArticleService articleService) {
        this.mvtStkRepository = repository;
        this.articleService = articleService;
    }

    @Override
    public BigDecimal stockReelArticle(Integer idArticle) {
        if (idArticle == null) {
            log.warn("ID article is NULL");
            return BigDecimal.valueOf(-1);
        }
        articleService.findById(idArticle);
        return mvtStkRepository.stockReelArticle(idArticle);
    }

    @Override
    public List<MvtStkDto> mvtStkArticle(Integer idArticle) {
        return mvtStkRepository.findAllByArticleId(idArticle).stream()
                .map(MvtStkDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public MvtStkDto entreeStock(MvtStkDto mvtStkDto) {
        return entreePostive(mvtStkDto, TypeMvtStock.ENTREE);
    }

    @Override
    public MvtStkDto sortieStock(MvtStkDto mvtStkDto) {
        return sortieNegative(mvtStkDto, TypeMvtStock.SORTIE);
    }

    @Override
    public MvtStkDto correctionStockPos(MvtStkDto mvtStkDto) {
        return entreePostive(mvtStkDto, TypeMvtStock.CORRECTION_POS);
    }

    @Override
    public MvtStkDto correctionStockNeg(MvtStkDto mvtStkDto) {
        return sortieNegative(mvtStkDto, TypeMvtStock.CORRECTION_NEG);
    }


    private MvtStkDto entreePostive(MvtStkDto mvtStkDto, TypeMvtStock typeMvtStock) {
        List<String> errors = MvtStockValidator.validate(mvtStkDto);
        if (!errors.isEmpty()) {
            log.error("le stock id n'est pas valide", mvtStkDto);
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
        }
        mvtStkDto.setTypeMvt(typeMvtStock); // For making sure that is an entrance
        mvtStkDto.setQuantite(
                BigDecimal.valueOf(
                        Math.abs(mvtStkDto.getQuantite().doubleValue())
                )
        );
        return MvtStkDto.fromEntity(
                mvtStkRepository.save(MvtStkDto.toEntity(mvtStkDto))
        );
    }

    private MvtStkDto sortieNegative(MvtStkDto mvtStkDto, TypeMvtStock typeMvtStock) {
        List<String> errors = MvtStockValidator.validate(mvtStkDto);
        if (!errors.isEmpty()) {
            log.error("le stock id n'est pas valide");
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
        }
        mvtStkDto.setTypeMvt(typeMvtStock); // For making sure that is exit
        mvtStkDto.setQuantite(
                BigDecimal.valueOf(
                        Math.abs(mvtStkDto.getQuantite().doubleValue()) * -1
                )
        );
        return MvtStkDto.fromEntity(
                mvtStkRepository.save(MvtStkDto.toEntity(mvtStkDto))
        );
    }
}
