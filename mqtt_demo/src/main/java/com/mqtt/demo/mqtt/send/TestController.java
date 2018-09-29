package com.mqtt.demo.mqtt.send;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private MqttGateway mqttGateway;


    @RequestMapping("/sendMqtt.do")
    public String sendMqtt(String sendData,String topic) {
        mqttGateway.sendToMqtt(sendData, topic);
        return "OK";
    }
}
