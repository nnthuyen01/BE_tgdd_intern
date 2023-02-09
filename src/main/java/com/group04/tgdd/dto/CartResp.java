package com.group04.tgdd.dto;

import com.group04.tgdd.model.CartID;
import com.group04.tgdd.model.Product;
import com.group04.tgdd.model.ProductColor;
import com.group04.tgdd.model.ProductOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
public class CartResp {

    private int quantity;
    private Items item;
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Items{
        ProductColor productColor;
        ProductOption productOption;
        String productName;
        String image;
        BigDecimal marketPrice;
        BigDecimal price;
        int promotion;
    }
}
