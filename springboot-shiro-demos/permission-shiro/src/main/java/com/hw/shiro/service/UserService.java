package com.hw.shiro.service;

import com.hw.shiro.model.User;

public interface UserService {

    public User findByName(String name);

    public User findById(Integer id);

}
