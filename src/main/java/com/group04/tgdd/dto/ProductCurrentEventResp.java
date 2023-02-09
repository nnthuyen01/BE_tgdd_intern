package com.group04.tgdd.dto;

import com.group04.tgdd.model.Category;
import com.group04.tgdd.model.Manufacturer;
import com.group04.tgdd.model.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCurrentEventResp {
    private Long id;
    private String name;
    @Lob
    private String description;
    private String video;
    private double rate=0;
    private int countRate=0;
    private boolean enable = true;
    private Category category;
    private Category subcategory;
    private Manufacturer manufacturer;
    private List<ProductImage> productImages;
    private Double installment;
    private BigDecimal marketPrice;
    private BigDecimal price;
    private double promotion;
}
