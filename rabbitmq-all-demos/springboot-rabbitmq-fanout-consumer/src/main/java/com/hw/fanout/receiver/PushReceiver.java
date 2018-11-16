package com.hw.fanout.receiver;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * error级别消息接收者
 *
 * @RabbitListener bindings:绑定队列
 * @QueueBinding value：绑定队列的名称
 * exchange:配置交换器
 * key:路由键
 * @Queue value:配置队列名称
 * autoDelete：是否是一个可删除的临时队列
 * @Exchange value:交换器名称
 * type:指定交换器类型
 */
@Component
@RabbitListener(
        bindings = @QueueBinding(
                value = @Queue(value = "${mq.config.queue.push}", autoDelete = "true"),
                exchange = @Exchange(value = "${mq.config.exchange}", type = ExchangeTypes.FANOUT)
        )
)
public class PushReceiver {

    /**
     * 接收消息方法，采用消息队列监听机制
     *
     * @param msg
     */
    @RabbitHandler
    public void process(String msg) {
        System.out.println("push.......receiver:" + msg);
    }

}
