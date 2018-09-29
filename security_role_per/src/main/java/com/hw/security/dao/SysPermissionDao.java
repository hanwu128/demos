package com.hw.security.dao;

import com.hw.security.entity.Syspermission;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SysPermissionDao {

    private static final String PERMSSION = "com.hw.mapper.Permission";

    public String getStatement(String id) {
        return PERMSSION + "." + id;
    }

    @Autowired
    private SqlSession sqlSession;

    public List<Syspermission> findByName(String name) {

        return sqlSession.selectList(getStatement("findByName"), name);
    }

}
