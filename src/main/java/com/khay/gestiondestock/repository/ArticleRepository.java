package com.khay.gestiondestock.repository;


import com.khay.gestiondestock.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    Optional<Article> findArticleByCodeArticle(String code);

    List<Article> findAllByCategoryId(Integer idCategory);

    @Query(value = "select * from article where codearticle= :code", nativeQuery = true)
    List<Article> findByCustomNativeQuery(@Param("code") String c);

    List<Article> findByCodeArticleAndDesignation(String codeArticle, String designation);
}
