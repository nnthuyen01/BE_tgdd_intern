package com.group04.tgdd.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShopDTO implements Serializable {
    private Long id;
    private String name;
    private String address;
}
