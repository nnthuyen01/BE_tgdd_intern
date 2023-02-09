package com.group04.tgdd.dto;

import com.group04.tgdd.model.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductDetailResp {
    private Long id;
    private String name;
    private String description;
    private double rate;
    private boolean enable;
    private String video;
    private int countRate;
    private Category category;
    private Category subcategory;
    private Manufacturer manufacturer;
    private List<ProductOptionResp> productOptions;
    private List<ImageProduct> images;
    private List<DetailSpecs> detailSpecs;
    private List<Event> events;
    private Double installment;

    public double getRate() {
        return Math.round(rate * 10.0) / 10.0;
    }
    public Category getCategory() {
        category.setSubcategories(null);
        return category;
    }

    public Category getSubcategory() {
        if (subcategory!=null) {
            subcategory.setSubcategories(null);
        }
        return subcategory;
    }
}
