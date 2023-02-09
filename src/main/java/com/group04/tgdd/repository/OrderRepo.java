package com.group04.tgdd.repository;


import com.group04.tgdd.model.Orders;

import com.group04.tgdd.model.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface OrderRepo extends JpaRepository<Orders,Long> {
    List<Orders> findAllByOrderByCreateTimeDesc(Pageable pageable);

    List<Orders> findAllByOrderUser(Users user);

    List<Orders> findByState(String state);

    Orders findAllByPaymentId(String paymentID);

    @Query("select count(o) from Orders o where o.state = 'paid' and o.createTime>=:day")
    Long countAllTimeGreaterThanEqual(LocalDateTime day);


    @Query("select SUM(o.orderdetail.totalPrice) from Orders o where o.createTime >= :date")
    BigDecimal totalRevenue(LocalDateTime date);

    @Query("select SUM(o.orderdetail.totalPrice) from Orders o")
    BigDecimal totalAllRevenue();


    Orders findOrdersByPaypalToken(String token);


}
