package com.group04.tgdd.service;

import com.group04.tgdd.dto.CommentReq;
import com.group04.tgdd.dto.CommentResp;
import com.group04.tgdd.model.Comment;

import java.util.ArrayList;
import java.util.List;

public interface CommentService {
    Comment findBy_Id(Long id);

    CommentResp findById(Long id);

    List<Comment> getAllComment(Long productId, int page, int size);

    List<Comment> getAllRateComment(Long productId, int page, int size);
    List<Comment> getAllNonRateComment(Long productId, int page, int size);
    Comment saveComment(CommentReq commentReq);

    Comment updateComment(CommentReq comment);

    boolean deleteComment(Long id);

    List<Double> countRateProduct(Long id);

}
