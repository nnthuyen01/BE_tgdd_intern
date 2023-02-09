package com.group04.tgdd.dto;

import lombok.Data;

@Data
public class ProductColorReq {
    private Long id;
    private Long productOptionId;
    private Long colorId;
    private int quantity;
}
