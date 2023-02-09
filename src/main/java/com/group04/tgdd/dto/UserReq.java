package com.group04.tgdd.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UserReq {
    private Long id;
    @Email(message = "Email invalidate")
    @NotBlank(message = "Email may not be blank")
    private String email;
    private String address;
    private String gender;
    @NotBlank(message = "Password may not be blank")
    private String phone;
    @NotBlank(message = "Password may not be blank")
    @Size(min = 6,max = 26,message = "min = 6 and max 26")
    private String password;
    @NotBlank(message = "Name may not be blank")
    private String name;
}
