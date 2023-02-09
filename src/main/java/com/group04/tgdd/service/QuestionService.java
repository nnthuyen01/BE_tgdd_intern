package com.group04.tgdd.service;

import com.group04.tgdd.dto.QuestionReq;
import com.group04.tgdd.dto.QuestionResp;
import com.group04.tgdd.model.Question;

import java.util.List;

public interface QuestionService {
        QuestionResp findById(Long id);
        List<Question> findAll();
        Question save(QuestionReq questionReq);
        Boolean deleteQuestion(Long questionId);
}
