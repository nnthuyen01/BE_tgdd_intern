package com.group04.tgdd.controller;

import com.group04.tgdd.dto.ProductColorReq;
import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.service.ProductColorService;
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
public class ProductColorController {

    private final ProductColorService productColorService;


    @PostMapping("/admin/productColor")
    private ResponseEntity<?> addProductColor(@RequestBody ProductColorReq productColorReq){
        ProductColorReq product = productColorService.saveNewProductColor(productColorReq);
        if (product !=null){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",product));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false,"Failed",null));
    }


    @PutMapping("/admin/productColor/quantity")
    private ResponseEntity<?> updateProductColor(@RequestParam Long productColorId,
                                                 @RequestParam int quantity){
        productColorService.updateProductColor(productColorId,quantity);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }
    @DeleteMapping("/admin/productColor/{id}")
    private ResponseEntity<?> deleteProductColor(@PathVariable Long id){
        productColorService.deleteProductColorById(id);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }
}
