package com.group04.tgdd.dto;

import com.group04.tgdd.model.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UpdateUserReq {
    private String gender;
    private String phone;
    private String name;
    private List<Address> addresses;
}
