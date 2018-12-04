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

    /**
     * token过期
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/timeout.html")
    @ResponseBody
    public Object timeout(HttpServletRequest request, HttpServletResponse response) {
        return JsonResp.httpCode(response, HttpServletResponse.SC_UNAUTHORIZED).errorResp(messagesService.getMessage("timeout.error", new Object[]{"登陆超时，请重新登陆！"}));
    }

    /**
     * token为空
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/tokennull.html")
    @ResponseBody
    public Object tokennull(HttpServletRequest request, HttpServletResponse response) {
        return JsonResp.httpCode(response, HttpServletResponse.SC_BAD_REQUEST).errorResp(messagesService.getMessage("token.null.error", new Object[]{"请求失败，Token空值，请重新登陆！"}));
    }

    /**
     * token无效
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/tokenfailure.html")
    @ResponseBody
    public Object tokenfailure(HttpServletRequest request, HttpServletResponse response) {
        return JsonResp.httpCode(response, HttpServletResponse.SC_BAD_REQUEST).errorResp(messagesService.getMessage("token.failure.error", new Object[]{"请求失败，Token无效，请重新登陆！"}));
    }

    /**
     * 系统错误
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/servererror.html")
    @ResponseBody
    public Object servererror(HttpServletRequest request, HttpServletResponse response) {
        return JsonResp.httpCode(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR).errorResp(messagesService.getMessage("error", new Object[]{"请求失败，系统错诶，请重新登陆！"}));
    }

}
