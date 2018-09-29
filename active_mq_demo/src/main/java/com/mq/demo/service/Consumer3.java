package com.mq.demo.service;


import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class Consumer3 {

    @JmsListener(destination = "mytest.queue")
    @SendTo("out.queue")
    public String receiveQueue(String text) {
        System.out.println("Consumer3收到的报文为:"+text);
        return "Consumer3 return message"+text;
    }
}