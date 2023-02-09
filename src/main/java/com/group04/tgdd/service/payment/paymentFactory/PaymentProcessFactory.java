package com.group04.tgdd.service.payment.paymentFactory;

import com.group04.tgdd.exception.NotFoundException;
import com.group04.tgdd.utils.constant.PaymentConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PaymentProcessFactory {
    @Autowired
    private  ApplicationContext context;

    final String message = "Not found payment method.";

    public PaymentProcess getPaymentMethod(String methodName){
        switch (methodName){
            case PaymentConstant.PAYPAL:
                return context.getBean(PaypalPayment.class);
            case PaymentConstant.VNPAY:
                return context.getBean(VNpayPayment.class);
            case PaymentConstant.COD:
                return context.getBean(CODPayment.class);
            default:
                throw new NotFoundException(message);
        }
    }
}
