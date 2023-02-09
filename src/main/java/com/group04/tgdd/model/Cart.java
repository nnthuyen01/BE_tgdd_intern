package com.group04.tgdd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Cart{

    @EmbeddedId
    private CartID cartID;

    private int quantity;

//    @Column(nullable = false,columnDefinition = "Decimal(19,0) default '0.00'")
//    private BigDecimal price;


}
