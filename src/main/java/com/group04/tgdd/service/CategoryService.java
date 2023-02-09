package com.group04.tgdd.service;

import com.group04.tgdd.dto.CategoryDTO;
import com.group04.tgdd.dto.SubCategoryDTO;
import com.group04.tgdd.model.Category;

import java.util.List;

public interface CategoryService {

    Category findById(Long id);
    List<Category> findAll();
    Category save(CategoryDTO categoryDTO);
    Category updateCategory(CategoryDTO categoryDTO);
    Boolean deleteCategory(Long categoryId);
}
