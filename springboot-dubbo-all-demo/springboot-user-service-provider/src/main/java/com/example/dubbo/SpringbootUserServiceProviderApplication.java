package com.example.dubbo;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo  //开启基于注解的dubbo功能
@SpringBootApplication
public class SpringbootUserServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootUserServiceProviderApplication.class, args);
    }
}