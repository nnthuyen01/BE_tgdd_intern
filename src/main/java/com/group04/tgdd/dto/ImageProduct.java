package com.group04.tgdd.dto;


import com.group04.tgdd.model.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ImageProduct {
    private String color;
    private List<ProductImage> items;
}
