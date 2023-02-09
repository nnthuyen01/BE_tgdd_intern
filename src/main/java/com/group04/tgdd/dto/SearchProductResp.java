package com.group04.tgdd.dto;

import com.group04.tgdd.model.ProductOption;
import lombok.Data;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SearchProductResp {
    private Long id;
    private String name;
    private String image;
    private boolean enable;
    private BigDecimal marketPrice;
    private BigDecimal price;
    private int promotion;
    private ProductOption option;
    private double rate;
    private int countRate;
    private Double installment;
    private List<DetailSpecs> detailSpecs;

    public double getRate() {
        return Math.round(rate * 10.0) / 10.0;
    }

}
