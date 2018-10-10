package com.lenovo.iot.digitaltwin.controller.front;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Desc: 用户登录登出
 * Name: com.lenovo.iot.digitaltwin.controller.front.LoginController
 * Author: chench9@lenovo.com
 * Date: 2018/6/5 10:28
 **/
@Controller
@RequestMapping("/subject")
public class SubjectController {

    /**
     * 用户登录
     * @param req
     * @param resp
     * @return
     */
    @PostMapping("/login.do")
    @ResponseBody
    public Object login(HttpServletRequest req, HttpServletResponse resp,
                        @RequestBody JSONObject params) {
        //TODO
        return "Login Success!";
    }

}
