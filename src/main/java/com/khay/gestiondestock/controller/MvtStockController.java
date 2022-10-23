package com.khay.gestiondestock.controller;


import com.khay.gestiondestock.controller.api.MvtStkApi;
import com.khay.gestiondestock.dto.MvtStkDto;
import com.khay.gestiondestock.services.MvtStkService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class MvtStockController implements MvtStkApi {

    private MvtStkService service;

    @Autowired
    public MvtStockController( MvtStkService service) {
        this.service = service;
    }

    @Override
    public BigDecimal stockReelArticle(Integer idArticle) {
        return service.stockReelArticle (idArticle);
    }

    @Override
    public List<MvtStkDto> mvtStkArticle(Integer idArticle) {
        return service.mvtStkArticle (idArticle);
    }

    @Override
    public MvtStkDto entreeStock(MvtStkDto dto) {
        return service.entreeStock (dto);
    }

    @Override
    public MvtStkDto sortieStk(MvtStkDto dto) {
        return service.sortieStk (dto);
    }

    @Override
    public MvtStkDto correctionStockPos(MvtStkDto dto) {
        return service.correctionStockPos (dto);
    }

    @Override
    public MvtStkDto correctionStockNeg(MvtStkDto dto) {
        return service.correctionStockNeg (dto);
    }
}
