package com.lenovo.iot.digitaltwin.controller.open;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Desc: 通过WebSocket协议支持流式方式读取数据，本质上就是实时推送技术
 * @Name: com.lenovo.iot.digitaltwin.controller.open.DigitalTwinInstanceAttrStreamController
 * @Author: chench9@lenovo.com
 * @Date: 2018/8/16
 */
public class DigitalTwinInstanceAttrStreamController extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(DigitalTwinInstanceAttrStreamController.class);
    private static final Map<String, WebSocketSession> sessionMap = new HashMap<String, WebSocketSession>();

    /**
     * 建立连接
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if(logger.isDebugEnabled()) {
            logger.debug("websocket session established: {}", session);
        }
        super.afterConnectionEstablished(session);
        sessionMap.put(session.getId(), session);
        broadcastMessage("hello, " + session.getId());
    }

    /**
     * 接收数据
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        broadcastMessage(message.getPayload());
    }

    /**
     * 关闭连接
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if(logger.isDebugEnabled()) {
            logger.debug("websocket session closed: {}", session);
        }
        super.afterConnectionClosed(session, status);
        session.close();
        sessionMap.remove(session.getId());
    }

    /**
     * 广播消息
     * @param message
     */
    private void broadcastMessage(String message) {
        Iterator<Map.Entry<String, WebSocketSession>> iterator = sessionMap.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, WebSocketSession> entry = iterator.next();
            WebSocketSession session = entry.getValue();
            if(session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
