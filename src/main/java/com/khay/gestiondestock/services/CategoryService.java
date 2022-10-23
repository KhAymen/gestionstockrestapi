package com.khay.gestiondestock.services;

import com.khay.gestiondestock.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto save(CategoryDto categoryDto);

    CategoryDto findById(Integer id);

    List<CategoryDto> findAll();

    CategoryDto findByCode(String code);

    void delete(Integer id);
}
