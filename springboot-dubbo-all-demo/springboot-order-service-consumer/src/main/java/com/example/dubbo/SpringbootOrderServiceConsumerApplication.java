package com.example.dubbo;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * springboot 与dubbo 整合的三种方式
 * 1、导入dubbo-starter，在application.properties配偶属性，使用@Service暴露服务，使用@Reference引用服务
 * 2、保留dubbo xml配置文件,导入dubbo-starter，使用@ImportResource(locations = "classpath:provider.xml")引入配置文件
 * 3、使用注解API方式，将每一个组件手动创建到spring容器中
 */
@EnableDubbo
@SpringBootApplication
public class SpringbootOrderServiceConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootOrderServiceConsumerApplication.class, args);
    }
}
