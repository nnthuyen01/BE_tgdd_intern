package com.group04.tgdd.controller;

import com.group04.tgdd.dto.SubCategoryDTO;
import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.model.Category;
import com.group04.tgdd.service.SubcategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class SubcategoryController {
    private final SubcategoryService subcategoryService;
    //Get all categoty
    @GetMapping("/subcategoryOf/{cateId}")
    public ResponseEntity<?> findAll(@PathVariable Long cateId){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",
                subcategoryService.findAllSubcate(cateId)));
    }

    // Get category by ID
    @GetMapping("/subcategory/{subcategoryId}")
    public ResponseEntity<?> findById(@PathVariable Long subcategoryId){
        Category subcategory = subcategoryService.findById(subcategoryId);
        if (subcategory!=null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",subcategory));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Subcategory ID not exits",null));
    }

    // Add SubCategory
    //requied name, parentId
    @PostMapping("/admin/subcategory")
    public ResponseEntity<?> saveCategory(@RequestBody SubCategoryDTO CategoryDTO){

        Category subcategorySave =  subcategoryService.save(CategoryDTO);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",subcategorySave));
    }

    // Update subcategory
    @PutMapping("/admin/subcategory")
    public ResponseEntity<?> updateCategory(@RequestBody SubCategoryDTO categoryDTO){
        Category subcategoryUpdate = subcategoryService.updateCSubcategory(categoryDTO);
        if (subcategoryUpdate!= null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",subcategoryUpdate));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Subcategory ID not exits",null));
    }

    //Delete category
    @DeleteMapping("/admin/subcategory/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        boolean check = subcategoryService.deleteCategory(id);
        if (check){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false,"Subcategory ID not exits",null));
    }
}
