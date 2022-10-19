package com.khay.gestiondestock.repository;


import com.khay.gestiondestock.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findCategoryByCode(String code);

//    Optional<Category> findByIdAndIdEntreprise(Integer id, Integer idEntreprise);
}
