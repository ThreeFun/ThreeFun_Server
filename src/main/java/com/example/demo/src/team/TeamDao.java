package com.example.demo.src.team;

import com.example.demo.src.team.model.PatchTeamReq;
import com.example.demo.src.team.model.PostImage;
import com.example.demo.src.team.model.PostTeamReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class TeamDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 팀 생성 + 팀 이미지 삽입
    @Transactional
    public int insertTeam(int userIdx, PostTeamReq postTeamReq, List<PostImage> postImage){

        String insertTeamQuery = "INSERT INTO Team(content, title, meetingTime, meetingDate, regionIdx, price, personnel, entryFee, allAgree, payment, teamPassword, secret, masterIdx, categoryIdx) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] insertTeamParams = new Object[] {postTeamReq.getContent(), postTeamReq.getTitle(), postTeamReq.getMeetingTime(), postTeamReq.getMeetingDate(), postTeamReq.getRegionIdx(), postTeamReq.getPrice(), postTeamReq.getPersonnel(), postTeamReq.getEntryFee(), postTeamReq.getAllAgree(), postTeamReq.getPayment(), postTeamReq.getTeamPassword(), postTeamReq.getSecret(), postTeamReq.getMasterIdx(), postTeamReq.getCategoryIdx()};
        this.jdbcTemplate.update(insertTeamQuery,
                insertTeamParams);

        // 방금 넣은 team의 idx 값을 다시 클라이언트에게 전달 (return)
        String lastInsertIdxQuery = "SELECT last_insert_id()";
        int teamIdx = this.jdbcTemplate.queryForObject(lastInsertIdxQuery,int.class);

        // 팀 이미지 삽입
        if(postTeamReq.getImage() == null){
            return teamIdx;
        }

        for (int i = 0; i < postTeamReq.getImage().size(); i++ ){
            // TeamImage table에 이미지 삽입
            String createTeamImageQuery = "insert into TeamImage(image, mainImage, teamIdx) values (?, ?, ?)";
            Object[] createTeamImageParams = new Object[]{postTeamReq.getImage().get(i), postTeamReq.getImage().get(i).getMainImage(), teamIdx};
            this.jdbcTemplate.update(createTeamImageQuery, createTeamImageParams);
        }
        return teamIdx;
    }

    // 팀 수정
    public int updateTeam(int teamIdx, PatchTeamReq patchTeamReq){
        String updateTeamQuery = "UPDATE Team SET title = ?, content = ? WHERE teamIdx = ?";
        Object[] updateTeamParams = new Object[] {patchTeamReq.getTitle(), patchTeamReq.getContent(), teamIdx};
        return this.jdbcTemplate.update(updateTeamQuery,
                updateTeamParams);
    }

    // 팀 삭제 - PATCH
    public int deleteTeam(int teamIdx){
        String deleteTeamQuery = "UPDATE Team SET state = 0 WHERE teamIdx = ?";
        int deleteTeamParams = teamIdx;
        return this.jdbcTemplate.update(deleteTeamQuery,
                deleteTeamParams);
    }

    // 존재하는 유저인지 확인
    public int checkUserExist(int userIdx){
        String checkUserExistQuery = "select exists(select userIdx from User where userIdx = ?)";
        int checkUserExistParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);
    }

    // 존재하는 팀인지 확인
    public int checkTeamExist(int teamIdx){
        String checkTeamExistQuery = "select exists(select teamIdx from Team where teamIdx = ?)";
        int checkTeamExistParams = teamIdx;
        return this.jdbcTemplate.queryForObject(checkTeamExistQuery,
                int.class,
                checkTeamExistParams);
    }

}
