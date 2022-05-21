package com.example.demo.src.team.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@AllArgsConstructor
public class GetRegionList {
    private int timeIdx;
    private String categoryName;
    private String title;
    private int totalPrice;
    private int price;
    private String regionName;
    private String meetingTime;
    private String meetingDate;
    private int numberOfPeople;
    private int personnel;
    private String image;
}

