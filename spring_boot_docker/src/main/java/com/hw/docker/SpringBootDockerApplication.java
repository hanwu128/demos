package com.hw.docker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@SpringBootApplication
@RestController
public class SpringBootDockerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDockerApplication.class, args);
    }

    @GetMapping("/")
    public String index() {
        return "你好,docker,现在时间是："+new Date().getTime();
    }

    @GetMapping("/hh")
    public String hh() {
        return "更新中......"+new Date().getTime();
    }
}
