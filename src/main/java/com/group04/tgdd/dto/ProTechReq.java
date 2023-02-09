package com.group04.tgdd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProTechReq {
    private Long productId;
    private Long techId;
    private String info;
}
