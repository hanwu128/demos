package com.hw.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecurityRolePerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityRolePerApplication.class, args);
    }
}
