package com.group04.tgdd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data

public class EventReq {
    private Long id;
    private String name;
    private String banner;
    private String color;
    private String award;
    private List<Long> productIdList;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime expireTime;

}
