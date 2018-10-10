package com.lenovo.iot.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
    @Value("${mqtt.broker.address}")
    private String mqtt_broker_address;

    @Value("${mqtt.broker.nodes}")
    private String mqtt_broker_nodes;

	public String getMqtt_broker_address() {
		return mqtt_broker_address;
	}

	public void setMqtt_broker_address(String mqtt_broker_address) {
		this.mqtt_broker_address = mqtt_broker_address;
	}

    public String getMqtt_broker_nodes() {
		return mqtt_broker_nodes;
	}

	public void setMqtt_broker_nodes(String mqtt_broker_nodes) {
		this.mqtt_broker_nodes = mqtt_broker_nodes;
	}
}
