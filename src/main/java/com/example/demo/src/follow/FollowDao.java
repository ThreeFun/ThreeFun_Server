package com.example.demo.src.follow;

import com.example.demo.src.follow.model.GetFollowerRes;
import com.example.demo.src.follow.model.PostFollowReq;
import com.example.demo.src.team.model.GetCurrentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class FollowDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 내가 팔로우 하는 유저 리스트
    public List<GetFollowerRes> getFollowerList(int userIdx){
        String checkFollowerExistQuery = "select userName, userProfile from User where userIdx in (select followeeIdx from Follow where followerIdx = ?);";
        int checkFollowerExistParams = userIdx;
        return this.jdbcTemplate.query(checkFollowerExistQuery,
                (rs, rowNum) -> new GetFollowerRes(
                    rs.getString("userProfile"),
                    rs.getString("userName")
        ), checkFollowerExistParams);
    }

    // 나를 팔로우 하는 유저 리스트
    public List<GetFollowerRes> getFolloweeList(int userIdx){
        String checkFolloweeExistQuery = "select userName, userProfile from User where userIdx in (select followerIdx from Follow where followeeIdx = ?);";
        int checkFolloweeExistParams = userIdx;
        return this.jdbcTemplate.query(checkFolloweeExistQuery, (rs, rowNum) -> new GetFollowerRes(
                rs.getString("userProfile"),
                rs.getString("userName")
        ), checkFolloweeExistParams);
    }

    // 팔로우
    public int follows(PostFollowReq postFollowReq){
        String checkFollowsQuery = "insert into Follow(followerIdx, followeeIdx) values (?, ?)";
        Object[] insertFollowsParams = new Object[] {postFollowReq.getFollowerIdx(), postFollowReq.getFolloweeIdx()};
        return this.jdbcTemplate.update(checkFollowsQuery,
                insertFollowsParams);
    }

    // 언팔로우
    public int unfollow(PostFollowReq postFollowReq){
        String checkUnfollowQuery = "UPDATE Follow SET status = 0 WHERE followerIdx = ? and followeeIdx = ?";
        Object[] insertUnfollowsParams = new Object[] {postFollowReq.getFollowerIdx(), postFollowReq.getFolloweeIdx()};
        return this.jdbcTemplate.update(checkUnfollowQuery,
                insertUnfollowsParams);
    }
}

