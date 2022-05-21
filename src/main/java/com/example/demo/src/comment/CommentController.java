package com.example.demo.src.comment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.comment.CommentProvider;
import com.example.demo.src.comment.CommentService;
import com.example.demo.src.comment.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/comments")
public class CommentController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CommentProvider commentProvider;
    @Autowired
    private final CommentService commentService;
    @Autowired
    private final JwtService jwtService;

    public CommentController(CommentProvider commentProvider, CommentService commentService, JwtService jwtService){
        this.commentProvider = commentProvider;
        this.commentService = commentService;
        this.jwtService = jwtService;
    }

    /**
     * 댓글 등록 API
     * [POST] /comments
     * @return BaseResponse<Integer>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<Integer> createComment(@RequestBody PostComment postComment) {
        try{
            int commentIdx = commentService.createComment(postComment);
            return new BaseResponse<>(commentIdx);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 댓글 삭제 API
     * [PATCH] /comments/:commentIdx/:userIdx/status
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{commentIdx}/{userIdx}")
    public BaseResponse<String> deleteComment(@PathVariable("commentIdx") int commentIdx, @PathVariable("userIdx") int userIdx){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            commentService.deleteComment(commentIdx, userIdx);
            String result = "성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 댓글 수정 API
     * [POST] /comments/modify
     * @return BaseResponse<String>
     */
    // Body
    @ResponseBody
    @PostMapping("/modify")
    public BaseResponse<String> modifyComment(@RequestBody PostModifyComment postModifyComment) {
        try{
            int userIdx = postModifyComment.getUserIdx();
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            commentService.modifyComment(postModifyComment);
            String result = "성공";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
