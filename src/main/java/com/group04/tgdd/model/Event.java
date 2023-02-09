package com.group04.tgdd.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String banner;
    private String color;
    private String award;
    @ManyToMany()
    @JoinTable(name = "event_product",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn (name = "product_id"))
    @JsonManagedReference
    private List<Product> productList;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime expireTime;

    @PreRemove
    private void removeGroupsFromUsers() {
        for (Product p : productList) {
            p.getEventList().remove(this);
        }
    }
}
