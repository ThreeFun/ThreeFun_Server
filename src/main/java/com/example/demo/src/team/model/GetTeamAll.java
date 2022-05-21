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
    private List<GetRegionList> RegionList;
    private List<GetFollowList> followList;
    private List<GetCurrentList> currentList;
}
