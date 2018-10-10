package com.lenovo.iot.digitaltwin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
    //////////////////////// MQTT配置参数 ////////////////////////
    @Value("${mqtt.broker}")
    private String mqttBroker;

    @Value("${mqtt.client_id}")
    private String mqttClientId;

    @Value("${mqtt.username}")
    private String mqttUserName;

    @Value("${mqtt.password}")
    private String mqttPassword;

    @Value("${mqtt.keep_alive}")
    private String mqttKeepAlive;

    @Value("${mqtt.clean_session}")
    private String mqttCleanSession;

    //////////////////////// TSDB配置参数 ////////////////////////
    @Value("${tsdb.url}")
    private String tsDbUrl;

    @Value("${tsdb.port}")
    public String tsDbPort;

    //////////////////////// Token验证地址 ////////////////////////
    @Value("${token.url}")
    public String tokenUrl;

    public String getMqttBroker() {
        return mqttBroker;
    }

    public void setMqttBroker(String mqttBroker) {
        this.mqttBroker = mqttBroker;
    }

    public String getMqttClientId() {
        return mqttClientId;
    }

    public void setMqttClientId(String mqttClientId) {
        this.mqttClientId = mqttClientId;
    }

    public String getMqttUserName() {
        return mqttUserName;
    }

    public void setMqttUserName(String mqttUserName) {
        this.mqttUserName = mqttUserName;
    }

    public String getMqttPassword() {
        return mqttPassword;
    }

    public void setMqttPassword(String mqttPassword) {
        this.mqttPassword = mqttPassword;
    }

    public String getMqttKeepAlive() {
        return mqttKeepAlive;
    }

    public void setMqttKeepAlive(String mqttKeepAlive) {
        this.mqttKeepAlive = mqttKeepAlive;
    }

    public String getMqttCleanSession() {
        return mqttCleanSession;
    }

    public void setMqttCleanSession(String mqttCleanSession) {
        this.mqttCleanSession = mqttCleanSession;
    }

    public String getTsDbUrl() {
        return tsDbUrl;
    }

    public void setTsDbUrl(String tsDbUrl) {
        this.tsDbUrl = tsDbUrl;
    }

    public String getTsDbPort() {
        return tsDbPort;
    }

    public void setTsDbPort(String tsDbPort) {
        this.tsDbPort = tsDbPort;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }
}
