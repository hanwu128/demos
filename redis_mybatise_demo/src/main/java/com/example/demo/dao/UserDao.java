package com.example.demo.dao;

import com.example.demo.model.User;

import java.util.List;

public interface UserDao {
    public List<User> getList();
    public User getById(long id);
    public Integer add(User user);
    public Integer update(User user);
    public Integer delete(List<Long> istIdList);
}
