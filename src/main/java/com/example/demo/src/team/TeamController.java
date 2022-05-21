package com.example.demo.src.team;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.team.model.GetTeamAll;
import com.example.demo.src.team.model.PatchTeamReq;
import com.example.demo.src.team.model.PostTeamReq;
import com.example.demo.src.team.model.PostTeamRes;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

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

            if (postTeamReq.getImage().size() > 450) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 이미지 등록 가능 개수가 초과되었습니다.
            }

            // null값 에러 처리
            if (postTeamReq.getSecret() == 1) {
                if (postTeamReq.getTeamPassword() == null) {
                    return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 비밀번호를 설정해주세요.
                }
            }
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
            if (postTeamReq.getCategoryIdx() <= 0) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 카테고리를 선택해주세요.
            }


            PostTeamRes postTeamRes = teamService.createTeam(postTeamReq.getMasterIdx(), postTeamReq);
            return new BaseResponse<>(postTeamRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 팀 수정
    @ResponseBody
    @PatchMapping ("/{teamIdx}/{userIdx}") // http://localhost:9000/teams/:teamIdx/:userIdx
    public BaseResponse<String> modifyTeam(@PathVariable("teamIdx") int teamIdx, @PathVariable("userIdx") int userIdx, @RequestBody PatchTeamReq patchTeamReq) {
        try {
            if (patchTeamReq.getTitle().length() > 30) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 제목은 30글자 이하만 가능합니다.
            }

            // null값 에러 처리
            if (patchTeamReq.getSecret() == 1) {
                if (patchTeamReq.getTeamPassword() == null) {
                    return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 비밀번호를 설정해주세요.
                }
            }

            teamService.modifyTeam(userIdx, teamIdx, patchTeamReq);
            String result = "팀 수정을 완료하였습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 팀 삭제 - PATCH
    @ResponseBody
    @PatchMapping ("/{teamIdx}/{userIdx}/status") // http://localhost:9000/teams/:teamIdx/:userIdx/status
    public BaseResponse<String> deleteTeam(@PathVariable("teamIdx") int teamIdx, @PathVariable("userIdx") int userIdx) {
        try{
            teamService.deleteTeam(teamIdx);
            String result = "팀 삭제를 완료하였습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 팀 1개 조회 API
     * [GET] /teams/:teamIdx/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
//    @ResponseBody
//    @GetMapping("/{teamIdx}/{userIdx}")
//    public BaseResponse<GetUserRes> getTeam(@PathVariable("teamIdx") int teamIdx, @PathVariable("userIdx") int userIdx) {
//        try{
//            GetUserRes getUserRes = userProvider.getUser(userIdx);
//            return new BaseResponse<>(getUserRes);
//        } catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//
//    }


    /**
     * 모든 팀 게시글 조회 API
     * [GET] /teams/:userIdx?regionName=
     * @return BaseResponse<List<GetTeamAll>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetTeamAll>> getTeamAll(@PathVariable("userIdx") int userIdx, @RequestParam("regionName") String regionName) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetTeamAll> getTeamAll = teamProvider.getTeamAll(userIdx, regionName);
            return new BaseResponse<>(getTeamAll);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
