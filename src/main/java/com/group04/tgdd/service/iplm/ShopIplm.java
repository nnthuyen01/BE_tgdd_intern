package com.group04.tgdd.service.iplm;

import com.group04.tgdd.exception.NotFoundException;
import com.group04.tgdd.model.Shop;
import com.group04.tgdd.repository.ShopRepo;
import com.group04.tgdd.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShopIplm implements ShopService {

    private final ShopRepo shopRepo;
    @Override
    public Shop saveNewShop(Shop shop) {
        return shopRepo.save(shop);
    }

    @Override
    public Shop updateShop(Long id, Shop shop) {
        Optional<Shop> optionalShop = shopRepo.findById(id);
        if (!optionalShop.isPresent()) {
            throw new NotFoundException("Shop not found");
        }
        if (shopRepo.findByName(shop.getName()) != null) {
            throw new IllegalArgumentException("Name already exist");
        }
        Shop updateShop = optionalShop.get();
        updateShop.setName(shop.getName());
        updateShop.setAddress(shop.getAddress());
        return shopRepo.save(updateShop);
    }

    @Override
    public void deleteShop(Long id) {
        shopRepo.deleteById(id);
    }

    @Override
    public List<Shop> getAllShop(Integer page, Integer size) {
        List<Shop> shops = shopRepo.findAll();
        PagedListHolder<Shop> pagedListHolder = new PagedListHolder<>(shops);
        pagedListHolder.setPage(page - 1);
        pagedListHolder.setPageSize(size);
        return pagedListHolder.getPageList();
    }
}
