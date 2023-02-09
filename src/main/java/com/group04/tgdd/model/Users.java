package com.group04.tgdd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = @Index(name = "index1",columnList = "email"))
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Transactional
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String gender;
    private String phone;

    @JsonIgnore
    private String password;
    @Column(nullable = false)
    private String name;
    private String role;
    private Boolean enable = false;

    @Lob
    private String avatar;
    private String provider;

    @JsonIgnore
    @OneToMany(mappedBy = "orderUser",fetch = FetchType.LAZY)
    private List<Orders> orders;

    @OneToMany(mappedBy = "users",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Address> addresses= new ArrayList<>();
}
