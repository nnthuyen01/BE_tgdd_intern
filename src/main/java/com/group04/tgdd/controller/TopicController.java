package com.group04.tgdd.controller;

import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.model.Topic;
import com.group04.tgdd.service.TopicService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class TopicController {
    private final TopicService topicService;

    // Get All Topics
    @GetMapping("/topics")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",topicService.findAll()));
    }

    // Get Topic by ID
    @GetMapping("/topic/{topicID}")
    public ResponseEntity<?> findById(@PathVariable Long topicID){
        Topic topic = topicService.findById(topicID);
        if (topic!=null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",topic));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Topic id does not exist",null));
    }

    // Add topic
    @PostMapping("/topic/new/")
    public ResponseEntity<?> saveTopic(@RequestBody Topic topic){
        Topic savedTopic =  topicService.save(topic);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",savedTopic));
    }

    // Update Topic
    @PutMapping("/topic/update")
    public ResponseEntity<?> updateTopic(@RequestBody Topic topic){
        Topic updatedTopic = topicService.updateTopic(topic);
        if (updatedTopic!= null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",updatedTopic));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Payment ID not exits",null));
    }

    //Delete Topic
    @DeleteMapping("/topic/delete/{topicId}")
    public ResponseEntity<?> deletePayment(@PathVariable Long topicId){
        boolean check = topicService.deleteTopic(topicId);
        if (check){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false,"Topic ID not exits",null));
    }
}
