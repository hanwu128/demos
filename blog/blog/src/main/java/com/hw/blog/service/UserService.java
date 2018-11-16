package com.hw.blog.service;

import com.hw.blog.model.User;

import java.util.List;

public interface UserService {

    int getTotal(String name);

    List<User> getList(int start, int offset, String like);

    User getById(Long id);

    Integer addUser(User user);

    User getPasswordById(Long id);

    Integer updateUser(User user);

    Integer activateUser(String code);
}
