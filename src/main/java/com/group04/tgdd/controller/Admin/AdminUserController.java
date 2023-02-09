package com.group04.tgdd.controller.Admin;

import com.group04.tgdd.dto.AllUserResp;
import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.model.Users;
import com.group04.tgdd.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class AdminUserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> findAll(@RequestParam int page, @RequestParam int size){
        AllUserResp result = new AllUserResp();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page-1,size);
        result.setUsers(userService.findAll(pageable));
        result.setTotalPage((int) Math.ceil((double) (userService.totalUser())/size));

        return ResponseEntity.ok(new ResponseDTO(true,"Success", result));
    }

    // Get users by ID
    @GetMapping("/users/{usersId}")
    public ResponseEntity<?> findById(@PathVariable Long usersId){
        Users users = userService.findById(usersId);
        if (users!=null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",users));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Users ID not exits",null));
    }

    @PutMapping("/users/disable/{userId}")
    public ResponseEntity<?> disable(@PathVariable Long userId){
        userService.disableUserById(userId);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }

}
