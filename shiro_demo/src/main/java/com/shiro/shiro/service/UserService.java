package com.shiro.shiro.service;

import com.shiro.shiro.entity.User;

public interface UserService {
    public User findUserByUserName(String username);
}
