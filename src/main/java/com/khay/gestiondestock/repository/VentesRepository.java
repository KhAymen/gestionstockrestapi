package com.khay.gestiondestock.repository;

import com.khay.gestiondestock.model.Ventes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VentesRepository extends JpaRepository<Ventes, Integer> {

//    Optional<LigneVente> findVentesByCode(String code);
    Optional<Ventes> findVentesByCode(String code);

}
