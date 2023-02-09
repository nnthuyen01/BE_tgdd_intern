package com.group04.tgdd.repository;

import com.group04.tgdd.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category,Long> {
    @Query("select c from Category c where c.parentCategory is null ")
    List<Category> findAllParentCategory();

    @Query("select c from Category c where c.parentCategory.id =:id")
    List<Category> findAllSubcateByCate(Long id);
}
