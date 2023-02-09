package com.group04.tgdd.controller;

import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.model.Manufacturer;
import com.group04.tgdd.service.ManufacturerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;
    // Get All manufacturer
    @GetMapping("/manufacturer")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",manufacturerService.findAll()));
    }

    @GetMapping("/manufacturer/{manufacturerId}")
    public ResponseEntity<?> findById(@PathVariable Long manufacturerId){
        Manufacturer manufacturer = manufacturerService.findById(manufacturerId);
        if (manufacturer !=null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",manufacturer));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Manufacturer ID not exits",null));
    }

    // Add payment
    @PostMapping("/admin/manufacturer")
    public ResponseEntity<?> saveManufacturer(@RequestBody Manufacturer manufacturer){
        Manufacturer manufacturerSave =  manufacturerService.save(manufacturer);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",manufacturerSave));
    }

    // Update Payment
    @PutMapping("/admin/manufacturer")
    public ResponseEntity<?> updateManufacturer(@RequestBody Manufacturer manufacturer){
        Manufacturer manufacturerUpdate = manufacturerService.updateManufacturer(manufacturer);
        if (manufacturerUpdate!= null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",manufacturerUpdate));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Manufacturer ID not exits",null));
    }

    //Delete Payment
    @DeleteMapping("/admin/manufacturer/{id}")
    public ResponseEntity<?> deleteManufacturer(@PathVariable Long id){
        boolean check = manufacturerService.deleteManufacturer(id);
        if (check){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false,"Manufacturer ID not exits",null));
    }
}
