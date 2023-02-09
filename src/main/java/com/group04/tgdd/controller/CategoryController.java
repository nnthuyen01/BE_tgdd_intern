package com.group04.tgdd.controller;

import com.group04.tgdd.dto.CategoryDTO;
import com.group04.tgdd.dto.SubCategoryDTO;
import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.model.Category;
import com.group04.tgdd.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class CategoryController {
    private final CategoryService categoryService;

    //Get all categoty
    @GetMapping("/category")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",categoryService.findAll()));
    }

    // Get category by ID
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> findById(@PathVariable Long categoryId){
        Category category = categoryService.findById(categoryId);
        if (category!=null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",category));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Category ID not exits",null));
    }

    // Add Category
    //requied Name
    @PostMapping("/admin/category")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDTO categoryDTO){
        Category categorySave =  categoryService.save(categoryDTO);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",categorySave));
    }

    // Update category
    @PutMapping("/admin/category")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDTO categoryDTO){
        Category categoryUpdate = categoryService.updateCategory(categoryDTO);
        if (categoryUpdate!= null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",categoryUpdate));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Category ID not exits",null));
    }

    //Delete category
    @DeleteMapping("/admin/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        boolean check = categoryService.deleteCategory(id);
        if (check){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false,"Category ID not exits",null));
    }
}
