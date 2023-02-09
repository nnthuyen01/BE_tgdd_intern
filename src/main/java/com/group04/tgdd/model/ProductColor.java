package com.group04.tgdd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProductColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductOption productOption;

    @ManyToOne
    private Color color;

    @JsonIgnore
    @OneToMany(mappedBy = "productColor",fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @JsonIgnore
    @Version
    private Integer version;

    public void increaseQuantityProduct(int num){
        this.quantity+=num;
    }

}
