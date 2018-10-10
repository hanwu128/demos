package com.hw;

import com.hw.service.OrderService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class MainApplication {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
        context.start();
        OrderService orderService = context.getBean(OrderService.class); // 获取远程服务代理
        orderService.initOrder("1"); // 执行远程方法
        System.in.read();
    }

}
