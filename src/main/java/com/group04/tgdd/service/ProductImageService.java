package com.group04.tgdd.service;

import com.group04.tgdd.dto.ProductImageReq;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {
    boolean saveNewImage(List<ProductImageReq> productImageReqs);

    void deleteImageProduct(Long id);

    List<String> uploadImageProduct(Long productId, Long colorId, List<MultipartFile> imgs);
}
