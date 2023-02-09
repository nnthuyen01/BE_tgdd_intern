package com.group04.tgdd.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class phoneLoginRequest {
    @NotBlank(message = "Phone may not be blank")
    private String phone;
    @NotBlank(message = "Password may not be blank")
    private String password;
}
