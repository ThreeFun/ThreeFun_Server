package com.example.demo.src.follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostFollowReq {
    private int followerIdx; // 사용자
    private int followeeIdx; // 상대방
}
