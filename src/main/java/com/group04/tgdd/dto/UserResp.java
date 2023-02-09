package com.group04.tgdd.dto;

import com.group04.tgdd.model.Address;
import lombok.Data;

import java.util.List;

@Data
public class UserResp {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String gender;
    private List<Address> addresses;
    private String provider;
    // admin
    private String role;
    private boolean enable;

}
