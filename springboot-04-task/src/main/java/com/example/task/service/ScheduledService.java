package com.example.task.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledService {

    @Scheduled(cron = "0/10 * * * * *")   //每十秒执行一次
    public void hello() {
        System.out.println("hello......");
    }
}
