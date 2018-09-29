package com.hw.security.dao;

import com.hw.security.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;


public interface UserDao {
    public SysUser findByUserName(String username);
}
