package com.example.demo.src.team.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@AllArgsConstructor
public class GetFollowList {
    private int teamIdx;
    private String userName;
    private String userProfile;
    private String regionName;
    private String categoryName;
    private String image;
}
