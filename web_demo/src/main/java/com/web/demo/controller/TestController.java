package com.web.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Controller
public class TestController {

    @GetMapping("/test")
    public String test(ModelMap map) {
        map.put("name", "three");
        map.put("date", new Date());
        return "test";
    }
}
