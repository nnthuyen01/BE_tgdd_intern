package com.group04.tgdd.dto;

import lombok.Data;
import org.springframework.context.annotation.Description;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductOpReq {
    private Long id;
    private Long productId;
    private String productOptionName;
    private BigDecimal price;
    @NotNull(message = "Phần trăm giảm giá")
    private Integer promotion;

    private List<ProductColorReq> colors;
}
