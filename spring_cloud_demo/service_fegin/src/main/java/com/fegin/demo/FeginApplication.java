package com.fegin.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrixDashboard
@SpringBootApplication
public class FeginApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeginApplication.class, args);
    }
}
