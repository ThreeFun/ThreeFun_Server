package com.example.demo.src.team.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetTeamAll {
    private int timeIdx;
    private String categoryName;
    private String title;
    private String totalPrice;
    private String price;
    private String regionName;
    private String date;
    private String nowPeople;
    private String image;
    private List<GetFollowList> followList;
    private List<GetCurrentList> currentList;
}
