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
}
