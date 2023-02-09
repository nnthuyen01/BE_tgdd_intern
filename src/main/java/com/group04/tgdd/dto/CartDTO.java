package com.group04.tgdd.dto;

import com.group04.tgdd.model.CartID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private CartID cartID;
    private int quantity;
}
