package com.group04.tgdd.service.iplm;

import com.group04.tgdd.dto.CartDTO;
import com.group04.tgdd.dto.CartResp;
import com.group04.tgdd.dto.OrderDetailResp;
import com.group04.tgdd.model.*;
import com.group04.tgdd.repository.CartRepo;
import com.group04.tgdd.repository.ProductColorRepo;
import com.group04.tgdd.service.CartService;
import com.group04.tgdd.service.ProductColorService;
import com.group04.tgdd.service.ProductOptionService;
import com.group04.tgdd.service.UserService;
import com.group04.tgdd.utils.MoneyConvert;
import com.group04.tgdd.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartIplm implements CartService {

    private final CartRepo cartRepo;
    private final ProductColorService productColorService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public void save(Long productColorId, int quantity) {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        CartDTO cartDTO = new CartDTO(new CartID(productColorId,Long.valueOf(id)),quantity);
        update(cartDTO);
    }

    @Override
    public List<CartResp> view(Long userID) {
        List<Cart> cartList = cartRepo.findByCartID_UserId(userID);
        List<CartResp> cartResps = new ArrayList<>();
        if(cartList != null){
            cartList.forEach(cart -> {
                CartResp cartResp = new CartResp();
                ProductColor productColor = productColorService.getProductColorById(cart.getCartID().getProductColorId());

                cartResp.setQuantity(cart.getQuantity());
                Product product = productColor.getProductOption().getProduct();
                cartResp.setItem(new CartResp.Items(
                        productColor,
                        productColor.getProductOption(),
                        product.getName(),
                        product.getProductImages().get(0).getUrlImage(),
                        MoneyConvert.calculaterPrice(productColor.getProductOption().getPrice(),productColor.getProductOption().getPromotion()),
                        productColor.getProductOption().getPrice(),
                        productColor.getProductOption().getPromotion()));
                cartResps.add(cartResp);
            });
            return cartResps;
        }
        else{
            return null;
        }
    }

    public void update(CartDTO cartDTO){
        Cart updatedCart =
                cartRepo.findCartByUserIdAndProductColorId(cartDTO.getCartID().getUserId(),cartDTO.getCartID().getProductColorId());

        if (updatedCart==null){
            updatedCart = modelMapper.map(cartDTO, Cart.class);
//            updatedCart.setCartID(cartDTO.getCartID());
//            updatedCart.setQuantity(cartDTO.getQuantity());
            cartRepo.save(updatedCart);
        } else {
            updatedCart.setQuantity(updatedCart.getQuantity()+cartDTO.getQuantity());
            cartRepo.save(updatedCart);
        }
    }

    @Modifying
    @Transactional
    @Override
    public Integer deleteCart(CartID cartID) {
        return cartRepo.deleteByCartID(cartID);
    }

    @Override
    public Cart updateCart(CartID cartID, int quantity) {
        Cart updatedCart = cartRepo.findById(cartID).orElse(null);
        if(updatedCart != null){
            updatedCart.setQuantity(quantity);
        }
        return updatedCart;
    }

    @Override
    public Cart decrease(CartID cartID) {
        Cart updatedCart = cartRepo.findById(cartID).orElse(null);
        if(updatedCart != null && updatedCart.getQuantity() - 1 > 0 ){
            updatedCart.setQuantity(updatedCart.getQuantity() - 1);
        }
        return updatedCart;
    }

    @Override
    public Cart increase(CartID cartID) {
        Cart updatedCart = cartRepo.findById(cartID).orElse(null);
        if(updatedCart != null && updatedCart.getQuantity() + 1 < 99 ){
            updatedCart.setQuantity(updatedCart.getQuantity() + 1);
        }
        return updatedCart;
    }

    @Override
    public void deleteAllCartUser() {
        Long userId = Utils.getIdCurrentUser();
        try {
            cartRepo.deleteAllByCartID_UserId(userId);
        } catch (Exception e){
            throw new NotFoundException("Cart not found");
        }
    }


}
