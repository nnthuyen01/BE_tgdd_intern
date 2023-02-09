package com.group04.tgdd.service.payment.paymentFactory;

import com.group04.tgdd.config.PaypalPaymentIntent;
import com.group04.tgdd.config.PaypalPaymentMethod;
import com.group04.tgdd.exception.OutOfStockException;
import com.group04.tgdd.exception.PaymentException;
import com.group04.tgdd.model.Orders;
import com.group04.tgdd.repository.*;
import com.group04.tgdd.service.PaypalService;
import com.group04.tgdd.service.UserService;
import com.group04.tgdd.service.email.EmailSenderService;
import com.group04.tgdd.service.payment.async.PaymentAsync;
import com.group04.tgdd.utils.MoneyConvert;
import com.group04.tgdd.utils.Utils;
import com.group04.tgdd.utils.constant.PaymentConstant;
import com.group04.tgdd.utils.constant.StateOrderConstant;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@Transactional
public class PaypalPayment extends PaymentProcess {


    public PaypalPayment(OrderRepo orderRepo, UsersRepo usersRepo, ProductColorRepo productColorRepo, PaymentRepo paymentRepo, UserService userService, PaypalService paypalService, ModelMapper modelMapper, PaymentAsync paymentAsync, EmailSenderService emailSenderService, OrderItemRepo orderItemRepo) {
        super(orderRepo, usersRepo, productColorRepo, paymentRepo, userService, paypalService, modelMapper, paymentAsync, emailSenderService, orderItemRepo);
    }

    @Override
    public String createOrder(HttpServletRequest request, Orders orders) {
        String cancelUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
        String successUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
        try {
            double exchangeRate = MoneyConvert.getConversionRate("VND","USD");
            com.paypal.api.payments.Payment payment = paypalService.createPayment(
                    orders.getOrderdetail().getTotalPrice().doubleValue()*exchangeRate,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "Order for TGDD",
                    cancelUrl,
                    successUrl);
            String paypalId =payment.getId();
            orders.setPaymentId("paypal"+paypalId);
            orders.setState(StateOrderConstant.UnPaid);

            for(Links links : payment.getLinks()){
                if(links.getRel().equals("approval_url")){
                    String token = links.getHref().substring(links.getHref().lastIndexOf("=")+1);
                    orders.setPaypalToken("paypal"+token);
                    orderRepo.save(orders);
                    return links.getHref();
                }
            }

        } catch (PayPalRESTException | IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /***
     *
     * @param orderId
     * @param param
     * param[0] is payment method
     * param[1] is paymentId
     * param[2] is payerId
     *
     */
    @SneakyThrows
    @Override
    @Transactional()
    public Object confirmPaymentAndSendMail(Long orderId, String... param) {
        Orders orders = orderRepo.findAllByPaymentId(param[0]+param[1]);
        CompletableFuture<Boolean> check = paymentAsync.asyncCheckAndUpdateQuantityProduct(orders.getOrderItems());
        orders.setState(StateOrderConstant.Paid);

        check.exceptionally(ex -> {
            orders.setState(StateOrderConstant.Cancel);
            orderRepo.save(orders);
            throw new PaymentException(409,"Out of stock");
        });
        if (check.get()) {
            try {
                Payment paymentApp = paypalService.executePayment(param[1], param[2]);
                if (paymentApp.getState().equals("approved")) {
                    sendEmailOrder(orders);
                    return orders.getId();
                }
            } catch (PayPalRESTException e) {
                throw new PaymentException(409, e.getMessage());
            }
        }
        return null;
    }

    @Override
    public void cancelOrder(Object orderId) {
        String token = (String) orderId;
        Orders orders = orderRepo.findOrdersByPaypalToken(PaymentConstant.PAYPAL+token);
        orders.setState(StateOrderConstant.Cancel);
    }


}
