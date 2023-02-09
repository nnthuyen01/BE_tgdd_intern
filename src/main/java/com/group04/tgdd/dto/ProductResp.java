package com.group04.tgdd.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data

public class ProductResp {
    private Long id;
    private String name;
    private String image;
    private BigDecimal marketPrice;
    private BigDecimal price;
    private int promotion;
    private double rate;
    private Double installment;

}
