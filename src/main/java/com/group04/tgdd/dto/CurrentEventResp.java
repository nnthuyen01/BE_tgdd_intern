package com.group04.tgdd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CurrentEventResp {

    public CurrentEventResp() {
        this.productList = new ArrayList<>();
    }

    private Long id;
    private String name;
    private String color;
    private String award;
    private String banner;
    private List<ProductCurrentEventResp> productList;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime expireTime;


}
