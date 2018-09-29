package com.hw.security.dao;

import com.hw.security.entity.SysUser;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SysUserDao {

    private static final String SYS_USER = "com.hw.mapper.SysUser";

    public String getStatement(String id) {
        return SYS_USER + "." + id;
    }

    @Autowired
    private SqlSession sqlSession;

    public SysUser findByName(String username) {

        return sqlSession.selectOne(getStatement("findByName"), username);
    }

}
