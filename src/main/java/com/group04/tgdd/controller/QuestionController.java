package com.group04.tgdd.controller;

import com.group04.tgdd.dto.QuestionReq;
import com.group04.tgdd.dto.QuestionResp;
import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.model.Question;
import com.group04.tgdd.service.QuestionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class QuestionController {
    private final QuestionService questionService;

    // Get All question
    @GetMapping("/question")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",questionService.findAll()));
    }

    // Get question by ID
    @GetMapping("/question/{questionId}")
    public ResponseEntity<?> findById(@PathVariable Long questionId){
        QuestionResp question = questionService.findById(questionId);
        if (question!=null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",question));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Question ID not exits",null));
    }

    // Add question
    @PostMapping("/question")
    public ResponseEntity<?> saveQuestion(@RequestBody QuestionReq questionReq){
        Question questionSave =  questionService.save(questionReq);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",questionSave));
    }


    //Delete question
    @DeleteMapping("/question/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id){
        boolean check = questionService.deleteQuestion(id);
        if (check){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false,"Question ID not exits",null));
    }
}
