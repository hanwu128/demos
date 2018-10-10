package com.lenovo.iot.digitaltwin.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;

/**
 * @Desc: 用Java实现的WebSocket客户端
 * @Name: com.lenovo.iot.digitaltwin.task.MyWebSocketClient
 * @Author: chench9@lenovo.com
 * @Date: 2018/8/16
 */
@ClientEndpoint()
public class MyWebSocketClient {
    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketClient.class);
    private Session session = null;

    @OnOpen
    public void onOpen(Session session) {
        if(logger.isDebugEnabled()) {
            logger.debug("websocket session open");
        }
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        if(logger.isDebugEnabled()) {
            logger.debug("websocket receive message: {}", message);
        }
        System.out.println("Client onMessage: " + message);
    }

    @OnClose
    public void onClose() {
        if(logger.isDebugEnabled()) {
            logger.debug("websocket session closed: {}", this.session);
        }
        try {
            this.session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
