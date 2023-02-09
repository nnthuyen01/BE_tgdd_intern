package com.group04.tgdd.controller;

import com.group04.tgdd.dto.ProductImageReq;
import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.service.ProductImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping("/admin/productImageUrl")
    private ResponseEntity<?> addImageProduct(@RequestBody List<ProductImageReq> productImageReqs){
        if (productImageReqs.size()!=0){
            boolean check = productImageService.saveNewImage(productImageReqs);
            if (check) {
                return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false,"Failed",null));

    }

    @DeleteMapping("/admin/productImageUrl/{id}")
    private ResponseEntity<?> deleteImageProduct(@PathVariable Long id){
        productImageService.deleteImageProduct(id);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));

    }
    @PutMapping(value = "/admin/product/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<?> uploadImageProduct(@RequestParam Long productId,
                                                 @RequestParam Long colorId,
                                                 @RequestParam List<MultipartFile> imgs){
        List<String> urls = productImageService.uploadImageProduct(productId,colorId,imgs);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",urls));

    }
}
