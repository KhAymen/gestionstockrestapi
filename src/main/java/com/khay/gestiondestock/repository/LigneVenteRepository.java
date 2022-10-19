package com.khay.gestiondestock.repository;

import com.khay.gestiondestock.model.LigneVente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LigneVenteRepository extends JpaRepository<LigneVente, Integer> {
    
    List<LigneVente> findAllByArticleId(Integer idArticle);

}
