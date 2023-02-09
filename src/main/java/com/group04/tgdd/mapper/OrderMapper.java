package com.group04.tgdd.mapper;

import com.group04.tgdd.dto.OrderHistoryResp;
import com.group04.tgdd.model.Orders;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderMapper {

    OrderHistoryResp toOrderHistory(Orders orders);

    List<OrderHistoryResp> toOrderHistory(List<Orders> orders);
}