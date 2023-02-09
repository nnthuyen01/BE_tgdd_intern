package com.group04.tgdd.controller;


import com.group04.tgdd.dto.OrderReq;
import com.group04.tgdd.dto.ResponseDTO;


import com.group04.tgdd.service.payment.OrderPaymentService;
import com.group04.tgdd.service.payment.paymentFactory.PaymentProcess;
import com.group04.tgdd.service.payment.paymentFactory.PaymentProcessFactory;
import com.group04.tgdd.utils.Utils;
import com.group04.tgdd.utils.constant.PaymentConstant;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.Constants;
import com.paypal.base.rest.PayPalRESTException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
@Slf4j
public class OrderPaymentController {

    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";
    private final OrderPaymentService orderPaymentService;

    @Value("${redirect.order}")
    public String redirectUrl;

    @PostMapping("api/order")
    public ResponseEntity<?> createOrder(HttpServletRequest request,
                                         @Valid @RequestBody OrderReq orderReq) throws UnsupportedEncodingException {

        String result = (String) orderPaymentService.order(orderReq, request);
        return ResponseEntity.ok(new ResponseDTO(true, "Success", result));
    }

    @SneakyThrows
    @Hidden
    @GetMapping(URL_PAYPAL_CANCEL)
    public void cancelPay(@RequestParam("token") String token,HttpServletResponse response) {
        orderPaymentService.cancelOrder(token, PaymentConstant.PAYPAL);
        String targerUrl = targerUrl(redirectUrl, "error", null);
        response.sendRedirect(targerUrl);
    }

    @SneakyThrows
    @Hidden
    @GetMapping(URL_PAYPAL_SUCCESS)
    public void successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletResponse response) {
        Long orderId = (Long) orderPaymentService.successOrder(null, PaymentConstant.PAYPAL, paymentId, payerId);

        String targerUrl = targerUrl(redirectUrl, "orderId", orderId.toString());
        response.sendRedirect(targerUrl);
    }

    @SneakyThrows
    @Hidden
    @GetMapping("payment/vnpay/{orderId}")
    public void resultVnPay(@RequestParam("vnp_ResponseCode") String responseCode,
                            @PathVariable Long orderId,
                            HttpServletResponse response) {
        if (responseCode.equals("00")) {
            orderPaymentService.successOrder(orderId, PaymentConstant.VNPAY);
            String targerUrl = targerUrl(redirectUrl, "orderId", orderId.toString());
            response.sendRedirect(targerUrl);
        } else {
            orderPaymentService.cancelOrder(orderId, PaymentConstant.VNPAY);
            String targerUrl = targerUrl(redirectUrl, "error", orderId.toString());
            response.sendRedirect(targerUrl);
        }
    }

    @SneakyThrows
    @PutMapping("/api/admin/cod/confirm/{orderId}")
    public ResponseEntity<?> confirmOrder(@PathVariable Long orderId) {
        orderPaymentService.successOrder(orderId, PaymentConstant.COD);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true, "Success", null));
    }

    @PutMapping("/api/cod/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        orderPaymentService.cancelOrder(orderId, PaymentConstant.COD);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true, "Success", null));
    }

    public String targerUrl(String redirectUrl, String param, String content) {
        return UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam(param, content)
                .build().toUriString();
    }
}
