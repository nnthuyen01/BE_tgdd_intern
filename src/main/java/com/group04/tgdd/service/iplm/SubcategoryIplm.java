package com.group04.tgdd.service.iplm;

import com.group04.tgdd.dto.SubCategoryDTO;
import com.group04.tgdd.model.Category;
import com.group04.tgdd.repository.CategoryRepo;
import com.group04.tgdd.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SubcategoryIplm implements SubcategoryService {

    private final CategoryRepo categoryRepo;
    @Override
    public Category findById(Long id) {
        Optional<Category> subcategory = categoryRepo.findById(id);
        return subcategory.orElse(null);
    }

    @Override
    public List<Category> findAllSubcate(Long id) {
//        boolean check = categoryRepo.existsById(id);
//        if(check){
//            return categoryRepo.findAllSubcateByCate(id);
//        }
//        else return null;
        return categoryRepo.findAllSubcateByCate(id);

    }

    @Override
    public Category save(SubCategoryDTO categoryDTO) {
        Category subcategory= new Category();
        subcategory.setCategoryName(categoryDTO.getName());
        Category category =categoryRepo.getReferenceById(categoryDTO.getCategoryId());
        subcategory.setParentCategory(category);
        return categoryRepo.save(subcategory);
    }

    @Override
    public Category updateCSubcategory(SubCategoryDTO categoryDTO) {

        Category subcategoryUpdate = findById(categoryDTO.getId());

        if (subcategoryUpdate != null){
            subcategoryUpdate.setCategoryName(categoryDTO.getName());
            Category category =categoryRepo.getReferenceById(categoryDTO.getCategoryId());
            subcategoryUpdate.setParentCategory(category);
            return categoryRepo.save(subcategoryUpdate);
        }
        else return null;
    }

    @Override
    public Boolean deleteCategory(Long subcategoryId) {
        boolean check = categoryRepo.existsById(subcategoryId);
        if(check){
            categoryRepo.deleteById(subcategoryId);
            return true;
        }
        else return false;
    }
}
