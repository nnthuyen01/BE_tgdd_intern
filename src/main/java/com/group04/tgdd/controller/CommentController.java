package com.group04.tgdd.controller;

import com.group04.tgdd.dto.CommentReq;
import com.group04.tgdd.dto.CommentResp;
import com.group04.tgdd.dto.QuestionResp;
import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.model.Comment;
import com.group04.tgdd.service.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class CommentController {
    private final CommentService commentService;
    private final SubCommentService subCommentService;
    // Get All Comment of Product
    @GetMapping("/subcomment/{id}")
    public ResponseEntity<?> findById(@PathVariable final Long id) {
        Comment subComment = subCommentService.findById(id);
        if (subComment != null) {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", subComment));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false, "Not found", null));

    }
    @GetMapping("/subcomments/{commentParentId}")
    public ResponseEntity<?> findSubCommetByParentCommentId(@PathVariable final Long commentParentId) {
        List<Comment> subComment = subCommentService.findSubCommetByParentCommentId(commentParentId);
        if (subComment != null) {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", subComment));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false, "Not found", null));

    }
    @PostMapping("comment/subcomment")
    public ResponseEntity<?> createSubComment(@RequestBody CommentReq subCommentReq) {
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", subCommentService.createSubComment(subCommentReq)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }
    }
    @GetMapping("/comment")
    public ResponseEntity<?> findAll(@RequestParam Long productId,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",commentService.getAllComment(productId,page,size)));
    }
    @GetMapping("/comment/ratecomment")
    public ResponseEntity<?> findAllRateComment(@RequestParam Long productId,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",commentService.getAllRateComment(productId,page,size)));
    }
    @GetMapping("/comment/nonratecomment")
    public ResponseEntity<?> findAllNonRateComment(@RequestParam Long productId,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",commentService.getAllNonRateComment(productId,page,size)));
    }
    @GetMapping("/comment/{commentid}")
    public ResponseEntity<?> findAll(@PathVariable Long commentid){
        CommentResp question = commentService.findById(commentid);
        if (question!=null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",question));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Comment ID not exits",null));
    }
    // Add Comment
    @PostMapping("/comment")
    public ResponseEntity<?> saveComment(@RequestBody CommentReq commentReq){
        Comment commentSave =  commentService.saveComment(commentReq);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",commentSave));
    }

    //Update Comment
    // require: id, content, rate.
    @PutMapping("/comment")
    public ResponseEntity<?> updateComment(@RequestBody CommentReq CommentReq){
        Comment commentUpdate = commentService.updateComment(CommentReq);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",commentUpdate));
    }
    @PutMapping(value = "comment/subcomment/{id}")
    public ResponseEntity<?> updateSubComment(@PathVariable final Long id, @RequestBody CommentReq subCommentReq) {
        Comment subCommentUpdate = subCommentService.updateSubComment(id, subCommentReq);
        if (subCommentUpdate != null) {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", subCommentUpdate));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false, "subComment ID not exits", null));
    }

    //Delete Comment
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id){
        Comment comment = subCommentService.findById(id);
        if (subCommentService.deleteComment(comment)) {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false, "SubComment ID not exits", null));
    }
    @GetMapping("/comment/countRate")
    public ResponseEntity<?> countRate(@Parameter Long ProductId){
        List<Double> temp = commentService.countRateProduct(ProductId);
        List<Integer> result = new ArrayList<>();
        for(double i : temp){
            int j = (int)i;
            result.add(j);
        }
        return ResponseEntity.ok(new ResponseDTO(true,"Success",result));
    }
}
