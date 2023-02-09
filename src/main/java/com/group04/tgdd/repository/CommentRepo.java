package com.group04.tgdd.repository;


import com.group04.tgdd.model.Comment;
import com.group04.tgdd.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepo extends JpaRepository<Comment,Long> {
    int countCommentByProducts(Product product);
    List<Comment> findCommentByProducts(Product product, Pageable pageable);

    @Query("select u from Comment u where u.parentComment is not null and u.parentComment = ?1")
    List<Comment> findAllSubcommentByComment(Comment comment);
    void deleteSubCommentById(Long subCommentId);
    boolean existsSubCommentById(Long subCommentId);
    @Query("select u from Comment u where u.parentComment is not null ")
    List<Comment> findAllSubcomment();

    @Query("SELECT c FROM Comment c where c.products.id = :products_id and c.rate >0")
    List<Comment> findAllRateComment(Long products_id);

    @Query(
            "SELECT c FROM Comment c WHERE c.products = :product and c.rate > 0"
    )
    List<Comment> findRateCommentByProducts(Product product, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.products = :product and c.rate = 0")
    List<Comment> findNonRateCommentByProducts(Product product, Pageable pageable);

    void deleteSubListCommentById(Long subCommentId);
}
