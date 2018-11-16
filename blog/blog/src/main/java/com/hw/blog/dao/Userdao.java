package com.hw.blog.dao;

import com.hw.blog.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface Userdao {

    int getTotal(Map<String, Object> map);

    List<User> getList(Map<String, Object> map);

    User getById(Map<String, Object> map);

    Integer addUser(User user);

    Integer updateUser(User user);

    User getPasswordById(Map<String, Object> map);

    Integer updatePassword(Map<String, Object> map);

    Integer activateUser(String code);
}
