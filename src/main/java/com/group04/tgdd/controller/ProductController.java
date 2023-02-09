package com.group04.tgdd.controller;

import com.group04.tgdd.dto.*;
import com.group04.tgdd.model.Product;
import com.group04.tgdd.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Search product")
    @GetMapping("/product/search")
    public ResponseEntity<?> search(@RequestParam(defaultValue = "") String keyword,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int size){
        List<SearchProductResp> searchProductRespList = productService.search(keyword,page,size);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",searchProductRespList));
    }

    @Operation(summary = "add product", description = "required categoryId, manufactureId," +
            "optional subcategoryId")
    @PostMapping("/admin/product")
    private ResponseEntity<?> addProduct(@RequestBody ProductReq productReq){
        Product product= productService.saveNewProduct(productReq);
        if (product !=null){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",product));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false,"Failed",null));
    }

    @Operation(summary = "update product", description = "required categoryId, manufactureId," +
            "optional subcategoryId")
    @PutMapping("/admin/product")
    private ResponseEntity<?> updateProduct(@RequestBody ProductReq productReq){
        Product product = productService.updateProduct(productReq);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",product));

    }

    @Operation(summary = "get product by ID")
    @GetMapping("/product/{productId}")
    private ResponseEntity<?> getProductById(@PathVariable Long productId){
        ProductDetailResp productResp = productService.findProductById(productId);
        if (productResp !=null){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",productResp));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(true,"Not Found Product ID",null));
    }

    @Operation(summary = "delete product")
    @DeleteMapping("/admin/product/{id}")
    private ResponseEntity<?> deleteProductById(@PathVariable Long id){
        productService.deleteProductById(id);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));

    }

    @Operation(summary = "search product",
            description = "filter product by keyword, manufactureID, categoryID, subCategoryId," +
                    ". all param optional (page>=1) ")
    @GetMapping("/product")
    private ResponseEntity<?> getProductByKeyword(@RequestParam(defaultValue = "") String keyword,
                                                  @RequestParam(defaultValue = "0") Long manufacturerId,
                                                  @RequestParam(defaultValue = "0") Long categoryId,
                                                  @RequestParam(defaultValue = "0") Long subCategoryId,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size){
        List<SearchProductResp> productResps =productService.getProductByKeyword(keyword,manufacturerId,categoryId,subCategoryId,page,size);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",productResps));
    }

    @PutMapping("/product/disable/{productId}")
    private ResponseEntity<?> disableProduct(@PathVariable Long productId){
        productService.disableProduct(productId);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }

}
