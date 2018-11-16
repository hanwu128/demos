package com.hw.config.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息接收者
 */
@Component
public class Receiver {

    /**
     * 接收消息方法，采用消息队列监听机制
     *
     * @param msg
     */
    @RabbitListener(queues = "hello queue")
    public void process(String msg) {
        System.out.println("receiver:" + msg);
    }

}
