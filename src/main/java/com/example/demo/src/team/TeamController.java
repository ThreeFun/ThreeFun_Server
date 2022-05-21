package com.example.demo.src.team;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.team.model.PostTeamImageReq;
import com.example.demo.src.team.model.PostTeamReq;
import com.example.demo.src.team.model.PostTeamRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserService;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
public class TeamController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final TeamProvider teamProvider;
    @Autowired
    private final TeamService teamService;
    @Autowired
    private final JwtService jwtService;


    public TeamController(TeamProvider teamProvider, TeamService teamService, JwtService jwtService){
        this.teamProvider = teamProvider;
        this.teamService = teamService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @PostMapping("") // http://localhost:9000/teams
    public BaseResponse<PostTeamRes> createPosts(@RequestBody PostTeamReq postTeamReq) {
        try{
//            int userIdxByJwt = jwtService.getUserIdx();
//            if (postTeamReq.getMasterIdx() != userIdxByJwt) {
//                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
//            }

            if (postTeamReq.getTeamPassword() != null) {
                if (postTeamReq.getTeamPassword().length() > 3) {
                    return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 이미지는 최대 3개 등록 가능합니다.
                }
            }

            if (postTeamReq.getTitle().length() > 30) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 제목은 30글자 이하만 가능합니다.
            }

            if (postTeamReq.getImages().size() > 450) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 이미지 등록 가능 개수가 초과되었습니다.
            }

            // null값 에러 처리
            if (postTeamReq.getContent() == null) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 내용을 입력해주세요.
            }
            if (postTeamReq.getTitle() == null) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 제목을 입력해주세요.
            }
            if (postTeamReq.getMeetingTime() == null) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 시간을 입력해주세요.
            }
            if (postTeamReq.getMeetingDate() == null) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 날짜를 입력해주세요.
            }
            if (postTeamReq.getRegionIdx() <= 0) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 지역을 선택해주세요.
            }
            if (postTeamReq.getPrice() <= 0) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 가격을 설정해주세요.
            }
            if (postTeamReq.getPersonnel() <= 0) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 정원을 설정해주세요.
            }
            if (postTeamReq.getEntryFee() <= 0) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 참가비를 설정해주세요.
            }
            if (postTeamReq.getSecret() <= 0) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 비밀 여부를 선택해주세요.
            }
            if (postTeamReq.getCategoryIdx() <= 0) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 카테고리를 선택해주세요.
            }


            PostTeamRes postTeamRes = teamService.createTeam(postTeamReq.getMasterIdx(), postTeamReq);
            return new BaseResponse<>(postTeamRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
