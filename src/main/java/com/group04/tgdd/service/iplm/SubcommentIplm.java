package com.group04.tgdd.service.iplm;

import com.group04.tgdd.dto.CommentReq;
import com.group04.tgdd.model.Comment;
import com.group04.tgdd.model.Users;
import com.group04.tgdd.repository.CommentRepo;
import com.group04.tgdd.service.CommentService;
import com.group04.tgdd.service.SubCommentService;
import com.group04.tgdd.service.UserService;

import com.group04.tgdd.utils.Utils;
import com.group04.tgdd.utils.constant.RoleConstant;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SubcommentIplm implements SubCommentService {

    private final UserService userService;
    private final CommentService commentService;
    private final CommentRepo commentRepo;

    //find comment by id
    @Override
    public Comment findById(Long id) {
        Optional<Comment> subComment = commentRepo.findById(id);
        return subComment.orElse(null);
    }

    //find all subcomment
    @Override
    public List<Comment> findAll() {
        return  commentRepo.findAllSubcomment();
    }
    //create subcomment
    @Override
    public Comment createSubComment(CommentReq subCommentReq) {
        if (subCommentReq == null) {
            throw new IllegalArgumentException("SubCommentReq can not allow null");
        }

        if (subCommentReq.getParentCommentId() == null) {
            throw new IllegalArgumentException("Comment id can not allow null");
        }
        if (subCommentReq.getContent() == null) {
            throw new IllegalArgumentException("Content can not allow null");
        }
        Users user = userService.findById(Utils.getIdCurrentUser());
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        Comment comment = commentService.findBy_Id(subCommentReq.getParentCommentId());
        if (comment == null) {
            throw new NotFoundException("Comment not found");
        }
        Comment subComment = Comment.builder()
                .users(user)
                .parentComment(comment)
                .createTime(new Date())
                .numReply(0L)
                .content(subCommentReq.getContent())
                .isAdminReply(user.getRole().equals(RoleConstant.ROLE_ADMIN))
                .build();
        return commentRepo.save(subComment);
    }
    //update comment
    @Override
    public Comment updateSubComment(final Long id, CommentReq subCommentReq) {
        Comment subCommentUpdate = findById(id);
        if (subCommentUpdate == null) {
            return null;
        }
        if (!StringUtils.isEmpty(subCommentReq.getContent())) {
            subCommentUpdate.setContent(subCommentReq.getContent());
        }

        return commentRepo.save(subCommentUpdate);
    }

    //delete comment
    @Override
    public boolean deleteComment(Comment subCommentId) {
        //delete parent comment
        if(subCommentId.getParentComment() == null)
        {
            deleteParentComment(subCommentId.getId());
            return true;
        }
        //delete sub comment
        else if(subCommentId.getParentComment() != null){
            deleteSubComment(subCommentId.getId());
            return true;
        }
        return false;
    }
    //delete parent comment
    @Override
    public boolean deleteParentComment(final Long CommentId) {
        boolean check = commentRepo.existsById(CommentId);
        if (check) {
            commentRepo.deleteById(CommentId);
            //delete list sub comment
            commentRepo.deleteSubListCommentById(CommentId);
            return true;
        }
        return false;
    }
    //delete sub parent comment
    @Override
    public boolean deleteSubComment(final Long subCommentId) {
        boolean check = commentRepo.existsSubCommentById(subCommentId);
        if (check) {
            commentRepo.deleteSubCommentById(subCommentId);
            return true;
        }
        return false;
    }
    @Override
    public List<Comment> findSubCommetByParentCommentId(Long commentParentId) {
        Comment comment = commentRepo.getReferenceById(commentParentId);
        List<Comment> subComments = commentRepo.findAllSubcommentByComment(comment);
        return subComments;
    }
}
