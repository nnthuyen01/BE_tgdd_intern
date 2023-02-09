package com.group04.tgdd.controller;

import com.group04.tgdd.dto.EventReq;
import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.service.EventService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class EventController {
    private final EventService eventService;

    @PostMapping("/event")
    public ResponseEntity<?> save(@RequestBody EventReq eventReq){

        return ResponseEntity.ok(new ResponseDTO(true,"Success", eventService.save(eventReq)));
    }
    @GetMapping("/event")
    public ResponseEntity<?> get(@RequestParam String name){

        return ResponseEntity.ok(new ResponseDTO(true,"Success", eventService.get(name)));
    }
    @GetMapping("/currentEvent")
    public ResponseEntity<?> getCurrentEvent(){

        return ResponseEntity.ok(new ResponseDTO(true,"Success", eventService.getCurrentEvent()));
    }
    @DeleteMapping("/event")
    public ResponseEntity<?> delete(@RequestParam String name){
        eventService.delete(name);
        return ResponseEntity.ok(new ResponseDTO(true,"Success", null));
    }

    @PutMapping("/event")
    public ResponseEntity<?> update(@RequestBody EventReq eventReq){
        return ResponseEntity.ok(new ResponseDTO(true,"Success", eventService.update(eventReq)));
    }
    @PutMapping("/event-addProduct")
    private ResponseEntity<?> addProductToEvent(@RequestParam Long eventId, @RequestParam Long productId){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",eventService.addProductToEvent(eventId, productId)));

    }
}
