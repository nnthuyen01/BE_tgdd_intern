package com.group04.tgdd.controller;

import com.cloudinary.utils.ObjectUtils;
import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.dto.UpdateUserReq;
import com.group04.tgdd.dto.UserReq;
import com.group04.tgdd.model.Users;
import com.group04.tgdd.repository.UsersRepo;
import com.group04.tgdd.service.Cloudinary.CloudinaryUpload;
import com.group04.tgdd.service.UserService;
import com.group04.tgdd.utils.Utils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Convert;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class UserController {

    private final UserService userService;


    //Update user (role user)
    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserReq userReq){
        Users usersUpdate = userService.updateUser(userReq);
        if (usersUpdate!= null){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",usersUpdate));
        }else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"User ID not exits",null));
    }
    @PutMapping(value = "/user/avatar",consumes = {
            "multipart/form-data"})
    public ResponseEntity<?> upAvatar(@Parameter(
                                              description = "Files to be uploaded",
                                              content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)  // Won't work without OCTET_STREAM as the mediaType.
                                      )
                                      @RequestParam("img") MultipartFile file) throws IOException {
        String url = userService.upAvartar(file);

        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                url));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(){

        Users users = userService.getCurrentUser();
        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                users));
    }
    @DeleteMapping("/user/address/{addressId}")
    public ResponseEntity<?> deleteAddressUser(@PathVariable Long addressId){
        userService.deleteAddressUser(addressId);
        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",null));
    }
}
