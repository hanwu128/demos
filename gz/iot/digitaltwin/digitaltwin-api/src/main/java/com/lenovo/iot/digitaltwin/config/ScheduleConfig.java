package com.lenovo.iot.digitaltwin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.util.concurrent.Executors;

/**
 * @Desc: 定时任务配置
 * @Name: com.lenovo.iot.digitaltwin.config.ScheduleConfig
 * @Author: chench9@lenovo.com
 * @Date: 2018/8/16
 */
@Configuration
public class ScheduleConfig {
    /**
     * 解决同时启用@EnableScheduling和@EnableWebSocket无法启动程序的问题
     * @return
     */
    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor());
    }
}
