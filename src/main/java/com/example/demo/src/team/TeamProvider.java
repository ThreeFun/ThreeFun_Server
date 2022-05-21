package com.example.demo.src.team;

import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
