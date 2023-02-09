package com.group04.tgdd.repository;

import com.group04.tgdd.model.Event;
import com.group04.tgdd.model.Manufacturer;
import com.group04.tgdd.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepo extends JpaRepository<Event,Long> {

    @Query("SELECT e FROM Event e WHERE e.name = :name")
    List<Event> findEventByName(String name);
    @Query("SELECT e.productList FROM Event e WHERE e.name = :name")
    List<Product> findEventProductList(String name);
    void deleteEventByName(String name);

    List<Event> findEventByProductListInAndExpireTimeGreaterThan(List<Product> products, LocalDateTime localDateTime);

    List<Event> findEventByExpireTimeGreaterThan(LocalDateTime localDateTime);
}
