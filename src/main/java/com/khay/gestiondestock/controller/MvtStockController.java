package com.khay.gestiondestock.controller;

import com.khay.gestiondestock.controller.api.MvtStkApi;
import com.khay.gestiondestock.dto.MvtStkDto;
import com.khay.gestiondestock.services.MvtStkService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class MvtStockController implements MvtStkApi {

    private MvtStkService mvtStkService;

    @Autowired
    public MvtStockController(MvtStkService mvtStkService) {
        this.mvtStkService = mvtStkService;
    }

    @Override
    public BigDecimal stockReelArticle(Integer idArticle) {
        return mvtStkService.stockReelArticle(idArticle);
    }

    @Override
    public List<MvtStkDto> mvtStkArticle(Integer idArticle) {
        return mvtStkService.mvtStkArticle(idArticle);
    }

    @Override
    public MvtStkDto entreeStock(MvtStkDto mvtStkDto) {
        return mvtStkService.entreeStock(mvtStkDto);
    }

    @Override
    public MvtStkDto sortieStk(MvtStkDto mvtStkDto) {
        return mvtStkService.sortieStk(mvtStkDto);
    }

    @Override
    public MvtStkDto correctionStockPos(MvtStkDto mvtStkDto) {
        return mvtStkService.correctionStockPos(mvtStkDto);
    }

    @Override
    public MvtStkDto correctionStockNeg(MvtStkDto mvtStkDto) {
        return mvtStkService.correctionStockNeg(mvtStkDto);
    }
}
