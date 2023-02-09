package com.group04.tgdd.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ProductReq {
    private Long id;
    private String name;
    private String description;
    private String video;
    private Long categoryId;
    private Long subCategoryId;
    private Long manufacturerId;
    private Double installment;
    private List<ProductOpReq> productOptions;

    private List<ProductImageReq> images;

    private List<ProTechReq> techs;
}
