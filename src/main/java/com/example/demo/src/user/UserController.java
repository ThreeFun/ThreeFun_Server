package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String Email) {
        try{
            if(Email == null){
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            }
            // Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsersByEmail(Email);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try{
            GetUserRes getUserRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        if(postUserReq.getId() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_ID);
        }
        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getId())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        if(postUserReq.getUserName() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_USERNAME);
        }

        if(postUserReq.getUserName().length() > 16){
            return new BaseResponse<>(POST_USERS_INVALID_USERNAME);
        }

        if(postUserReq.getPassword() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }

        if(postUserReq.getPassword().length() > 20){
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }

        if(postUserReq.getRegionIdx() == 0){
            return new BaseResponse<>(POST_USERS_EMPTY_REGION);
        }

        if(postUserReq.getRegionIdx() < 1 || postUserReq.getRegionIdx() > 16){
            return new BaseResponse<>(POST_USERS_INVALID_REGION);
        }

        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            if(postLoginReq.getId() == null){
                return new BaseResponse<>(POST_USERS_EMPTY_ID);
            }
            //이메일 정규표현
            if(!isRegexEmail(postLoginReq.getId())){
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            }

            if(postLoginReq.getPassword() == null){
                return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
            }

            if(postLoginReq.getPassword().length() > 20){
                return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
            }

            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody User user){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            PatchUserReq patchUserReq = new PatchUserReq(userIdx,user.getUserName());
            userService.modifyUserName(patchUserReq);

            String result = "";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 사용자 검색 API
     * [POST] /users/search
     * @return BaseResponse<PostSearchUserRes>
     */
    @ResponseBody
    @PostMapping("/search")
    public BaseResponse<List<PostSearchUserRes>> searchUser(@RequestBody PostSearchUserReq postSearchUserReq){
        try{
            int userIdx = postSearchUserReq.getUserIdx();
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if(postSearchUserReq.getUserIdx() == 0){
                return new BaseResponse<>(POST_USERS_EMPTY_IDX);
            }

            Object obj = postSearchUserReq.getUserIdx();
            if(obj instanceof String){
                return new BaseResponse<>(POST_USERS_INVALID_IDX);
            }

            if(postSearchUserReq.getUserName() == null){
                return new BaseResponse<>(POST_USERS_EMPTY_USERNAME);
            }

            if(postSearchUserReq.getUserName().length() > 16){
                return new BaseResponse<>(POST_USERS_INVALID_USERNAME);
            }

            List<PostSearchUserRes> postSearchUserRes = userProvider.searchUser(postSearchUserReq);
            return new BaseResponse<>(postSearchUserRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
