package com.group04.tgdd.controller;


import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.mapper.ShopMapper;
import com.group04.tgdd.model.Product;
import com.group04.tgdd.model.Shop;
import com.group04.tgdd.service.ShopService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/shop")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class ShopController {

    private final ShopService shopService;
    private final ShopMapper shopMapper;

    //Save new shop
    @PostMapping
    private ResponseEntity<?> saveShop(@RequestBody Shop shop){
        Shop newShop = shopService.saveNewShop(shop);
        if (newShop !=null){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",newShop));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false,"Failed",null));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateShop(@PathVariable final Long id, @RequestBody Shop shop) {
        try {
            // Update ok
            return ResponseEntity.ok(new ResponseDTO(true, "Success",
                    shopMapper.toShopDTO(shopService.updateShop(id, shop))));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(false, "Failed", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShop(@PathVariable final Long id) {
        try {
            // Delete ok
            shopService.deleteShop(id);
            return ResponseEntity.ok(new ResponseDTO(true, "Success", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(false, "Failed", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllShop(@RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "4") Integer size) {
        try {
            // Get all ok
            return ResponseEntity.ok(new ResponseDTO(true, "Success",
                    shopMapper.toShopDTO(shopService.getAllShop(page, size))));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(false, "Failed", e.getMessage()));
        }
    }
}
