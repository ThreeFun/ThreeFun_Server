package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.follow.model.GetFollowerRes;
import com.example.demo.src.team.TeamProvider;
import com.example.demo.src.team.TeamService;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follows")
public class FollowController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final FollowProvider followProvider;
    @Autowired
    private final FollowService followService;
    @Autowired
    private final JwtService jwtService;


    public FollowController(FollowProvider followProvider, FollowService followService, JwtService jwtService) {
        this.followProvider = followProvider;
        this.followService = followService;
        this.jwtService = jwtService;
    }

    // 내가 팔로우한 사용자들 조회
    // GetFollowerRes
    @ResponseBody
    @GetMapping("/{userIdx}/followers") // /follows/:userIdx/followers
    public BaseResponse<List<GetFollowerRes>> getFollowerList(@PathVariable("userIdx") int userIdx) {
        try{
//            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }
            List<GetFollowerRes> getFollowerRes = followProvider.getFollowerList(userIdx);
            return new BaseResponse<>(getFollowerRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 나를 팔로우하는 사용자들 조회
    // GetFollowerRes
    @ResponseBody
    @GetMapping("/{userIdx}/followees") // /follows/:userIdx/followees
    public BaseResponse<List<GetFollowerRes>> getFolloweeList(@PathVariable("userIdx") int userIdx) {
        try{
            // jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            // userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            List<GetFollowerRes> getFollowerRes = followProvider.getFolloweeList(userIdx);
            return new BaseResponse<>(getFollowerRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}