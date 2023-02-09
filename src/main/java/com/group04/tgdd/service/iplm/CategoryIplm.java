package com.group04.tgdd.service.iplm;

import com.group04.tgdd.dto.CategoryDTO;
import com.group04.tgdd.dto.SubCategoryDTO;
import com.group04.tgdd.exception.AppException;
import com.group04.tgdd.model.Category;
import com.group04.tgdd.repository.CategoryRepo;
import com.group04.tgdd.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryIplm implements CategoryService {
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    @Override
    public Category findById(Long id) {
        Optional<Category> category = categoryRepo.findById(id);
        return category.orElse(null);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepo.findAllParentCategory();
    }

    @Override
    public Category save(CategoryDTO categoryDTO) {
//        Category category = modelMapper.map(categoryDTO, Category.class);
        Category category= new Category();
        category.setCategoryName(categoryDTO.getCategoryName());
        return categoryRepo.save(category);
    }

    @Override
    public Category updateCategory(CategoryDTO categoryDTO) {
        Category categoryUpdate = findById(categoryDTO.getId());

        if (categoryUpdate != null) {
            categoryUpdate.setCategoryName(categoryDTO.getCategoryName());
//            Category temp = categoryRepo.getReferenceById(categoryDTO.getCategoryParentId());
//            categoryUpdate.setParentCategory(temp);
            return categoryRepo.save(categoryUpdate);
        } else throw new AppException(404, "Comment ID not found");
    }
    @Override
    public Boolean deleteCategory(Long categoryId) {

        Category categoryDelete = findById(categoryId);
        if(categoryDelete!= null){
            Category temp = categoryDelete.getParentCategory();
            if (temp!= null){
                categoryRepo.deleteById(categoryId);
                return true;
            } else {
                List<Category> subcate = categoryDelete.getSubcategories();
                for(Category sub :subcate){
                    sub.setParentCategory(null);
                }
                subcate.removeAll(subcate);
                categoryRepo.deleteById(categoryDelete.getId());
                return true;
            }
        } else return false;

    }
}


