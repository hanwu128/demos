package com.shiro.shiro.mapper;

import com.shiro.shiro.entity.User;

public interface UserMapper {
    public User findByUserName(String username);
}
