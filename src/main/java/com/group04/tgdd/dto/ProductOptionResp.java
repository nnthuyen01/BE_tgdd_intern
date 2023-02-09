package com.group04.tgdd.dto;

import com.group04.tgdd.model.ProductColor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductOptionResp {
    private Long id;
    private String optionName;
    private BigDecimal price;
    private BigDecimal marketPrice;
    private int promotion;
    List<ProductColor> productColors;
}
