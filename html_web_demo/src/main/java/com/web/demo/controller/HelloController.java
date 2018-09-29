package com.web.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
public class HelloController {

    @Value("${userName}")
    private String userName;

    @Value("${bookTitle}")
    private String bookTitle;

    @RequestMapping("/")
    public String index(ModelMap map) {
//        map.put("name", userName);
//        map.put("bookTitle", bookTitle);
//        map.put("data", new Date());
        return "index";
    }
}