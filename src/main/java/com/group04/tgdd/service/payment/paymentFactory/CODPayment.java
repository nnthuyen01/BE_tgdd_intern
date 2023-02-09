package com.group04.tgdd.service.payment.paymentFactory;

import com.cloudinary.api.exceptions.ApiException;
import com.group04.tgdd.exception.AppException;
import com.group04.tgdd.exception.NotFoundException;
import com.group04.tgdd.exception.PaymentException;
import com.group04.tgdd.model.Orders;
import com.group04.tgdd.repository.*;
import com.group04.tgdd.service.PaypalService;
import com.group04.tgdd.service.UserService;
import com.group04.tgdd.service.email.EmailSenderService;
import com.group04.tgdd.service.payment.async.PaymentAsync;
import com.group04.tgdd.utils.constant.StateOrderConstant;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@Transactional
public class CODPayment extends PaymentProcess {


    public CODPayment(OrderRepo orderRepo, UsersRepo usersRepo, ProductColorRepo productColorRepo, PaymentRepo paymentRepo, UserService userService, PaypalService paypalService, ModelMapper modelMapper, PaymentAsync paymentAsync, EmailSenderService emailSenderService, OrderItemRepo orderItemRepo) {
        super(orderRepo, usersRepo, productColorRepo, paymentRepo, userService, paypalService, modelMapper, paymentAsync, emailSenderService, orderItemRepo);
    }

    @Override
    public Object createOrder(HttpServletRequest request, Orders orders) {
        orders.setState(StateOrderConstant.Pending);
        orderRepo.save(orders);
        sendEmailOrder(orders);
        return orders.getId().toString();
    }

    @Override
    public Object confirmPaymentAndSendMail(Long orderId, String... param) {
        Orders orders = orderRepo.findById(orderId).orElseThrow(()->new NotFoundException("Order ID not found"));
        if (orders.getState().equals(StateOrderConstant.Confirmed))
            throw new PaymentException(400,"Order has been confirmed");
        CompletableFuture<Boolean> check = paymentAsync.asyncCheckAndUpdateQuantityProduct(orders.getOrderItems());
        orders.setState(StateOrderConstant.Confirmed);

        check.exceptionally(ex ->{
            throw new PaymentException(409,"Payment failed");
        });
        sendEmailOrder(orders);
        return orders.getId();
    }

    @Override
    public void cancelOrder(Object orderId) {
        Orders orders = orderRepo.findById((Long) orderId).orElseThrow(()->new NotFoundException("Order ID not found"));
        if (orders.getState().equals(StateOrderConstant.Cancel))
            throw new PaymentException(400,"Order has been canceled");
        if (orders.getState().equals(StateOrderConstant.Confirmed)){
            returnProduct(orders.getOrderItems());
        }
        orders.setState(StateOrderConstant.Cancel);
        orderRepo.save(orders);
    }
}
