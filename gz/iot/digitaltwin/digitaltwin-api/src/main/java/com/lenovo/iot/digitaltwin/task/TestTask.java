package com.lenovo.iot.digitaltwin.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

/**
 * @Desc: 模拟通过WebSocket协议向服务端实时推送消息
 * @Name: com.lenovo.iot.digitaltwin.task.TestTask
 * @Author: chench9@lenovo.com
 * @Date: 2018/8/16
 */
@Component
public class TestTask {
    private Session session = null;
    private String uri = "ws://localhost:8400/dt/open/websocket/v1/instance/attribute";


    @Scheduled(fixedRate=5000)
    public void test() {
        // 定时向WebSocket服务器发送消息
        if(session == null) {
            WebSocketContainer container = null;
            try {
                container = ContainerProvider.getWebSocketContainer();
            } catch (Exception ex) {
                System.out.println("error" + ex);
            }
            try {
                URI r = URI.create(uri);
                session = container.connectToServer(MyWebSocketClient.class, r);
            } catch (DeploymentException | IOException e) {
                e.printStackTrace();
            }
        }

        String message = "now: " + new Date();
        System.out.println(message);
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
