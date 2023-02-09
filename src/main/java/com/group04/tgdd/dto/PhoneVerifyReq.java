package com.group04.tgdd.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PhoneVerifyReq {
    private Long userID;
    private String token;
}
