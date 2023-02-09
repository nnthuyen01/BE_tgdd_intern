package com.group04.tgdd.repository;

import com.group04.tgdd.model.OrderItem;
import com.group04.tgdd.model.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;

public interface ProductColorRepo extends JpaRepository<ProductColor,Long>{
    List<ProductColor> findAllByIdIn(List<Long> productColorId);




}
