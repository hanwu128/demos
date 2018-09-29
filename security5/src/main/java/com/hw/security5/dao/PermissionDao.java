package com.hw.security5.dao;

import com.hw.security5.entity.Permission;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionDao {


    private static final String PERMSSION = "com.hw.mapper.Permission";

    public String getStatement(String id) {
        return PERMSSION + "." + id;
    }

    @Autowired
    private SqlSession sqlSession;

    public List<Permission> findByRoleId(Integer rolesId) {

        return sqlSession.selectList(getStatement("findByRoleId"), rolesId);
    }

}
