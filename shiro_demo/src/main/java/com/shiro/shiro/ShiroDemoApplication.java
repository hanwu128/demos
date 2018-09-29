package com.shiro.shiro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.shiro.shiro.mapper")
public class ShiroDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroDemoApplication.class, args);
    }
}
