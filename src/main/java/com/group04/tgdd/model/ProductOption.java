package com.group04.tgdd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProductOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String optionName;
    @Column(nullable = false,columnDefinition = "Decimal(19,0) default '0.00'")
    private BigDecimal price;
    
    private int promotion = 0;

    @OneToMany(mappedBy = "productOption", cascade = CascadeType.REMOVE)
    List<ProductColor> productColors;

    @JsonIgnore
    @ManyToOne
    private Product product;
}
