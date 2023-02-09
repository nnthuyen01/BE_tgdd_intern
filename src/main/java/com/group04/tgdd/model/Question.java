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
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String content;
    private String image;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Topic topic;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;
}
