package com.lenovo.iot.devicemanager.service;

import com.lenovo.iot.devicemanager.util.HttpClient;

import net.sf.json.JSONObject;

public class OnlineDeviceService {

	public static void online(String borker, String clientId, String messageString) {
		// {"clientid":"MOC_EmqttServerAgent","username":"usename and password can't be empty","ipaddress":"111.205.43.234","clean_sess":false,"protocol":4,"connack":0,"ts":1499169181}
		try {
			JSONObject js = JSONObject.fromObject(messageString);
			String username = js.optString("username");
			String ipaddress = js.optString("ipaddress");
			long timestamp = js.optLong("ts");

			JSONObject params = new JSONObject();
			params.put("device_id", clientId);
			params.put("username", username);
			params.put("status", 1);
			params.put("ipaddress", ipaddress);
			params.put("timestamp", timestamp);

			HttpClient.httpPost("device/online.url", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void offline(String borker, String clientId, String messageString) {
		// {"clientid":"MOC_EmqttServerAgent","reason":"closed","ts":1499169182})
		try {
			JSONObject js = JSONObject.fromObject(messageString);
			long ts = js.optLong("ts");

			JSONObject params = new JSONObject();
			params.put("device_id", clientId);
			params.put("status", 0);
			params.put("timestamp", ts);

			HttpClient.httpPost("device/online.url", params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Device offline:" + clientId);
	}
}
