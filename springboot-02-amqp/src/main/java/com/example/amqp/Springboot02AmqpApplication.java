package com.example.amqp;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 自动配置
 * 1、RabbitAutoConfiguration
 * 2、有自动配置了连接工厂ConnectionFactory
 * 3、RabbitProperties封装了RabbitMQ的配置
 * 4、RabbitTemplate 给RabbitMQ发送和接受消息
 * 5、AmqpAdmin 系统管理组件
 *      创建和删除 Queue，ExChange，Binding
 * 6、@EnableRabbit + @RabbitListener 监听消息队列的内容
 */
@EnableRabbit   //开机基于注解的RabbiMQ
@SpringBootApplication
public class Springboot02AmqpApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot02AmqpApplication.class, args);
    }
}
