package com.hw.blog.controller;

import com.hw.blog.model.User;
import com.hw.blog.service.MessagesService;
import com.hw.blog.service.UserService;
import com.hw.blog.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
    private MessagesService messagesService;

    @RequestMapping("/list")
    public Object getList(HttpServletRequest request, HttpServletResponse response, User user, @RequestParam(value = "page") Integer page, @RequestParam(value = "limit") Integer limit) {
        try {
            if (page == null || page <= 0) page = 1;
            if (limit == null || limit <= 0) limit = 10;
            Integer total = userService.getTotal(PageUtil.like(user.getName()), user.getActivate());
            Integer start = PageUtil.getStart(page, limit);
            List<User> userList = userService.getList(start, limit, PageUtil.like(user.getName()), user.getActivate());
            return new JsonResp(total.toString(), userList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResp.httpCode(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR).errorResp(messagesService.getMessage("error", new Object[]{"query user is error "}));
    }

    @GetMapping("/add")
    public Object addUser(HttpServletRequest request, HttpServletResponse response, User user) {
        user.setIp(GetIpUtil.getIpAddr(request));
        if (StringUtils.isEmpty(user.getNickname())) {
            user.setNickname(user.getName());
        }
        user.setAge(CommonUtil.getAge(user.getBirth()));
        user.setCode(CommonUtil.getUUID());
        Integer rows = userService.addUser(user);
        return new JsonResp(user);
    }

    @GetMapping("/user/{id}")
    public Object getUser(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long id) {
        User user = userService.getById(id);
        return new JsonResp(user);
    }

    @GetMapping("/update")
    public Object updateUser(HttpServletRequest request, HttpServletResponse response, User user) {
        userService.updateUser(user);
        return new JsonResp(user);
    }

    @GetMapping("/verify/password/{password}")
    public Object verifyPassword(HttpServletRequest request, HttpServletResponse response, @PathVariable("password") String password) {
        //TODO 密码校验
        /*User user = userService.getPassWordById(id);
        if (!StringUtils.isEmpty(password) && user != null && !StringUtils.isEmpty(user.getPassword())) {
            if (user.getPassword().equals(password)) {
                return new JsonResp(0, "success", password);
            }
        }*/
        //return new JsonResp(1001, "failure", password);
        return new JsonResp(password);
    }

    @GetMapping("/del/{id}")
    public Object deltUser(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) {
        if (!StringUtils.isEmpty(id)) {
            String[] idArr = id.split(",");
            List<String> ids = Arrays.asList(idArr);
            Integer rows = userService.delUser(ids);
            return new JsonResp(null);
        }
        return new JsonResp(1001, "failure", null);
    }

    @GetMapping("/{id}")
    public Object getById(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id) {
        User user = userService.getById(id);
        return user;
    }

    @PostMapping("/upload/photo")
    public Object uploadPhoto(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            String oldName = file.getOriginalFilename();
            //获取跟目录
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!path.exists()) path = new File("");
            //获取上传目录路径
            File upload = new File(path.getAbsolutePath(), "static/images/upload/");
            if (!upload.exists()) upload.mkdirs();
            String fileName = getPhotoName(oldName);
            String rappendix = "/images/upload/" + "/" + fileName;
            fileName = upload + "/" + fileName;
            File file1 = new File(fileName);
            file.transferTo(file1);
            return new JsonResp(rappendix);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonResp(1001, "failure", null);
    }

    private static String getPhotoName(String oldName) {
        Random r = new Random();
        Date d = new Date();
        String newName = oldName.substring(oldName.indexOf('.'));
        newName = r.nextInt(99999999) + d.getTime() + newName;
        return newName;
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
