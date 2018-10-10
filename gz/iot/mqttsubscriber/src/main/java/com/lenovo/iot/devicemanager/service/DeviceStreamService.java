package com.lenovo.iot.devicemanager.service;

import net.sf.json.JSONObject;

import com.lenovo.iot.devicemanager.util.HttpClient;

public class DeviceStreamService {

	public static void transfer(String clientId, String topic, String message, int qos) {
		// 流数据上报 - 保存在数据库
		try {
			JSONObject params = new JSONObject();
			params.put("device_id", clientId);
			params.put("topic", topic);
			params.put("message", message);
			params.put("qos", qos);
			HttpClient.httpPost("stream/upload.url", params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// System.out.println("stream data uploaded:" + message);
	}
}
