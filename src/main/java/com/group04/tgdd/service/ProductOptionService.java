package com.group04.tgdd.service;

import com.group04.tgdd.dto.ProductOpReq;
import com.group04.tgdd.model.Product;
import com.group04.tgdd.model.ProductColor;
import com.group04.tgdd.model.ProductOption;

import java.util.List;

public interface ProductOptionService {
    ProductOpReq saveNewProductOption(ProductOpReq productOpReq);

    void updateProductOption(ProductOpReq productOpReq);

    void deleteProductOption(Long id);

    ProductOption getProductOptionById(Long id);

}
