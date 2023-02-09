package com.group04.tgdd.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AllUserResp {
    private int page;
    private int totalPage;
    private List<UserResp> users = new ArrayList<>();
}
