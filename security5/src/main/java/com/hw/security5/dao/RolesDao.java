package com.hw.security5.dao;

import com.hw.security5.entity.Roles;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RolesDao {

    private static final String ROLES = "com.hw.mapper.Roles";

    public String getStatement(String id) {
        return ROLES + "." + id;
    }

    @Autowired
    private SqlSession sqlSession;

    public Roles findByRoleName(String rolename) {

        return sqlSession.selectOne(getStatement("findByName"), rolename);
    }

    public Roles selectById(Integer id) {
        return this.sqlSession.selectOne(getStatement("selectById"), id);
    }
}
