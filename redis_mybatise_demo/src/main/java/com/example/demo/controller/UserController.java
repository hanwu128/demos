package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
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

}
