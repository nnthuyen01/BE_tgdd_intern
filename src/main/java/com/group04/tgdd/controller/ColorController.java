package com.group04.tgdd.controller;

import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.model.Color;
import com.group04.tgdd.service.ColorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "AUTHORIZATION")
@RequiredArgsConstructor

public class ColorController {

    private final ColorService colorService ;

    // Get All Color
    @GetMapping("/color")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",colorService.findAll()));
    }

    // Get Color by ID
    @GetMapping("/color/{colorId}")
    public ResponseEntity<?> findById(@PathVariable Long colorId){
        Color color = colorService.findById(colorId);
        if (color!=null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",color));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Color ID not exits",null));
    }

    // Add Color
    @PostMapping("/admin/color")
    public ResponseEntity<?> saveColor(@RequestBody Color color){
        Color colorSave =  colorService.save(color);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",colorSave));
    }

    //Update Color
    @PutMapping("/admin/color")
    public ResponseEntity<?> updateColor(@RequestBody Color color){
        Color colorUpdate = colorService.updateColor(color);
        if (colorUpdate!= null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",colorUpdate));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Color ID not exits",null));
    }

    //Delete Color
    @DeleteMapping("/admin/color/{id}")
    public ResponseEntity<?> deleteColor(@PathVariable Long id){
        boolean check = colorService.deleteColor(id);
        if (check){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false,"Color ID not exits",null));
    }
}
