package com.group04.tgdd.controller;

import com.group04.tgdd.dto.OrderReq;
import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.mapper.OrderMapper;
import com.group04.tgdd.model.Orders;
import com.group04.tgdd.service.OrderService;

import com.group04.tgdd.utils.constant.PaymentConstant;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping("/admin/all-order")
    private ResponseEntity<?> getAllOrder(@RequestParam int page,
                                          @RequestParam int size) {
        return ResponseEntity.ok(new ResponseDTO(true, "Success", orderService.findAllOrder(page, size)));
    }
    @GetMapping("/order/{orderId}")
    private ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(new ResponseDTO(true, "Success", orderService.findByIdOrder(orderId)));
    }

    @GetMapping("/admin/order/search-status")
    public ResponseEntity<?> searchOrder(@RequestParam final String status,
                                         @RequestParam(defaultValue = "1") final Integer page,
                                         @RequestParam(defaultValue = "10") final Integer size) {
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success",
                    orderMapper.toOrderHistory(orderService.searchOrder(status, page, size))));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(),
                    null));
        }
    }

    @GetMapping("/order/history/{userId}")
    public ResponseEntity<?> getOrderHistoryOfUser(@PathVariable final Long userId,
                                                   @RequestParam(defaultValue = "1") final Integer page,
                                                   @RequestParam(defaultValue = "10") final Integer size) {
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success",
                    orderMapper.toOrderHistory(orderService.getOrderHistory(userId, page, size))));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(),
                    null));
        }
    }

    //Count order by day month year
    @GetMapping("/admin/order/count-order")
    public ResponseEntity<?> getNumberOrderByDay(@RequestParam(defaultValue = "0") int day,
                                                 @RequestParam(defaultValue = "0") int month,
                                                 @RequestParam(defaultValue = "0") int year) {
        return ResponseEntity.ok(new ResponseDTO(true, "Success",
                orderService.countOrderByDay(day, month, year)));

    }

    @GetMapping("/admin/order/count-revenue")
    public ResponseEntity<?> getRevenueByDay(@RequestParam(defaultValue = "0") int day,
                                             @RequestParam(defaultValue = "0") int month,
                                             @RequestParam(defaultValue = "0") int year) {
        return ResponseEntity.ok(new ResponseDTO(true, "Success",
                orderService.countRevenueByDay(day, month, year)));

    }

    @PutMapping("/admin/order/update-status/{id}")
    public ResponseEntity<?> updateStatusOrders(@PathVariable Long id,
                                                @RequestParam String status){
        Orders orderUpdate = orderService.updateStatusOrder(id, status);
        if (orderUpdate!= null){
            return ResponseEntity.ok(new ResponseDTO(true,"Success", orderUpdate));
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false,"ID order not FOUND",null));
    }
}
