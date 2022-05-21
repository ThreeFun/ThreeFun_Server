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
        if(postTeamReq.getImage() == null){
            return teamIdx;
        }

        String createMainImageQuery = "insert into TeamImage(image, mainImage, teamIdx) values (?, ?, ?)";
        Object[] createMainImageParams = new Object[]{postTeamReq.getImage().get(0), postTeamReq.getMainImage(), teamIdx};
        this.jdbcTemplate.update(createMainImageQuery, createMainImageParams);

        for (int i = 1; i < postTeamReq.getImage().size(); i++ ){
            // TeamImage table에 이미지 삽입
            String createTeamImageQuery = "insert into TeamImage(image, teamIdx) values (?, ?)";
            Object[] createTeamImageParams = new Object[]{postTeamReq.getImage().get(i), teamIdx};
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
    public List<GetTeamAll> getTeamAll(int userIdx){
        String getTestQuery = "select userIdx from User where userIdx = ?";
        String getRegionListQuery = "select t.teamIdx timeIdx,\n" +
                "       gc.categoryName categoryName,\n" +
                "       t.title title,\n" +
                "       (t.price * t.personnel) totalPrice,\n" +
                "       t.price price,\n" +
                "       r.regionName regionName,\n" +
                "       meetingTime,\n" +
                "       meetingDate,\n" +
                "       ifnull(numberOfPeople, 0) numberOfPeople,\n" +
                "       personnel,\n" +
                "       ti.image image\n" +
                "from GameCategory gc, User u, TeamImage ti, Region r, Team t\n" +
                "left join(\n" +
                "    select m.teamIdx, count(m.userIdx) as numberOfPeople\n" +
                "    from Member m\n" +
                "    group by teamIdx\n" +
                "    ) as x on x.teamIdx = t.teamIdx\n" +
                "where t.categoryIdx = gc.categoryIdx\n" +
                "and t.teamIdx = ti.teamIdx\n" +
                "and ti.mainImage = 1\n" +
                "and r.regionIdx = t.regionIdx\n" +
                "and u.regionIdx = r.regionIdx\n" +
                "and r.state = 1\n" +
                "and t.state = 1\n" +
                "and gc.state = 1\n" +
                "and t.secret = 0\n" +
                "and u.userIdx = ?;";
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
                "       (t.price * t.personnel) totalPrice,\n" +
                "       t.price price,\n" +
                "       r.regionName regionName,\n" +
                "       meetingTime,\n" +
                "       meetingDate,\n" +
                "       ifnull(numberOfPeople, 0) numberOfPeople,\n" +
                "       personnel,\n" +
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
                "order by t.createAt desc";

        int getUserParams = userIdx;

        return this.jdbcTemplate.query(getTestQuery,
                (rs, rowNum) -> new GetTeamAll(
                        this.jdbcTemplate.query(getRegionListQuery, (rs1, rowNum1) -> new GetRegionList(
                                rs1.getInt("timeIdx"),
                                rs1.getString("categoryName"),
                                rs1.getString("title"),
                                rs1.getInt("totalPrice"),
                                rs1.getInt("price"),
                                rs1.getString("regionName"),
                                rs1.getString("meetingTime"),
                                rs1.getString("meetingDate"),
                                rs1.getInt("numberOfPeople"),
                                rs1.getInt("personnel"),
                                rs1.getString("image")
                        ),  getUserParams),
                        this.jdbcTemplate.query(getFollowListQuery, (rs2, rowNum2) -> new GetFollowList(
                                rs2.getInt("teamIdx"),
                                rs2.getString("userName"),
                                rs2.getString("userProfile"),
                                rs2.getString("regionName"),
                                rs2.getString("categoryName"),
                                rs2.getString("image")
                        ), getUserParams),
                        this.jdbcTemplate.query(getCurrentListQuery, (rs3, rowNum3) -> new GetCurrentList(
                                rs3.getInt("timeIdx"),
                                rs3.getString("categoryName"),
                                rs3.getString("title"),
                                rs3.getInt("totalPrice"),
                                rs3.getInt("price"),
                                rs3.getString("regionName"),
                                rs3.getString("meetingTime"),
                                rs3.getString("meetingDate"),
                                rs3.getInt("numberOfPeople"),
                                rs3.getInt("personnel"),
                                rs3.getString("image")
                        ))
                ), getUserParams);
    }
}
