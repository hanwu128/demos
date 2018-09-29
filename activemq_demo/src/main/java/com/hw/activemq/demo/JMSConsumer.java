package com.hw.activemq.demo;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSConsumer {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final String URL = "tcp://192.168.3.21:61616";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageConsumer messageConsumer;

        connectionFactory = new ActiveMQConnectionFactory(JMSConsumer.USERNAME, JMSConsumer.PASSWORD, JMSConsumer.URL);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("FirstQueue1");
            messageConsumer = session.createConsumer(destination);
            while (true) {
                TextMessage textMessage = (TextMessage) messageConsumer.receive(100000);//1s钟接收1次消息
                if (textMessage != null) {
                    System.out.println("收到的消息：" + textMessage.getText());
                } else {
                    break;
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
