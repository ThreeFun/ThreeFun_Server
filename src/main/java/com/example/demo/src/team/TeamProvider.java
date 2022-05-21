package com.example.demo.src.team;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
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
public class TeamProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TeamDao teamDao;
    private final JwtService jwtService;


    @Autowired
    public TeamProvider(TeamDao teamDao, JwtService jwtService) {
        this.teamDao = teamDao;
        this.jwtService = jwtService;
    }

    // 존재하는 유저인지 확인
    public int checkUserExist(int userIdx) throws BaseException {
        try{
            return teamDao.checkUserExist(userIdx);
        } catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    // 존재하는 게시물인지 확인
    public int checkTeamExist(int teamIdx) throws BaseException{
        try{
            return teamDao.checkTeamExist(teamIdx);
        } catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<GetTeamAll> getTeamAll(int userIdx, String regionName) throws BaseException{
        try{
            List<GetTeamAll> getTeamAll = teamDao.getTeamAll(userIdx, regionName);
            return getTeamAll;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
