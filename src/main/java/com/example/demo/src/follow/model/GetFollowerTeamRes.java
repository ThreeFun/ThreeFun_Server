package com.example.demo.src.follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFollowerTeamRes {
    private String title;
    private int price;
    private String region; // int regionIdx
    private String mainImage;
    private String category; // int categoryIdx
}
