package com.group04.tgdd.service.payment;

import com.group04.tgdd.dto.OrderReq;
import com.group04.tgdd.exception.NotFoundException;
import com.group04.tgdd.model.Orders;
import com.group04.tgdd.repository.OrderRepo;
import com.group04.tgdd.service.payment.paymentFactory.PaymentProcess;
import com.group04.tgdd.service.payment.paymentFactory.PaymentProcessFactory;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class OrderPaymentSrvIplm implements OrderPaymentService{
    private final PaymentProcessFactory paymentProcessFactory;

    @Override
    public Object order(OrderReq orderReq, HttpServletRequest request) throws UnsupportedEncodingException {
        PaymentProcess paymentProcess = paymentProcessFactory.getPaymentMethod(orderReq.getPaymentMethod());
        Orders orders = (Orders) paymentProcess.initOrder(orderReq,request);
        return paymentProcess.createOrder(request,orders);
    }

    @Override
    public Object successOrder(Long orderId, String... param) throws MessagingException, TemplateException, IOException {

        PaymentProcess paymentProcess = paymentProcessFactory.getPaymentMethod(param[0]);
        Long orderIdSuccess = (Long) paymentProcess.confirmPaymentAndSendMail(orderId,param);
        return orderIdSuccess;
    }

    @Override
    public void cancelOrder(Object orderId,String paymentMethod) {
        PaymentProcess paymentProcess = paymentProcessFactory.getPaymentMethod(paymentMethod);
        paymentProcess.cancelOrder(orderId);


    }
}
