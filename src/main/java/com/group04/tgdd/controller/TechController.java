package com.group04.tgdd.controller;

import com.group04.tgdd.dto.DetailSpecs;
import com.group04.tgdd.dto.ProTechReq;
import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.model.ProductTechnical;
import com.group04.tgdd.model.Technical;
import com.group04.tgdd.service.TechnicalService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class TechController {

    private final TechnicalService technicalService;

    @GetMapping("/tech")
    private ResponseEntity<?> getTech(){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",technicalService.getAll()));
    }
    @PostMapping("/admin/tech")
    private ResponseEntity<?> addTech(@RequestBody Technical technical){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",technicalService.saveTech(technical)));
    }
    @DeleteMapping("/admin/tech/{techId}")
    private ResponseEntity<?> deleteTech(@PathVariable Long techId){
        technicalService.deleteTech(techId);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }

    @GetMapping("/productTech")
    private ResponseEntity<?> getProductTech(@RequestParam Long productId){
        List<DetailSpecs> detailSpecs = technicalService.getAllProductTech(productId);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",detailSpecs));
    }


    @PostMapping("/admin/productTech")
    private ResponseEntity<?> addProductTech(@RequestBody List<ProTechReq> proTechReqs) {
        technicalService.saveAllProductTech(proTechReqs);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }
    @PutMapping("/admin/productTech")
    private ResponseEntity<?> updateProductTech(@RequestBody ProTechReq proTechReq){
        ProductTechnical productTechnical = technicalService.saveProductTech(proTechReq);
        if (productTechnical!=null){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
        }
        return ResponseEntity.ok(new ResponseDTO(false,"Failed",null));
    }

    @DeleteMapping("/admin/productTech/{id}")
    private ResponseEntity<?> deleteProductTech(@PathVariable Long id){

        technicalService.deleteProductTech(id);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }
}
