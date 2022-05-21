package com.example.demo.src.team.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@AllArgsConstructor
public class GetCurrentList {
    private int teamIdx;
    private String categoryName;
    private String title;
    private String totalPrice;
    private String price;
    private String regionName;
    private String date;
    private String nowPeople;
    private String image;
}
