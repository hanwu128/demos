package com.fegin.demo.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class SchedualServiceHiHystric implements SchedualServiceHi {
    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry "+name;
    }
}
