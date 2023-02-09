
package com.group04.tgdd.mapper.impl;

        import com.group04.tgdd.dto.OrderHistoryResp;
        import com.group04.tgdd.mapper.OrderMapper;
        import com.group04.tgdd.model.Orders;
        import org.springframework.stereotype.Service;
        import org.springframework.util.CollectionUtils;

        import java.util.ArrayList;
        import java.util.List;
        @Service
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderHistoryResp toOrderHistory(Orders orders) {
        OrderHistoryResp orderHistoryResp = new OrderHistoryResp();
        if (orders != null) {
            if (orders.getId() != null) {
                orderHistoryResp.setOrderId(orders.getId());
            }
            if (orders.getOrderUser() != null) {
                orderHistoryResp.setUserName(orders.getOrderUser().getName());
            }
            if (orders.getDeliveryDate() != null) {
                orderHistoryResp.setDelivery_date(orders.getDeliveryDate());
            }
            if (orders.getState() != null) {
                orderHistoryResp.setState(orders.getState());
            }
            if (orders.getOrderdetail() != null) {
                orderHistoryResp.setTotalPrice(orders.getOrderdetail().getTotalPrice());
                orderHistoryResp.setOrderDate(orders.getCreateTime());
            }
            orderHistoryResp.setPaymendMethod(orders.getOrderdetail().getPayment().getName());
        }
        return orderHistoryResp;
    }

    @Override
    public List<OrderHistoryResp> toOrderHistory(List<Orders> orders) {
        List<OrderHistoryResp> orderHistoryResps = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orders)) {
            for (Orders order : orders) {
                orderHistoryResps.add(this.toOrderHistory(order));
            }
        }
        return orderHistoryResps;
    }
}
