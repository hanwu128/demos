package com.hw.security.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class Home {

    @RequestMapping(value = {"/", "/index"})
    //@PreAuthorize("hasRole('admin')")
    public String index(Model model) {

        model.addAttribute("key1", "我的Java世界Security!!!");
        return "index";
    }

    @RequestMapping(value = "/login")
    public String login() {

        return "login";
    }

    @RequestMapping(value = "/welcome")
    public String welcome() {

        return "welcome";
    }

    @RequestMapping(value = {"/news"})
    //@PreAuthorize("hasRole('user')")
    public String news(Model model) {

        return "news";
    }

    @RequestMapping(value = {"/myerror"})
    public String Myerror(Model model) {

        return "myerror";
    }


}
