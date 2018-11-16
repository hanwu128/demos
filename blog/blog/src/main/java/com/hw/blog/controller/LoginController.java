package com.hw.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    /**
     * 登录
     *
     * @return
     */
    @RequestMapping("/login.html")
    public String login() {
        return "admin/login/login";
    }

    /**
     * 注册
     *
     * @return
     */
    @RequestMapping("/register.html")
    public String loginPage() {
        return "admin/register/register";
    }


}
