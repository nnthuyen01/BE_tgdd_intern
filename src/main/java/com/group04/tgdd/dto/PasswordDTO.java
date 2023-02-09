package com.group04.tgdd.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PasswordDTO {
    @NotBlank(message = "Email may not be blank")
    private String email;
    private String token;
    private String oldPassword;
    @NotBlank(message = "New Password may not be blank")
    private String newPassword;
}
