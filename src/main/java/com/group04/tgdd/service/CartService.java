package com.group04.tgdd.service;

import com.group04.tgdd.dto.CartDTO;
import com.group04.tgdd.dto.CartResp;
import com.group04.tgdd.model.Cart;
import com.group04.tgdd.model.CartID;

import java.util.List;

public interface CartService {
    void save(Long productColorId, int quantity);

    List<CartResp> view(Long userID);
    Integer deleteCart(CartID cartID);

    Cart updateCart(CartID cartID, int quantity);

    Cart decrease(CartID cartID);

    Cart increase(CartID cartID);

    void deleteAllCartUser();
}
