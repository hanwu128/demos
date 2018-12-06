package com.hw.shiro.mapper;

import com.hw.shiro.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {

    public User findByName(String name);

    public User findById(Integer id);
}
