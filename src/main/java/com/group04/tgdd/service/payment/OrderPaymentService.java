package com.group04.tgdd.service.payment;

import com.group04.tgdd.dto.OrderReq;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface OrderPaymentService {
    Object order(OrderReq orderReq, HttpServletRequest request) throws UnsupportedEncodingException;
    Object successOrder (Long orderId, String... param) throws MessagingException, TemplateException, IOException;
    void cancelOrder(Object orderId,String paymentMethod);
}
