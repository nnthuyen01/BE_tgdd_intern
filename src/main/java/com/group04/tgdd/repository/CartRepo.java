package com.group04.tgdd.repository;

import com.group04.tgdd.model.Cart;
import com.group04.tgdd.model.CartID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<Cart, CartID> {

    /**
     * findByIdUser_id -> directive "findBy" field "CartID.user_id"
     * findByIdProduct_id -> directive "findBy" field "CartID.product_id"
     * Search Spring Boot Composite Key for more references
     */
    List<Cart> findByCartID_UserId(Long user_id);

    List<Cart> findByCartID_ProductColorId(Long productColorId);

    @Modifying
    @Transactional
    Integer deleteByCartID(CartID id);

    @Query("select c from Cart c where c.cartID.userId = :userId and c.cartID.productColorId = :productColorId")
    Cart findCartByUserIdAndProductColorId(Long userId,Long productColorId);

    void deleteAllByCartID_UserId(Long userId);
}
