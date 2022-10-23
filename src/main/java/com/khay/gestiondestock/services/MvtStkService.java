package com.khay.gestiondestock.services;


import com.khay.gestiondestock.dto.MvtStkDto;

import java.math.BigDecimal;
import java.util.List;

public interface MvtStkService {

    BigDecimal stockReelArticle(Integer idArticle);

    List<MvtStkDto> mvtStkArticle(Integer idArticle);

    MvtStkDto entreeStock(MvtStkDto mvtStkDto);

    MvtStkDto sortieStk(MvtStkDto mvtStkDto);

    MvtStkDto correctionStockPos(MvtStkDto mvtStkDto);

    MvtStkDto correctionStockNeg(MvtStkDto mvtStkDto);
}
