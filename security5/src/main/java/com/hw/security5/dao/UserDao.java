package com.hw.security5.dao;


import com.hw.security5.entity.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDao {

    private static final String USER = "com.hw.mapper.User";

    public String getStatement(String id) {
        return USER + "." + id;
    }

    @Autowired
    private SqlSession sqlSession;

    public User selectById(long id) {
        return this.sqlSession.selectOne(getStatement("selectById"), id); //sqlSession.selectOne(String,gvr)
    }


}
