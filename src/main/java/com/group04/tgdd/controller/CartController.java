package com.group04.tgdd.controller;

import com.group04.tgdd.dto.CartDTO;
import com.group04.tgdd.dto.CartResp;
import com.group04.tgdd.dto.ProductReq;
import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.model.Cart;
import com.group04.tgdd.model.CartID;
import com.group04.tgdd.model.Color;
import com.group04.tgdd.model.Product;
import com.group04.tgdd.service.CartService;
import com.group04.tgdd.service.UserService;
import com.group04.tgdd.utils.Utils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class CartController {

    private final CartService cartService;

    @GetMapping("/cart")
    private ResponseEntity<?> getCart(){
        Long userId = Utils.getIdCurrentUser();
        List<CartResp> cartList = cartService.view(userId);
        if(cartList != null){
            return ResponseEntity.ok(new ResponseDTO(true,"Success", cartList));
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(false,"No Item!",null));
        }
    }
    @PostMapping("/cart/add")
    private ResponseEntity<?> addProduct(@RequestParam Long productColorId,
                                         @RequestParam int quantity){

        cartService.save(productColorId,quantity);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }


    @DeleteMapping("/cart/remove/")
    public ResponseEntity<?> removeCartItem(@RequestParam Long productColorId){

        CartID cartID = getCartId(productColorId);

        Integer check = cartService.deleteCart(cartID);
        if (check >= 1){
            return ResponseEntity.ok(new ResponseDTO(true,check.toString() + " rows affected",check));
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false,"Cart Product ID = " + cartID.getProductColorId() +
                        " with User ID = " + cartID.getUserId() +" not exits ",cartID));
    }
    @PutMapping("/cart/update")
    public ResponseEntity<?> updateQuantity(@RequestParam Long productColorId, @RequestParam int quantity){

        CartID cartID = getCartId(productColorId);
        Cart updatedCart = cartService.updateCart(cartID, quantity);
        if(quantity < 1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(false,"Quantity Must Greater Than 0",null));
        }
        if(quantity > 99){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(false,"Quantity Must Lower Than 100",null));
        }
        else {
            if (updatedCart != null)
                return ResponseEntity.ok(new ResponseDTO(true,"Success", updatedCart));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(false,"Cart Product ID = " + cartID.getProductColorId() +
                                " with User ID = " + cartID.getUserId() +" not exits ",null));
        }
    }
    @PutMapping("/cart/increase/")
    public ResponseEntity<?> increaseQuantity(@RequestParam Long productColorId){

        CartID cartID = getCartId(productColorId);
        Cart updatedCart = cartService.increase(cartID);
        if (updatedCart != null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success", updatedCart));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Cart Product ID = " + cartID.getProductColorId() +
                            " with User ID = " + cartID.getUserId() +" not exits ",null));
    }
    @PutMapping("/cart/decrease/")
    public ResponseEntity<?> decreaseQuantity(@RequestParam Long productColorId){

        CartID cartID = getCartId(productColorId);
        Cart updatedCart = cartService.decrease(cartID);
        if (updatedCart != null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success", updatedCart));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Cart Product ID = " + cartID.getProductColorId() +
                            " with User ID = " + cartID.getUserId() +" not exits ",null));
    }
    @DeleteMapping("/cart")
    public ResponseEntity<?> deleteAllCartUser(){
        cartService.deleteAllCartUser();
        return ResponseEntity.ok(new ResponseDTO(true,"Success", null));
    }
    public CartID getCartId(Long productColorId){
        Long id = Utils.getIdCurrentUser();
        CartID cartID = new CartID(productColorId,id);
        return cartID;
    }
}
