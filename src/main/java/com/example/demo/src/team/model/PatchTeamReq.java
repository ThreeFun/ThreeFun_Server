package com.example.demo.src.team.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PatchTeamReq {
    private String title;
    private String content;
    private String meetingTime;
    private String meetingDate;
    private int regionIdx;
    private int price;
    private int personnel;
    private int entryFee;

    @Nullable
    private String teamPassword;

    private int secret;
    private int categoryIdx;

//    // 이미지
//    private List<String> images;
//    private int teamIdx;
//    private int mainImage;
}
