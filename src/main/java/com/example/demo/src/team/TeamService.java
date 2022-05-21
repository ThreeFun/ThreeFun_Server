package com.example.demo.src.team;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.team.model.PatchTeamReq;
import com.example.demo.src.team.model.PostTeamReq;
import com.example.demo.src.team.model.PostTeamRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Service Create, Update, Delete 의 로직 처리
@Service
public class TeamService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TeamDao teamDao;
    private final TeamProvider teamProvider;
    private final JwtService jwtService;


    @Autowired
    public TeamService(TeamDao teamDao, TeamProvider teamProvider, JwtService jwtService) {
        this.teamDao = teamDao;
        this.teamProvider = teamProvider;
        this.jwtService = jwtService;

    }

    // 팀 생성
    public PostTeamRes createTeam(int userIdx, PostTeamReq postTeamReq) throws BaseException {

        //try{
            int teamIdx = teamDao.insertTeam(userIdx, postTeamReq, postTeamReq.getImage());
            return new PostTeamRes(teamIdx);
        //}
        //catch (Exception exception) {
            //throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        //}
    }

    // 팀 수정
    public void modifyTeam(int userIdx, int teamIdx, PatchTeamReq patchTeamReq) throws BaseException {

        if (teamProvider.checkUserExist(userIdx) == 0) {
            throw new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID);
        }

        if (teamProvider.checkTeamExist(teamIdx) == 0) {
            throw new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID); // POSTS_EMPTY로 바꾸기
        }

        try{
            int result = teamDao.updateTeam(teamIdx, patchTeamReq);

            // 성공이면 result = 1, 실패면 result = 0
            if (result == 0) {
                throw new BaseException(BaseResponseStatus.MODIFY_FAIL_USERNAME); // MODIFY_FAIL_TEAM으로 바꾸기
            }
        }
        catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    // 팀 삭제 - PATCH
    public void deleteTeam(int teamIdx) throws BaseException {
        if (teamProvider.checkTeamExist(teamIdx) == 0) {
            throw new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID); // 수정하기
        }

        try{
            int result = teamDao.deleteTeam(teamIdx);

            // 성공이면 result = 1, 실패면 result = 0
            if (result == 0) {
                throw new BaseException(BaseResponseStatus.MODIFY_FAIL_USERNAME); // 수정
            }
        }
        catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

}
