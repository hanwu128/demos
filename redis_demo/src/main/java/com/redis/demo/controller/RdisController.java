package com.redis.demo.controller;

import com.redis.demo.model.User;
import com.redis.demo.redis.RedisClient;
import com.redis.demo.until.JsonData;
import com.redis.demo.until.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/redis")
public class RdisController {


    //得到redis封装类
    @Autowired
    private RedisClient redis;

    //添加字符串
    @GetMapping(value="add")
    public Object add(){

        redis.set("username", "xddddddd");
        return JsonData.buildSuccess();

    }

    //通过key值得到value字符串
    @GetMapping(value="get")
    public Object get(){

        String value = redis.get("username");
        return JsonData.buildSuccess(value);

    }

    //将对象通过工具类转成String类型，存入redis中
    @GetMapping(value="save_user")
    public Object saveUser(){
        User user = new User("lisi", 12, '男', "北京市昌平区");
        String userStr = JsonUtil.obj2String(user);
        boolean flag = redis.set("base:user:22", userStr);
        return JsonData.buildSuccess(flag);

    }

    //通过key值得到value值，让后将value转为对象
    @GetMapping(value="find_user")
    public Object findUser(){

        String userStr = redis.get("base:user:22");
        User user = JsonUtil.string2Obj(userStr, User.class);
        return JsonData.buildSuccess(user);

    }
}
