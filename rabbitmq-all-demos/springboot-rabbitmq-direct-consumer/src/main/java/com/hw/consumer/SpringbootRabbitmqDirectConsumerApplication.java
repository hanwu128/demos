package com.hw.consumer;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class SpringbootRabbitmqDirectConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRabbitmqDirectConsumerApplication.class, args);
    }
}
