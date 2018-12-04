package com.hw.blog.controller;

import com.hw.blog.service.MessagesService;
import com.hw.blog.util.JsonResp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 错误信息处理
 */
@Controller
public class ErrorController {

    @Resource
    private MessagesService messagesService;

    @RequestMapping("/timeout.html")
    @ResponseBody
    public Object timeout(HttpServletRequest request, HttpServletResponse response) {
        return JsonResp.httpCode(response, HttpServletResponse.SC_UNAUTHORIZED).errorResp(messagesService.getMessage("timeout.error", new Object[]{"登陆超时，请重新登陆！"}));
    }

}
