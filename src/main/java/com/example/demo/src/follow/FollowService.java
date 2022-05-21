package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.follow.model.PostFollowReq;
import com.example.demo.src.team.TeamDao;
import com.example.demo.src.team.TeamProvider;
import com.example.demo.src.team.model.PostTeamReq;
import com.example.demo.src.team.model.PostTeamRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Service Create, Update, Delete 의 로직 처리
@Service
public class FollowService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FollowDao followDao;
    private final FollowProvider followProvider;
    private final JwtService jwtService;


    @Autowired
    public FollowService(FollowDao followDao, FollowProvider followProvider, JwtService jwtService) {
        this.followDao = followDao;
        this.followProvider = followProvider;
        this.jwtService = jwtService;

    }

    // 팔로우
    public void follows(PostFollowReq postFollowReq) throws BaseException {

        try{
            int result = followDao.follows(postFollowReq);

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
