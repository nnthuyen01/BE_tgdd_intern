package com.group04.tgdd.repository;

import com.group04.tgdd.model.Category;
import com.group04.tgdd.model.Manufacturer;
import com.group04.tgdd.model.Product;
import com.group04.tgdd.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {
    @Query("select p from Product p where p.enable = true and"+
            " ((:category is null) or (p.category = :category)) and" +
            "((:subcategory is null) or (p.subcategory = :subcategory)) and" +
            "((:manufacturer is null ) or (p.manufacturer = :manufacturer))" +
            "and (concat('%',lower(p.name),'%') like concat('%',lower(:keyword),'%'))")
    List<Product> findAllByKeyword(String keyword, Category category, Category subcategory, Manufacturer manufacturer, Pageable pageable);

    @Query(
            "SELECT p FROM Product p WHERE (concat('%',lower(p.name),'%') LIKE concat('%',lower(:keyword),'%')) "
    )
    List<Product> search(String keyword, Pageable pageable);

    @Query(
            "SELECT p.eventList FROM Product as p WHERE p.id = :pid"
    )
    List<Event> findEventListBy(Long pid);
}
