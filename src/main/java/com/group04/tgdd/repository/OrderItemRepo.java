package com.group04.tgdd.repository;

import com.group04.tgdd.model.Category;
import com.group04.tgdd.model.OrderItem;
import com.group04.tgdd.model.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem,Long>{

    @Query("select o.productColor from OrderItem o where o in :orderItems")
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "10000")})
    List<ProductColor> getAllProductInOrderItems(List<OrderItem> orderItems);
}
