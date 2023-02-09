package com.group04.tgdd.service;

import com.group04.tgdd.dto.ProductColorReq;
import com.group04.tgdd.model.ProductColor;

public interface ProductColorService {
    ProductColorReq saveNewProductColor(ProductColorReq productColorReq);

    void updateProductColor(Long productColorId, int quantity);

    void deleteProductColorById(Long id);

    ProductColor getProductColorById(Long id);
}
