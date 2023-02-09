package com.group04.tgdd.controller;

import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.dto.VoucherReq;
import com.group04.tgdd.service.VoucherService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class VoucherController {
    private final VoucherService voucherService;

    @PostMapping("/voucher")
    public ResponseEntity<?> save(@RequestBody VoucherReq voucherReq){

        return ResponseEntity.ok(new ResponseDTO(true,"Success", voucherService.save(voucherReq)));
    }
    @GetMapping("/voucher")
    public ResponseEntity<?> get(@RequestParam String code){

        return ResponseEntity.ok(new ResponseDTO(true,"Success", voucherService.get(code)));
    }
    @GetMapping("/voucher-category")
    public ResponseEntity<?> getByCategory(@RequestParam Long categoryId){

        return ResponseEntity.ok(new ResponseDTO(true,"Success", voucherService.getByCategory(categoryId)));
    }
    @DeleteMapping("/voucher")
    public ResponseEntity<?> delete(@RequestParam String code){
        voucherService.delete(code);
        return ResponseEntity.ok(new ResponseDTO(true,"Success", null));
    }

    @PutMapping("/voucher")
    public ResponseEntity<?> update(@RequestBody VoucherReq voucherReq){
        return ResponseEntity.ok(new ResponseDTO(true,"Success", voucherService.update(voucherReq)));
    }

    @PutMapping("/voucher-addCategory")
    private ResponseEntity<?> addProductToEvent(@RequestParam Long voucherId, @RequestParam Long categoryId){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",voucherService.addCategoryToVoucher(voucherId, categoryId)));

    }
}
