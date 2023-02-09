package com.group04.tgdd.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class OrderHistoryResp implements Serializable {
    private Long orderId;
    private String userName;
    private Date delivery_date;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;
    private String state;
    private String paymendMethod;
}


