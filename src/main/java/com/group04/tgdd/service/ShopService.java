package com.group04.tgdd.service;

import com.group04.tgdd.model.Shop;

import java.util.List;

public interface ShopService {
    Shop saveNewShop(Shop shop);
    Shop updateShop(final Long id, Shop shop);

    void deleteShop(final Long id);

    List<Shop> getAllShop(Integer page, Integer size);
}
