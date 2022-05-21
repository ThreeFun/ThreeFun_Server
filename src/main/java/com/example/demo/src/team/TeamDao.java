package com.example.demo.src.team;

import com.example.demo.src.team.model.PostTeamImageReq;
import com.example.demo.src.team.model.PostTeamReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.transaction.Transactional;

@Repository
public class TeamDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 팀 생성 + 팀 이미지 삽입
    @Transactional
    public int insertTeam(int userIdx, PostTeamReq postTeamReq){

        String insertTeamQuery = "INSERT INTO Team(content, title, meetingTime, meetingDate, regionIdx, price, personnel, entryFee, allAgree, payment, teamPassword, secret, masterIdx, categoryIdx) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] insertTeamParams = new Object[] {postTeamReq.getContent(), postTeamReq.getTitle(), postTeamReq.getMeetingTime(), postTeamReq.getMeetingDate(), postTeamReq.getRegionIdx(), postTeamReq.getPrice(), postTeamReq.getPersonnel(), postTeamReq.getEntryFee(), postTeamReq.getAllAgree(), postTeamReq.getPayment(), postTeamReq.getTeamPassword(), postTeamReq.getSecret(), postTeamReq.getMasterIdx(), postTeamReq.getCategoryIdx()};
        this.jdbcTemplate.update(insertTeamQuery,
                insertTeamParams);

        // 방금 넣은 team의 idx 값을 다시 클라이언트에게 전달 (return)
        String lastInsertIdxQuery = "SELECT last_insert_id()";
        int teamIdx = this.jdbcTemplate.queryForObject(lastInsertIdxQuery,int.class);

        // 팀 이미지 삽입
        if(postTeamReq.getImages() == null){
            return teamIdx;
        }

        for (int i = 0; i < postTeamReq.getImages().size(); i++ ){
            // TeamImage table에 이미지 삽입
            String createTeamImageQuery = "insert into TeamImage(image, teamIdx, mainImage) values (?, ?, ?)";
            Object[] createTeamImageParams = new Object[]{postTeamReq.getImages().get(i), teamIdx, postTeamReq.getMainImage()};
            this.jdbcTemplate.update(createTeamImageQuery, createTeamImageParams);
        }
        return teamIdx;
    }

}
