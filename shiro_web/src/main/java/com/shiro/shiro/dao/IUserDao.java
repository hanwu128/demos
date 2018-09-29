package com.shiro.shiro.dao;

import com.shiro.shiro.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Mapper
@Service
public interface IUserDao {

    @Insert("select * from t_user")
    public List<User> getList();

    @Insert("select * from t_user where username = #{username} ")
    public User findByName(String username);
}
