package com.group04.tgdd.dto;

import com.group04.tgdd.model.ProductColor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class OrderReq {
    @NotEmpty(message = "Paymend method not empty")
    private String paymentMethod;
    private String discountCode;
    @NotEmpty(message = "Delivery address not empty")
    private String deliveryAddress;
    private String description;
    private String differentReceiverName;
    private String differentReceiverPhone;
    @NotNull(message = "Item not empty")
    private List<Items> items;
    @Data
    public static class Items{
        @NotBlank(message = "Product ID may not be blank")
        private Long productColorId;
        @NotBlank(message = "quantity may not be blank")
        private int quantity;
    }
}
