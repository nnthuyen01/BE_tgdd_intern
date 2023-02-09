package com.group04.tgdd.controller;

import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.model.Payment;
import com.group04.tgdd.service.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class PaymentController {

    private final PaymentService paymentService;
    // Get All payment
    @GetMapping("/payment")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",paymentService.findAll()));
    }

    // Get payment by ID
    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<?> findById(@PathVariable Long paymentId){
        Payment payment = paymentService.findById(paymentId);
        if (payment !=null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success", payment));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false,"Payment ID not exits",null));
    }

    // Add payment
    @PostMapping("/admin/payment")
    public ResponseEntity<?> savePayment(@RequestBody Payment payment){
       Payment paymentSave =  paymentService.save(payment);
        return ResponseEntity.ok(new ResponseDTO(true,"Success", paymentSave));
    }

    // Update Payment
    @PutMapping("/admin/payment")
    public ResponseEntity<?> updatePayment(@RequestBody Payment payment){
        Payment paymentUpdate = paymentService.updatePayment(payment);
        if (paymentUpdate != null)
        return ResponseEntity.ok(new ResponseDTO(true,"Success", paymentUpdate));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false,"Payment ID not exits",null));
    }

    //Delete Payment
    @DeleteMapping("/admin/payment/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long id){
        boolean check = paymentService.deletePayment(id);
        if (check){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false,"Payment ID not exits",null));
    }
}
