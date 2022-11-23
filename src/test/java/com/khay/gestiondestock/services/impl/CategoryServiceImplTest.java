package com.khay.gestiondestock.services.impl;

import com.khay.gestiondestock.dto.CategoryDto;
import com.khay.gestiondestock.exception.EntityNotFoundException;
import com.khay.gestiondestock.exception.ErrorCodes;
import com.khay.gestiondestock.exception.InvalidEntityException;
import com.khay.gestiondestock.services.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void shouldSaveCategoryWithSuccess() {

        CategoryDto expectedCategory = CategoryDto.builder()
                .code("code test")
                .designation("Designation test")
                .idEntreprise(1)
                .build();

        CategoryDto savedCategory = categoryService.save(expectedCategory);

        assertNotNull(savedCategory);
        assertNotNull(savedCategory.getId());
        assertEquals(expectedCategory.getCode(), savedCategory.getCode());
        assertEquals(expectedCategory.getDesignation(), savedCategory.getDesignation());
        assertEquals(expectedCategory.getIdEntreprise(), savedCategory.getIdEntreprise());
    }


    @Test
    public void shouldUpdateCategoryWithSuccess() {

        CategoryDto expectedCategory = CategoryDto.builder()
                .code("code test")
                .designation("Designation test")
                .idEntreprise(1)
                .build();

        CategoryDto savedCategory = categoryService.save(expectedCategory);

        CategoryDto categoryToUpdate = savedCategory;
        categoryToUpdate.setCode("Cat update");

        savedCategory = categoryService.save(categoryToUpdate);

        assertNotNull(categoryToUpdate);
        assertNotNull(categoryToUpdate.getId());
        assertEquals(categoryToUpdate.getCode(), savedCategory.getCode());
        assertEquals(categoryToUpdate.getDesignation(), savedCategory.getDesignation());
        assertEquals(categoryToUpdate.getIdEntreprise(), savedCategory.getIdEntreprise());
    }

    @Test
    public void shouldThrowInvalidEntityException() {
        CategoryDto expectedCategory = CategoryDto.builder().build();

        InvalidEntityException expectedException = assertThrows(InvalidEntityException.class, () -> categoryService.save(expectedCategory));

        assertEquals(ErrorCodes.CATEGORY_NOT_VALID, expectedException.getErrorCode());
        assertEquals(1, expectedException.getErrors().size());
        assertEquals("Veuillez renseigner le code de la categorie", expectedException.getErrors().get(0));

    }

    @Test
    public void shouldThrowEntityNotFoundException() {

        EntityNotFoundException expectedException = assertThrows(EntityNotFoundException.class, () -> categoryService.findById(0));

        assertEquals(ErrorCodes.CATEGORY_NOT_FOUND, expectedException.getErrorCode());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundException2() {
        categoryService.findById(0);
    }


}