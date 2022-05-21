package com.example.demo.src.team.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostTeamReq {
    private String title;
    private String content;
    private String meetingTime;
    private String meetingDate;
    private int regionIdx;
    private int price;
    private int personnel;
    private int entryFee;
    private int allAgree;
    private int payment;

    @Nullable
    private String teamPassword;

    private int secret;
    private int masterIdx;
    private int categoryIdx;

    private List<String> image;
    private int mainImage;
}
