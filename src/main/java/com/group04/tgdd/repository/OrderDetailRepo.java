package com.group04.tgdd.repository;

import com.group04.tgdd.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepo extends JpaRepository<OrderDetail,Long> {
    @Query("select p from OrderDetail p where (p.orders = :orders)")
    OrderDetail findAllByKeyword(Orders orders);
}
