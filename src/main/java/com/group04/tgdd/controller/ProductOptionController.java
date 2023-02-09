package com.group04.tgdd.controller;

import com.group04.tgdd.dto.ProductOpReq;
import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.service.ProductOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class ProductOptionController {

    private final ProductOptionService productOptionService;

    @PostMapping("/admin/productOption")
    private ResponseEntity<?> addProductOption(@RequestBody ProductOpReq productOpReq){
        ProductOpReq product = productOptionService.saveNewProductOption(productOpReq);

        if (product !=null){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",product));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false,"Failed",null));
    }
    @PutMapping("/admin/productOption")
    private ResponseEntity<?> updateProductOption(@RequestBody ProductOpReq productOpReq){
        productOptionService.updateProductOption(productOpReq);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }
    @DeleteMapping("/admin/productOption/{id}")
    private ResponseEntity<?> deleteProductOption(@PathVariable Long id){
        productOptionService.deleteProductOption(id);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }
}
