package com.lenovo.iot.digitaltwin.config;

import com.lenovo.iot.digitaltwin.controller.open.DigitalTwinInstanceAttrStreamController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @Desc: 配置WebSocket
 * @Name: com.lenovo.iot.digitaltwin.config.WebSocketConfig
 * @Author: chench9@lenovo.com
 * @Date: 2018/8/16
 */
//@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(dtAttrStreamHandler(), "/dt/open/websocket/v1/instance/attribute");
    }

    @Bean
    public WebSocketHandler dtAttrStreamHandler() {
        return new DigitalTwinInstanceAttrStreamController();
    }
}
