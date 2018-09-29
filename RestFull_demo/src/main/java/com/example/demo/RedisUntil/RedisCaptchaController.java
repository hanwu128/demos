package com.example.demo.RedisUntil;

public class RedisCaptchaController {

    private  RedisTemplate<String,String> redisTemplate;

    private static int captchaExpires = 3*60;//超时时间3min，验证码超时，自动从redis中删除
    private static int captchaW = 200;
    private static int captchaH = 60;
    private static  String cookieName = "CaptchaCode";


}
