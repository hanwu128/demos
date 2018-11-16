package com.hw.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableScheduling                   // 启用定时任务
//@EnableWebSocket                    // 支持WebSocket
@EnableTransactionManagement        // 声明式事物管理
@ServletComponentScan   //自动扫描
@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
}
