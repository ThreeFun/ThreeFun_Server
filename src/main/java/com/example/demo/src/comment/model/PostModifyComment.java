package com.example.demo.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostModifyComment {
    private int userIdx;
    private int commentIdx;
    private String comment;
}
