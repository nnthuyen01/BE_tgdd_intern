package com.group04.tgdd.dto;

import com.group04.tgdd.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class OrderDetailResp {
    private Orders orders;
    private List<Items> items;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Items{
        ProductColor productColor;
        ProductOption productOption;
        String itemName;
        Product product;
        int quantity;
        BigDecimal totalPrice;
    }

}


