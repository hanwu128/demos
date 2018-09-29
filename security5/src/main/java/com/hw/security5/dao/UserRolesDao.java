package com.hw.security5.dao;

import com.hw.security5.entity.UserRole;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRolesDao {

    private static final String USER_ROLES = "com.hw.mapper.UserRoles";

    public String getStatement(String id) {
        return USER_ROLES + "." + id;
    }

    @Autowired
    private SqlSession sqlSession;

    public List<UserRole> selectById(Integer id) {
        return this.sqlSession.selectList(getStatement("selectById"), id);
    }
}
