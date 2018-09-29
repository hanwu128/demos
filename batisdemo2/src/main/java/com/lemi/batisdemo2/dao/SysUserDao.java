package com.lemi.batisdemo2.dao;

import com.lemi.batisdemo2.entity.SysUser;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SysUserDao {

    @Autowired
    private SqlSession sqlSession;

    public SysUser findByName(String name){

        return sqlSession.selectOne("findByName",name);
    }

}
