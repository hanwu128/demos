package com.hw.provider.send;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 消息发送者
 */
@Component
public class Sender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    //exchange：交换器名称
    @Value("${mq.config.exchange}")
    private String exchange;

    //routingkey:路由键
    @Value("${mq.config.queue.info.routing.key}")
    private String routingkey;

    /**
     * 发送消息方法
     *
     * @param msg
     */
    public void send(String msg) {
        //想消息队列发送消息
        //参数一：交换器名称
        //参数二：路由键
        //参数三：消息
        this.amqpTemplate.convertAndSend(this.exchange, this.routingkey, msg);
    }

}

