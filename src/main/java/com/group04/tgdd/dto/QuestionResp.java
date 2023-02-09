package com.group04.tgdd.dto;

import com.group04.tgdd.model.Topic;
import lombok.Data;

@Data
public class QuestionResp {
    private Long id;
    private String content;
    private String image;
    private Topic topic;
    private UserResp user;
}
