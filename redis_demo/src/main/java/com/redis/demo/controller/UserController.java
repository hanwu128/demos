package com.redis.demo.controller;

import com.redis.demo.model.User;
import com.redis.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("list")
    public List<User> getList() {
        List<User> list = userService.getUserList();
        return list;
    }

    @RequestMapping("getById")
    public User getById() {

        return userService.getById(1);

    }

    @RequestMapping("/save")
    public void save(){
        userService.addUser(new User("赵六",24,'女',"北京市海淀区"));

    }

}
