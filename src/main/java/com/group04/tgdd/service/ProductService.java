package com.group04.tgdd.service;

import com.group04.tgdd.dto.ProductReq;
import com.group04.tgdd.dto.ProductDetailResp;

import com.group04.tgdd.dto.SearchProductResp;
import com.group04.tgdd.model.Product;

import java.util.List;

public interface ProductService {
    Product saveNewProduct(ProductReq productReq);

    ProductDetailResp findProductById(Long productId);

    Product updateProduct(ProductReq productReq);

    void deleteProductById(Long id);

    List<SearchProductResp> getProductByKeyword(String keyword, Long manufacturerId, Long categoryId, Long subCategoryId,int page,int size);

    List<SearchProductResp> search(String keyword, int page, int size);

    void disableProduct(Long productId);
}
