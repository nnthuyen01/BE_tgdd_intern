package com.group04.tgdd.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String deliveryAddress;
    private String discountCode;
    @Column(nullable = false,columnDefinition = "Decimal(19,0) default '0.00'")
    private BigDecimal totalPrice;
    @Column(nullable = false)
    private Integer quantity;
    private String description;
    private String differentReceiverName;
    private String differentReceiverPhone;

    @OneToOne
    @JsonBackReference
    private Orders orders;

    @ManyToOne
    private Payment payment;
}
