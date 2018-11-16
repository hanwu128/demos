package com.hw.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 菜单操作
 */
@Controller
public class MenuController {

    private static final String PAGE = "admin/index/";

    /**
     * 主页
     *
     * @return
     */
    @RequestMapping("/index.html")
    public String index() {
        return PAGE + "index";
    }

    /**
     * 后台菜单
     *
     * @return
     */
    @RequestMapping("/menu1.html")
    public String menu1() {
        return PAGE + "menu1";
    }


    /**
     * 前台菜单
     *
     * @return
     */
    @RequestMapping("/menu2.html")
    public String menu2() {
        return PAGE + "menu2";
    }

    /**
     * 文章管理
     *
     * @return
     */
    @RequestMapping("/article.html")
    public String article() {
        return PAGE + "article-list";
    }

    /**
     * 但也管理
     *
     * @return
     */
    @RequestMapping("/danye.html")
    public String danye() {
        return PAGE + "danye-list";
    }

    /**
     * 邮件管理
     *
     * @return
     */
    @RequestMapping("/email.html")
    public String email() {
        return PAGE + "email";
    }

    /**
     * 写邮件
     *
     * @return
     */
    @RequestMapping("/email/write.html")
    public String emailWrite() {
        return PAGE + "email-write";
    }

    /**
     * 个人信息
     *
     * @return
     */
    @RequestMapping("/info.html")
    public String info() {
        return PAGE + "admin-info";
    }

    /**
     * 系统设置
     *
     * @return
     */
    @RequestMapping("/system.html")
    public String system() {
        return PAGE + "system";
    }


    /**
     * 欢迎页
     *
     * @return
     */
    @RequestMapping("/welcome.html")
    public String welcome() {
        return PAGE + "welcome";
    }

}
