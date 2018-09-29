package com.hw.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages ="com.hw.security.dao")
@SpringBootApplication
public class Security52Application {

    public static void main(String[] args) {
        SpringApplication.run(Security52Application.class, args);
    }
}
