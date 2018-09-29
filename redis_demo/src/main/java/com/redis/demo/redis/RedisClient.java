package com.redis.demo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 功能描述：redis工具类
 * 对于redisTpl.opsForValue().set(key, value)进行了一次封装，不然每次都要这样保存值
 * 而封装后只需：new RedisClient().set(key,value);
 */
@Component
public class RedisClient {

    private static final long EXPIRE_TIME_IN_MINUTES = 1; // redis过期时间

    @Autowired
    private StringRedisTemplate redisTpl; //jdbcTemplate

    // 功能描述：设置key-value到redis中
    public boolean set(String key ,String value){
        try{
            redisTpl.opsForValue().set(key, value,EXPIRE_TIME_IN_MINUTES,TimeUnit.MINUTES);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // 功能描述：通过key获取缓存里面的值
    public String get(String key){
        return redisTpl.opsForValue().get(key);
    }
}
