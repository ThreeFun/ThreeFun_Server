package com.example.demo.src.comment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.comment.CommentDao;
import com.example.demo.src.comment.CommentProvider;
import com.example.demo.src.comment.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class CommentService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CommentDao commentDao;
    private final CommentProvider commentProvider;
    private final JwtService jwtService;

    @Autowired
    public CommentService(CommentDao commentDao, CommentProvider commentProvider, JwtService jwtService) {
        this.commentDao = commentDao;
        this.commentProvider = commentProvider;
        this.jwtService = jwtService;
    }

    //POST
    public int createComment(PostComment postComment) throws BaseException {
        try{
            int commentIdx = commentDao.createComment(postComment);
            return commentIdx;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteComment(int commentIdx, int userIdx) throws BaseException {
        try{
            int result = commentDao.deleteComment(commentIdx, userIdx);
            if (result == 0) {
                throw new BaseException(BaseResponseStatus.MODIFY_FAIL_USERNAME); // 수정
            }
        }
        catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
