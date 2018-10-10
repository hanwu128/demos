package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 1、将服务提供者注册到注册中心
 *      1）引入dubbo和zkclient相关依赖
 *      2）配置dubbo的扫描包和注册中心地址
 *      3）使用@Server发布服务
 */
@SpringBootApplication
public class Springboot06DubboTicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot06DubboTicketApplication.class, args);
    }
}
