package com.group04.tgdd.service;


import com.group04.tgdd.dto.SubCategoryDTO;
import com.group04.tgdd.model.Category;

import java.util.List;

public interface SubcategoryService {
    Category findById(Long id);
    List<Category> findAllSubcate(Long id);
    Category save(SubCategoryDTO categoryDTO);
    Category updateCSubcategory(SubCategoryDTO categoryDTO);
    Boolean deleteCategory(Long subcategoryId);
}
