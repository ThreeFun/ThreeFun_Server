package com.example.demo.src.team;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.team.model.PostTeamImageReq;
import com.example.demo.src.team.model.PostTeamReq;
import com.example.demo.src.team.model.PostTeamRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
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
            int teamIdx = teamDao.insertTeam(userIdx, postTeamReq);
//            for (int i=0; i<postTeamImageReq.getImages().size(); i++) {
//                teamDao.insertPostImgs(teamIdx, postTeamImageReq.getImages().get(i));
//            }
            return new PostTeamRes(teamIdx);
        //}
        //catch (Exception exception) {
            //throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        //}
    }
}
