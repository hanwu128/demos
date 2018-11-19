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
import java.sql.Timestamp;
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

    @PostMapping("/list")
    public Object getList(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject params) {
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
            user.setRegistrationTime(new Date());
            user.setProfilePhone("aa");
            if (StringUtils.isEmpty(user.getNickname())) {
                user.setNickname(user.getName());
            }
            user.setAge(CommonUtil.getAge(user.getBirthday()));
            user.setCode(CommonUtil.getUUID());
            Integer rows = userService.addUser(user);
            if (rows == null && rows <= 0) {
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


    private User jsonToUser(JSONObject params) {
        User user = new User();

        Long id = params.getLong("id");
        String ip = params.getString("ip");
        String name = params.getString("name");
        String password = params.getString("password");
        String email = params.getString("email");
        String profilePhone = params.getString("profilePhone");
        String level = params.getString("level");
        String rights = params.getString("rights");
        Timestamp registrationTime = params.getTimestamp("registrationTime");
        String birthday = params.getString("birthday");
        Integer age = params.getInteger("age");
        Integer telephoneNumber = params.getInteger("telephoneNumber");
        String nickname = params.getString("nickname");

        user.setId(id);
        user.setIp(ip);
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setProfilePhone(profilePhone);
        user.setLevel(level);
        user.setRights(rights);
        user.setRegistrationTime(registrationTime);
        user.setBirthday(CommonUtil.longToString(CommonUtil.getTimestamp(birthday, "yyyy-MM-dd")));
        user.setAge(age);
        user.setTelephoneNumber(telephoneNumber);
        user.setNickname(nickname);
        return user;
    }

}