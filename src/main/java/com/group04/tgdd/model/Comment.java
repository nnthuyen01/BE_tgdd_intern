package com.group04.tgdd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;
    private double rate;
    private Date createTime;
    private Long numReply;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Product products;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;


    @OneToMany(mappedBy = "parentComment",cascade = CascadeType.REMOVE)
    private List<Comment> subComments;

    @JsonIgnore
    @ManyToOne()
    private Comment parentComment;

    private Boolean isAdminReply;
    
}
