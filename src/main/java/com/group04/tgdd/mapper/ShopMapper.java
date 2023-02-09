package com.group04.tgdd.mapper;

import com.group04.tgdd.dto.ShopDTO;
import com.group04.tgdd.model.Shop;

import java.util.List;

public  interface ShopMapper {
    ShopDTO toShopDTO(Shop shop);

    List<ShopDTO> toShopDTO(List<Shop> shops);
}
