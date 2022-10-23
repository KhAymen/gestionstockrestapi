package com.khay.gestiondestock.services;


import com.khay.gestiondestock.dto.ArticleDto;
import com.khay.gestiondestock.dto.LigneCommandeClientDto;
import com.khay.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.khay.gestiondestock.dto.LigneVenteDto;
import com.khay.gestiondestock.model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {

    ArticleDto save(ArticleDto article);

    ArticleDto findById(Integer id);

    Optional<Article> findByCodeArticle(String codeArticle);

    List<ArticleDto> findAll();

    List<LigneVenteDto> findHistoriqueVentes(Integer idArticle);

    List<LigneCommandeClientDto> findHistoriqueCommandeClient(Integer idArticle);

    List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle);

    List<ArticleDto> findAllArticleByIdCategory(Integer idCategory);

    void delete(Integer id);
}
