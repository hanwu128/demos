package com.lenovo.iot.digitaltwin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

//@EnableScheduling                   // 启用定时任务
@EnableWebSocket                    // 支持WebSocket
@EnableTransactionManagement		// 声明式事物管理
@ServletComponentScan               // 自动扫描过滤器
@SpringBootApplication
public class DigitaltwinApplication {
	private static final Logger logger = LoggerFactory.getLogger(DigitaltwinApplication.class);

	public static void main(String[] args) {
		String logDirKey = "log.dir";
		String logDir = System.getProperty(logDirKey);
		if(logDir == null || "".equals(logDir.trim())) {
			logDir = ".";
		}
		System.setProperty(logDirKey, logDir);
		logger.info("log.dir: {}", logDir);
		SpringApplication.run(DigitaltwinApplication.class, args);
		logger.info("started.");
	}
}
