package com.group04.tgdd.mapper.impl;

import com.group04.tgdd.dto.ShopDTO;
import com.group04.tgdd.mapper.ShopMapper;
import com.group04.tgdd.model.Shop;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShopMapperImpl implements ShopMapper {

    @Override
    public ShopDTO toShopDTO(Shop shop) {
        ShopDTO shopDTO = new ShopDTO();
        if (shop != null) {
            shopDTO.setId(shop.getId());
            shopDTO.setName(shop.getName());
            shopDTO.setAddress(shop.getAddress());
        }
        return shopDTO;
    }

    @Override
    public List<ShopDTO> toShopDTO(List<Shop> shops) {
        List<ShopDTO> shopDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shops)) {
            for (Shop shop : shops) {
                shopDTOS.add(this.toShopDTO(shop));
            }
        }
        return shopDTOS;
    }
}
