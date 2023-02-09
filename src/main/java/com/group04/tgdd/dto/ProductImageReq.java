package com.group04.tgdd.dto;

import lombok.Data;

@Data
public class ProductImageReq {
    private String urlImage;
    private Long productId;
    private Long colorId;
}
