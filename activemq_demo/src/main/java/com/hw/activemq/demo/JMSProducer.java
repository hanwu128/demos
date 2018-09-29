package com.hw.activemq.demo;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSProducer {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final String URL = "tcp://192.168.3.21:61616";
    private static final int SENDNUM = 10;

    public static void main(String[] args) {

        ConnectionFactory connectionFactory;//创建连接工厂
        Connection connection = null;//创建连接
        Session session;//创建会话 ，发送或者接收消息
        Destination destination;//消息目的地
        MessageProducer messageProducer;//消息生产者

        //实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(JMSProducer.USERNAME, JMSProducer.PASSWORD, JMSProducer.URL);
        try {
            connection = connectionFactory.createConnection();//获取链接
            connection.start();//启动连接
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);//创建Session
            destination = session.createQueue("FirstQueue1");//创建消息队列
            messageProducer = session.createProducer(destination);//创建消息生产者
            sendMessage(session, messageProducer);//发送消息
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 发送消息
     *
     * @param session
     * @param messageProducer
     * @throws Exception
     */
    public static void sendMessage(Session session, MessageProducer messageProducer) throws Exception {
        for (int i = 0; i < JMSProducer.SENDNUM; i++) {
            TextMessage message = session.createTextMessage("ActiveMQ 发送的消息" + i);
            System.out.println("发送消息：" + "ActiveMQ 发送的消息" + i);
            messageProducer.send(message);
        }
    }
}
