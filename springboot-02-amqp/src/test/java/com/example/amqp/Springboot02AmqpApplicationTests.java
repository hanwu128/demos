package com.example.amqp;

import com.example.amqp.bean.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot02AmqpApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AmqpAdmin amqpAdmin;

    /**
     * 创建Exchange
     */
    @Test
    public void createExchange() {
        //创建Exchange
        //amqpAdmin.declareExchange(new DirectExchange("amqpadmin.exchange"));

        //创建Queue
        //amqpAdmin.declareQueue(new Queue("amqpadmin.queue", true));

        //创建绑定Exchange
        amqpAdmin.declareBinding(new Binding("amqpadmin.queue", Binding.DestinationType.QUEUE, "amqpadmin.exchange", "amqpadmin.aaa", null));
        System.out.println("创建完成");
    }

    /**
     * 单播（点对点）
     */
    @Test
    public void contextLoads() {
        //Message需要自己构造一个，定义消息体内容和消息头
        //rabbitTemplate.send(exchange,routeKey,message);

        //object默认当成消息体，只需要传入要发送的对象，自动序列化发送给rabbitmq
        //rabbitTemplate.convertAndSend(exchange,routeKey,object);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "这是一个测试消息");
        map.put("data", Arrays.asList("helloword", 123, true));
        //对象默认序列化后发出去，默认采用java的jdk序列化
        rabbitTemplate.convertAndSend("exchange.direct", "atguigu.news", map);

        //发送对象
        //rabbitTemplate.convertAndSend("exchange.direct", "atguigu.news", new Book("西游记", "吴承恩"));
    }

    /**
     * 接收数据，如何将数据转成json发送
     */
    @Test
    public void receive() {
        Object object = rabbitTemplate.receiveAndConvert("atguigu.news");
        System.out.println(object.getClass());
        System.out.println(object);
    }

    /**
     * 广播
     */
    @Test
    public void sendMsg() {
        rabbitTemplate.convertAndSend("exchange.fanout", "", new Book("西游记", "吴承恩"));
    }

}
