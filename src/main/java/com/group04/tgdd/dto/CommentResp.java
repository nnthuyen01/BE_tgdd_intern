package com.group04.tgdd.dto;

import com.group04.tgdd.model.Comment;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentResp {
    private Long id;
    private String content;
    private Date create_time;
    private List<Comment> listSubComment;
    private double rate;
    private UserResp user;
    private ProductDetailResp product;
}
