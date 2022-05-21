package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.follow.model.GetFollowerRes;
import com.example.demo.src.team.TeamDao;
import com.example.demo.src.team.model.GetTeamAll;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

//Provider : Read의 비즈니스 로직 처리
@Service
public class FollowProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FollowDao followDao;
    private final JwtService jwtService;


    @Autowired
    public FollowProvider(FollowDao followDao, JwtService jwtService) {
        this.followDao = followDao;
        this.jwtService = jwtService;
    }

    // 내가 팔로우 하는 유저 리스트
    public List<GetFollowerRes> getFollowerList(int userIdx) throws BaseException {
        try{
            List<GetFollowerRes> getFollowerRes = followDao.getFollowerList(userIdx);
            return getFollowerRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 나를 팔로우 하는 유저 리스트
    public List<GetFollowerRes> getFolloweeList(int userIdx) throws BaseException {
        try{
            List<GetFollowerRes> getFolloweeRes = followDao.getFolloweeList(userIdx);
            return getFolloweeRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
