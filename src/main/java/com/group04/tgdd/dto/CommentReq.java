package com.group04.tgdd.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class CommentReq {
    private Long id;
    private Long productId;
    private double rate;
    private String content;
    private Long parentCommentId;
}
