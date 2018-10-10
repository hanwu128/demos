package com.lenovo.iot.digitaltwin.service;

import com.lenovo.iot.digitaltwin.config.Config;
import com.lenovo.iot.digitaltwin.util.IOUtil;
import com.lenovo.iot.digitaltwin.util.SSL;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.InputStream;

/**
 * @Desc: MQTT服务，封装MQTT客户端
 * @Name: com.lenovo.iot.digitaltwin.service.MQTTService
 * @Author: chench9@lenovo.com
 * @Date: 2018/7/12
 */
@Service
public class MQTTService {
    private static final Logger logger = LoggerFactory.getLogger(MQTTService.class);
    private static MqttClient mqttClient = null;
    private static MqttConnectOptions mqttClientConOpt = null;

    @Autowired
    private Config config;

    /**
     * 发送反控设备消息
     * @param topic
     * @param message
     * @return
     */
    public boolean publish(String topic, String message) {
        if((topic == null || "".equals(topic.trim())) ||
                (message == null || "".equals(message.trim()))) {
            logger.error("topic or message is empty: {}, {}", topic, message);
            return false;
        }
        if(mqttClient == null) {
            logger.error("mqtt client is null");
            return false;
        }
        if(mqttClientConOpt == null) {
            logger.error("mqtt connection option is null");
            return false;
        }

        try {
            if(!mqttClient.isConnected()) {
                mqttClient.connect(this.mqttClientConOpt);
            }
            MqttMessage msg = new MqttMessage();
            msg.setPayload(message.getBytes());
            msg.setQos(0);
            mqttClient.publish(topic, msg);
            return true;
        } catch (MqttException e) {
            logger.error("public mqtt message error: {}, {}, {}", topic, message, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @PostConstruct
    public void initMqttClient() {
        InputStream is = null;
        try {
            is = this.getClass().getResourceAsStream("/mqttserver.crt");
            String tmpDir = System.getProperty("java.io.tmpdir");
            MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);

            MqttConnectOptions opt = new MqttConnectOptions();
            if (!StringUtils.isEmpty(config.getMqttUserName())) {
                opt.setUserName(config.getMqttUserName());
            }
            if (!StringUtils.isEmpty(config.getMqttPassword())) {
                opt.setPassword(config.getMqttPassword().toCharArray());
            }
            opt.setAutomaticReconnect(true); //自动重连没有回调，若要回调需要使用  MqttAsyncClient
            opt.setCleanSession(Boolean.valueOf(config.getMqttCleanSession()));
            opt.setKeepAliveInterval(Integer.valueOf(config.getMqttKeepAlive()));
            opt.setSocketFactory(SSL.getSSLSocktet(is));

            mqttClient = new MqttClient(config.getMqttBroker(), config.getMqttClientId(), dataStore);
            mqttClient.setCallback(new MqttCallbackImpl());
            //mqttClient.connect(opt);
            mqttClientConOpt = opt;
            //client.subscribe(topic, qos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeInputStream(is);
        }
    }

    @PreDestroy
    public void clean() {
        if(mqttClient == null) {
            return;
        }

        try {
            if(mqttClient.isConnected()) {
                mqttClient.close(true);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // MQTT客户端回调
    class MqttCallbackImpl implements MqttCallback {
        @Override
        public void connectionLost(Throwable throwable) {
            if(logger.isDebugEnabled()) {
                logger.debug("connectionLost");
            }
        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            if(logger.isDebugEnabled()) {
                logger.debug("messageArrived");
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            if(logger.isDebugEnabled()) {
                logger.debug("deliveryComplete");
            }
        }
    }
}
