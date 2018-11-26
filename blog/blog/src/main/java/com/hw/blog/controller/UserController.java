package com.hw.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.hw.blog.model.User;
import com.hw.blog.service.MessagesService;
import com.hw.blog.service.UserService;
import com.hw.blog.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 用户操作
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    MessagesService messagesService;

    @GetMapping("/list")
    public Object getList(HttpServletRequest request, HttpServletResponse response) {
        try {

            List<User> userList = userService.getList(0, 10, "%");
            return new JsonResp(0, "success", "100", userList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResp.httpCode(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR).errorResp(messagesService.getMessage("error", new Object[]{"query user is error "}));
    }

    @GetMapping("/user/{id}")
    public Object getUser(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long id) {
        User user = userService.getById(id);
        return new JsonResp(0, "success", user);
    }

    @GetMapping("/update")
    public Object updateUser(HttpServletRequest request, HttpServletResponse response, User user) {
        userService.updateUser(user);
        return new JsonResp(0, "success", user);
    }

    @PostMapping("/list2")
    public Object getList2(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject params) {
        try {
            int page = params.getInteger("page");
            int offset = params.getInteger("offset");
            String name = params.getString("name");
            if (page <= 0) page = 1;
            if (offset <= 0) offset = 10;
            int total = userService.getTotal(PageUtil.like(name));
            int pageTotal = PageUtil.pageTotal(total, offset);
            int start = PageUtil.getStart(page, offset);
            List<User> list = userService.getList(start, offset, PageUtil.like(name));
            if (list == null || list.isEmpty()) {
                return JsonResp.httpCode(response, HttpServletResponse.SC_NOT_FOUND).errorResp(messagesService.getMessage("not.found", new Object[]{"query user is null "}));
            }
            return JsonResp.httpCode(response, HttpServletResponse.SC_OK).successResp(new JsonList(total, pageTotal, page, offset, list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResp.httpCode(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR).errorResp(messagesService.getMessage("error", new Object[]{"query user is error "}));
    }

    @GetMapping("/{id}")
    public Object getById(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id) {
        User user = userService.getById(id);
        return user;
    }

    @PostMapping(value = "/register")
    public Object register(HttpServletRequest request, HttpServletResponse response, User user) {
        try {
            user.setIp(GetIpUtil.getIpAddr(request));
            user.setRegistTime(new Date().getTime());
            user.setPhoto("aa");
            if (StringUtils.isEmpty(user.getNickname())) {
                user.setNickname(user.getName());
            }
            user.setAge(CommonUtil.getAge(user.getBirth()));
            user.setCode(CommonUtil.getUUID());
            Integer rows = userService.addUser(user);
            if (rows == null || rows <= 0) {
                return JsonResp.httpCode(response, HttpServletResponse.SC_BAD_REQUEST).errorResp(messagesService.getMessage("parameter.error", new Object[]{"register user "}));
            }
            response.sendRedirect("/email/send/" + user.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResp.httpCode(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR).errorResp(messagesService.getMessage("error", new Object[]{"register user is error "}));
    }

    @GetMapping("/activate/{code}")
    public void activate(HttpServletRequest request, HttpServletResponse response, @PathVariable String code) {
        try {
            Integer rows = userService.activateUser(code);
            response.sendRedirect("/login.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
