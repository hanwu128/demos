package com.hw.shiro.controller;

import com.hw.shiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/aaa")
    public String testThymeleaf(Model model) {
        model.addAttribute("name", "aasdfasdfasdd");
        return "test";

    }

    @RequestMapping("/add")
    public String add() {
        return "user/add";

    }

    @RequestMapping("/update")
    public String update() {
        return "user/update";

    }

    @RequestMapping("/tologin")
    public String tologin() {
        return "login";

    }

    @RequestMapping("/unauth")
    public String unauth() {
        return "unauth";

    }

    @RequestMapping("/login")
    public String login(String username, String password, Model model) {

        //使用Shiro编写认证操作
        //1、获取Subject
        Subject subject = SecurityUtils.getSubject();

        //2、封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            //3、执行登陆
            subject.login(token);

            //没有异常验证成功
            return "test";
        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户名不存在！");
            return "login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码错误！");
            return "login";
        }

    }

}
