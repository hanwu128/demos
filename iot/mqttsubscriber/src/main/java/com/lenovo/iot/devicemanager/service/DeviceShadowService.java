package com.lenovo.iot.devicemanager.service;

import net.sf.json.JSONObject;

import com.lenovo.iot.devicemanager.util.HttpClient;

public class DeviceShadowService {

	public static void transfer(String clientId, String topic, String message, int qos) {
		// 影子变化 - 通知应用服务更新
		try {
			JSONObject params = new JSONObject();
			params.put("device_id", clientId);
			params.put("topic", topic);
			params.put("message", message);
			params.put("qos", qos);
			
			HttpClient.httpPost("deviceshadow/update_item_value.url", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
