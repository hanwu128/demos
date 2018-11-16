package com.hw.blog.service.impl;

import com.hw.blog.dao.Userdao;
import com.hw.blog.model.User;
import com.hw.blog.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private Userdao userdao;

    @Override
    public int getTotal(String name) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        return userdao.getTotal(map);
    }

    @Override
    public List<User> getList(int start, int offset, String like) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", start);
        map.put("offset", offset);
        map.put("like", like);
        return userdao.getList(map);
    }

    @Override
    public User getById(Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return userdao.getById(map);
    }

    @Override
    public Integer addUser(User user) {
        return userdao.addUser(user);
    }

    @Override
    public User getPasswordById(Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return userdao.getPasswordById(map);
    }

    @Override
    public Integer updateUser(User user) {
        return userdao.updateUser(user);
    }

    @Override
    public Integer activateUser(String code) {
        return userdao.activateUser(code);
    }
}
