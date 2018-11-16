package com.hw.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MenuController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/home/console.html")
    public String console() {
        return "home/console";
    }

    @RequestMapping("/home/homepage1.html")
    public String homepage1() {
        return "home/homepage1";
    }

    @RequestMapping("/home/homepage2.html")
    public String homepage2() {
        return "home/homepage2";
    }

    @RequestMapping("/component/grid/list.html")
    public String componentGridList() {
        return "component/grid/list";
    }

    @RequestMapping("/component/grid/mobile.html")
    public String componentGridMobile() {
        return "component/grid/mobile";
    }

    @RequestMapping("/component/grid/mobile-pc.html")
    public String componentGridMobilePc() {
        return "component/grid/mobile-pc";
    }

    @RequestMapping("/component/grid/all.html")
    public String componentGridAll() {
        return "component/grid/all";
    }


    @RequestMapping("/component/grid/stack.html")
    public String componentGridStack() {
        return "component/grid/stack";
    }

    @RequestMapping("/component/grid/speed-dial.html")
    public String componentGridSpeedDial() {
        return "component/grid/speed-dial";
    }
}
