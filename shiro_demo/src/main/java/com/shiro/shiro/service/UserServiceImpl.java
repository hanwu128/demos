package com.shiro.shiro.service;

import com.shiro.shiro.entity.User;
import com.shiro.shiro.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserByUserName(String username) {
        return userMapper.findByUserName(username);
    }
}
