package com.group04.tgdd.service;

import com.group04.tgdd.dto.OrderDetailResp;
import com.group04.tgdd.dto.OrderReq;
import com.group04.tgdd.model.Orders;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OrderService {
    List<Orders> getOrderHistory(final Long userId, Integer page, Integer size);

    List<Orders> searchOrder(final String state, Integer page, Integer size);

    OrderDetailResp findByIdOrder(Long id);

    List<Orders> findAllOrder(int page, int size);

    Long countOrderByDay(int day, int month, int year);

    BigDecimal countRevenueByDay(int day, int month, int year);

    Orders updateStatusOrder(Long orderId, String status);

    Orders save(Orders orders);
}
