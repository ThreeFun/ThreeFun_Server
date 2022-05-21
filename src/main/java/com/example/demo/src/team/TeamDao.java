package com.example.demo.src.team;

import com.example.demo.src.team.model.*;
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

    @Transactional
    public List<GetTeamAll> getTeamAll(int userIdx, String regionName){
        String getTeamAllQuery = "select t.teamIdx timeIdx,\n" +
                "       gc.categoryName categoryName,\n" +
                "       t.title title,\n" +
                "       concat('총 상금 ', (format(t.price * t.personnel, 0)), '원') totalPrice,\n" +
                "       concat((format(t.price, 0)), '원') price,\n" +
                "       r.regionName regionName,\n" +
                "       concat(date_format(meetingDate, '%m'), '월', date_format(meetingDate, '%d'), '일 ', date_format(meetingTime, '%H:%m')) as date,\n" +
                "       ifnull(concat(numberOfPeople, '/', t.personnel, '명'), concat('0/', t.personnel, '명')) as nowPeople,\n" +
                "       ti.image image\n" +
                "from GameCategory gc, TeamImage ti, Region r, Team t\n" +
                "left join(\n" +
                "    select m.teamIdx, count(m.userIdx) as numberOfPeople\n" +
                "    from Member m\n" +
                "    group by teamIdx\n" +
                "    ) as x on x.teamIdx = t.teamIdx\n" +
                "where t.categoryIdx = gc.categoryIdx\n" +
                "and t.teamIdx = ti.teamIdx\n" +
                "and ti.mainImage = 1\n" +
                "and r.regionIdx = t.regionIdx\n" +
                "and r.state = 1\n" +
                "and t.state = 1\n" +
                "and gc.state = 1\n" +
                "and t.secret = 0\n" +
                "and r.regionName = ?;";
        String getFollowListQuery = "select t.teamIdx teamIdx,\n" +
                "       u.userName userName,\n" +
                "       u.userProfile userProfile,\n" +
                "       r.regionName regionName,\n" +
                "       gc.categoryName categoryName,\n" +
                "       ti.image image\n" +
                "from User u, Region r, Team t, TeamImage ti, GameCategory gc, Follow f\n" +
                "where t.regionIdx = r.regionIdx\n" +
                "and ti.mainImage = 1\n" +
                "and t.teamIdx = ti.teamIdx\n" +
                "and t.masterIdx = u.userIdx\n" +
                "and gc.categoryIdx = t.categoryIdx\n" +
                "and f.followeeIdx = u.userIdx\n" +
                "and f.followerIdx = ?\n" +
                "order by t.createAt desc\n" +
                "limit 1;";
        String getCurrentListQuery = "select t.teamIdx timeIdx,\n" +
                "       gc.categoryName categoryName,\n" +
                "       t.title title,\n" +
                "       concat('총 상금 ', (format(t.price * t.personnel, 0)), '원') totalPrice,\n" +
                "       concat((format(t.price, 0)), '원') price,\n" +
                "       r.regionName regionName,\n" +
                "       concat(date_format(meetingDate, '%m'), '월', date_format(meetingDate, '%d'), '일 ', date_format(meetingTime, '%H:%m')) as date,\n" +
                "       ifnull(concat(numberOfPeople, '/', t.personnel, '명'), concat('0/', t.personnel, '명')) as nowPeople,\n" +
                "       ti.image image\n" +
                "from GameCategory gc, TeamImage ti, Region r, Team t\n" +
                "left join(\n" +
                "    select m.teamIdx, count(m.userIdx) as numberOfPeople\n" +
                "    from Member m\n" +
                "    group by teamIdx\n" +
                "    ) as x on x.teamIdx = t.teamIdx\n" +
                "where t.categoryIdx = gc.categoryIdx\n" +
                "and t.teamIdx = ti.teamIdx\n" +
                "and ti.mainImage = 1\n" +
                "and r.regionIdx = t.regionIdx\n" +
                "and r.state = 1\n" +
                "and t.state = 1\n" +
                "and gc.state = 1\n" +
                "and t.secret = 0\n" +
                "order by t.createAt desc";

        int getUserParams = userIdx;
        String getTeamRegionParams = regionName;

        return this.jdbcTemplate.query(getTeamAllQuery,
                (rs, rowNum) -> new GetTeamAll(
                        rs.getInt("timeIdx"),
                        rs.getString("categoryName"),
                        rs.getString("title"),
                        rs.getString("totalPrice"),
                        rs.getString("price"),
                        rs.getString("regionName"),
                        rs.getString("date"),
                        rs.getString("nowPeople"),
                        rs.getString("image"),
                        this.jdbcTemplate.query(getFollowListQuery, (rs1, rowNum1) -> new GetFollowList(
                                rs1.getInt("teamIdx"),
                                rs1.getString("userName"),
                                rs1.getString("userProfile"),
                                rs1.getString("regionName"),
                                rs1.getString("categoryName"),
                                rs1.getString("image")
                        ), getUserParams),
                        this.jdbcTemplate.query(getCurrentListQuery, (rs2, rowNum2) -> new GetCurrentList(
                                rs2.getInt("timeIdx"),
                                rs2.getString("categoryName"),
                                rs2.getString("title"),
                                rs2.getString("totalPrice"),
                                rs2.getString("price"),
                                rs2.getString("regionName"),
                                rs2.getString("date"),
                                rs2.getString("nowPeople"),
                                rs2.getString("image")
                        ))
                ), getTeamRegionParams);
    }

}
