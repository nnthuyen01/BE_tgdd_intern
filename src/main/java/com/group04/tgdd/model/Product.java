package com.group04.tgdd.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.awt.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Lob
    private String description;

    private String video;

    private double rate=0;

    private int countRate=0;

    private boolean enable = true;

    private Double installment;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category subcategory;

    @ManyToOne(fetch = FetchType.LAZY)
    private Manufacturer manufacturer;


    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<ProductImage> productImages;
    @JsonIgnore
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<ProductTechnical> productTechnicals;

    @JsonIgnore
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<ProductOption> productOptions;

    public void increaseCountRate(){
        this.countRate++;
    }

    
    @ManyToMany(mappedBy = "productList")
    @JsonBackReference
    private List<Event> eventList;

}
