package com.group04.tgdd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String urlImage;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Color color;
}
