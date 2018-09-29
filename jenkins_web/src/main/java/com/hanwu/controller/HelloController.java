package com.hanwu.controller;

import com.hanwu.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String hello(User user, ModelMap map) {
        map.put("date", new Date());
        map.put("username", "张三");
        return "hello";
    }
}
