package com.group04.tgdd.service.iplm;

import com.group04.tgdd.config.PaypalPaymentIntent;
import com.group04.tgdd.config.PaypalPaymentMethod;
import com.group04.tgdd.dto.OrderDetailResp;
import com.group04.tgdd.dto.OrderReq;
import com.group04.tgdd.exception.AppException;
import com.group04.tgdd.model.*;
import com.group04.tgdd.repository.*;
import com.group04.tgdd.service.OrderService;
import com.group04.tgdd.service.PaypalService;
import com.group04.tgdd.service.UserService;

import com.group04.tgdd.utils.MoneyConvert;
import com.group04.tgdd.utils.Utils;
import com.group04.tgdd.utils.VNpayUtils;
import com.group04.tgdd.utils.constant.PaymentConstant;
import com.group04.tgdd.utils.constant.StateOrderConstant;
import com.paypal.api.payments.Links;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderIplm implements OrderService {
    private final OrderRepo orderRepo;
    private final UsersRepo usersRepo;
    private final ProductColorRepo productColorRepo;
    private final PaymentRepo paymentRepo;
    private final UserService userService;
    private final PaypalService paypalService;
    private final ModelMapper modelMapper;
    private Logger log = LoggerFactory.getLogger(getClass());

    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";
    final String not_found_order_id = "Not found Order ID";
    final String sold_out_product = "Sold out Product";

    @Override
    public OrderDetailResp findByIdOrder(Long id) {
        Orders orders = orderRepo.findById(id).orElseThrow(()-> {
            throw new AppException(404, not_found_order_id);
        });
        OrderDetailResp orderDetailResp = new OrderDetailResp();
        orderDetailResp.setOrders(orders);
        orderDetailResp.setItems(new ArrayList<>());
        List<OrderItem> orderItems = orders.getOrderItems();
        orderItems.forEach(orderItem -> {
            OrderDetailResp.Items items = modelMapper.map(orderItem,OrderDetailResp.Items.class);
            items.setProductOption(orderItem.getProductColor().getProductOption());
            items.setProduct(orderItem.getProductColor().getProductOption().getProduct());
            items.setTotalPrice(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            orderDetailResp.getItems().add(items);
        });
        return  orderDetailResp;
    }
    @Override
    public List<Orders> findAllOrder(int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        List<Orders> orders = orderRepo.findAllByOrderByCreateTimeDesc(pageable);
        return orders;
    }


    @Override
    public List<Orders> getOrderHistory(Long userId, Integer page, Integer size) {
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        Users user = userService.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        List<Orders> orders = orderRepo.findAllByOrderUser(user);
        orders.sort((o1, o2) -> Long.compare(o2.getId(), o1.getId()));
        PagedListHolder<Orders> pagedListHolder = new PagedListHolder<>(orders);
        pagedListHolder.setPage(page-1);
        pagedListHolder.setPageSize(size);
        return pagedListHolder.getPageList();
    }

    @Override
    public List<Orders> searchOrder(final String state, Integer page, Integer size) {
        List<Orders> orders = orderRepo.findAll();
        if (!StringUtils.isEmpty(state)) {
            orders = orderRepo.findByState(state);
        }
        orders.sort(((o1, o2) -> Long.compare(o2.getId(), o1.getId())));
        PagedListHolder<Orders> pagedListHolder = new PagedListHolder<>(orders);
        pagedListHolder.setPage(page-1);
        pagedListHolder.setPageSize(size);
        return pagedListHolder.getPageList();
    }


    @Override
    public Long countOrderByDay(int day, int month, int year) {
        if (day==0 && month ==0 && year ==0){
            return orderRepo.count();
        }
        if (!GenericValidator.isDate(year+"-"+month+"-"+day,"yyyy-MM-dd",false))
            throw new AppException(400,"Wrong day");

        LocalDateTime date = LocalDateTime.of(year,month,day,0,0,0);

        return orderRepo.countAllTimeGreaterThanEqual(date);

    }


    @Override
    public BigDecimal countRevenueByDay(int day, int month, int year) {
        if (day==0 && month ==0 && year ==0){
            return orderRepo.totalAllRevenue();
        }
        if (!GenericValidator.isDate(year+"-"+month+"-"+day,"yyyy-MM-dd",false))
            throw new AppException(400,"Wrong day");

        LocalDateTime date = LocalDateTime.of(year,month,day,0,0,0);

        return orderRepo.totalRevenue(date);
    }

    @Override
    public Orders updateStatusOrder(Long orderId, String status) {
        var check = orderRepo.findById(orderId);
        if (!check.isPresent())
            throw new AppException(404, "Product ID not found");

        Orders orderUpdate = check.get();
        orderUpdate.setState(status);
        orderRepo.save(orderUpdate);
        return  orderUpdate;
    }

    @Override
    public Orders save(Orders orders) {
        return orderRepo.save(orders);
    }
}
