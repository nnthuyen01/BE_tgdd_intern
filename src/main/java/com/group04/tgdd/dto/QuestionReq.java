package com.group04.tgdd.dto;

import lombok.Data;

@Data
public class QuestionReq {
    private Long userId;
    private Long topicId;
    private String content;
}
