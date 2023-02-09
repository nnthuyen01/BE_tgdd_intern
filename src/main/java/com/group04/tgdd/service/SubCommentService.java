package com.group04.tgdd.service;

import com.group04.tgdd.dto.CommentReq;
import com.group04.tgdd.model.Comment;

import java.util.List;

public interface SubCommentService {

    Comment findById(final Long id);

    List<Comment> findAll();


    Comment createSubComment(CommentReq subCommentReq);

    Comment updateSubComment(final Long id, CommentReq subCommentReq);


    boolean deleteComment(Comment subCommentId);

    boolean deleteParentComment(Long subCommentId);

    boolean deleteSubComment(Long subCommentId);


    List<Comment> findSubCommetByParentCommentId(Long commentParentId);
}
