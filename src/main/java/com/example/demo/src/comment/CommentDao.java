package com.example.demo.src.comment;

import com.example.demo.src.comment.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class CommentDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createComment(PostComment postComment){
        String createCommentQuery = "insert into Comment (comment, teamIdx, userIdx) values (?, ?, ?);";
        Object[] createCommentParams = new Object[]{postComment.getComment(), postComment.getTeamIdx(), postComment.getUserIdx()};
        this.jdbcTemplate.update(createCommentQuery, createCommentParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int deleteComment(int commentIdx, int userIdx ){
        String deleteCommentQuery = "UPDATE Comment SET state = 0 WHERE commentIdx = ? and userIdx = ?";
        return this.jdbcTemplate.update(deleteCommentQuery, commentIdx, userIdx);
    }

    public int modifyComment(PostModifyComment postModifyComment){
        String modifyCommentQuery = "update Comment set comment = ? where commentIdx = ? and userIdx = ?";
        Object[] modifyCommentParams = new Object[]{postModifyComment.getComment(), postModifyComment.getCommentIdx(), postModifyComment.getUserIdx()};
        return this.jdbcTemplate.update(modifyCommentQuery, modifyCommentParams);
    }

}
