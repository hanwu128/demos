package com.hw.fanout.send;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class Sender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    //exchange：交换器名称
    @Value("${mq.config.exchange}")
    private String exchange;

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
        this.amqpTemplate.convertAndSend(this.exchange, "", msg);
    }

}
