package com.hw.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.hw.blog.util.JsonResp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    /**
     * 跳转到登陆
     *
     * @return
     */
    @RequestMapping(value = {"/", "/user/login.html"})
    public String userLogin() {
        return "user/login";
    }

    /**
     * 登录
     *
     * @return
     */
    @RequestMapping("/login.html")
    @ResponseBody
    public Object login(HttpServletRequest request, HttpServletResponse response, @RequestParam String username, @RequestParam String password, @RequestParam String vercode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("access_token", "c262e61cd13ad99fc650e6908c7e5e65b63d2f32185ecfed6b801ee3fbdd5c0a");
        return new JsonResp(0, "登入成功", map);
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
