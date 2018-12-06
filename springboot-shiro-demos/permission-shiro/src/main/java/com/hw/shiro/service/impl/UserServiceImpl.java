package com.hw.shiro.service.impl;

import com.hw.shiro.mapper.UserDao;
import com.hw.shiro.model.User;
import com.hw.shiro.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User findByName(String name) {
        return userDao.findByName(name);
    }

    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }
}
