package com.group04.tgdd.repository;

import com.group04.tgdd.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepo extends JpaRepository<Shop, Long> {

    Boolean findByName(String name);
}
