package com.hw.security.dao;

import com.hw.security.entity.SysRole;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SysRoleDao {

    private static final String ROLES = "com.hw.mapper.Roles";

    public String getStatement(String id) {
        return ROLES + "." + id;
    }

    @Autowired
    private SqlSession sqlSession;

    public SysRole findByRoleName(String rolename) {

        return sqlSession.selectOne(getStatement("findByRoleName"), rolename);
    }

    public SysRole selectById(Integer id) {
        return this.sqlSession.selectOne(getStatement("selectById"), id);
    }

    public SysRole findByName(String rolename) {

        return sqlSession.selectOne(getStatement("findByName"), rolename);
    }
}
