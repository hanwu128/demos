package com.hw.activemq.demo;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSConsumer2 {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final String URL = "tcp://192.168.3.21:61616";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory; // 连接工厂
        Connection connection = null; // 连接
        Session session; // 会话 接受或者发送消息的线程
        Destination destination; // 消息的目的地
        MessageConsumer messageConsumer; // 消息的消费者

        // 实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(JMSConsumer2.USERNAME, JMSConsumer2.PASSWORD, JMSConsumer2.URL);

        try {
            connection = connectionFactory.createConnection();  // 通过连接工厂获取连接
            connection.start(); // 启动连接
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE); // 创建Session
            destination = session.createQueue("FirstQueue1");  // 创建连接的消息队列
            messageConsumer = session.createConsumer(destination); // 创建消息消费者
            //采用监听的方式 ，注册监听，自动的去监听里触发消息
            messageConsumer.setMessageListener(new Listener()); // 注册消息监听
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 消息监听
 *
 * @author Administrator
 */
class Listener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("收到的消息：" + ((TextMessage) message).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
