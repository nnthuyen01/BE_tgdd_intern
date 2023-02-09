package com.group04.tgdd.model;



import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String categoryName;

    @JsonIgnore
    @ManyToOne()
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory",cascade = CascadeType.REMOVE)
    List<Category> subcategories;

    @ManyToMany(mappedBy = "categoryList")
    @JsonBackReference
    List<Voucher> voucherList;
}
