package com.group04.tgdd.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubCommentReq implements Serializable {
    private Long userId;
    private Long commentId;
    private String content;
}
