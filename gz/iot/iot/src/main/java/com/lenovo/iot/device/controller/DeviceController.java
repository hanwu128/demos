package com.lenovo.iot.device.controller;

import com.lenovo.iot.device.service.DeviceService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping({ "/" })
@Controller
public class DeviceController {
	@Autowired
	private DeviceService deviceService;

	@RequestMapping(value = { "/register.url" }, produces = { "application/json; charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins = { "*" })
	public JSONObject device_register(@RequestBody String device, HttpServletRequest request, HttpServletResponse response) {
		JSONObject device_object = JSONObject.fromObject(device);

		long company_id = device_object.optLong("company_id");
		String company_sk = device_object.optString("company_sk");
		String device_id = device_object.optString("device_id");
		String device_desc = device_object.optString("device_desc");
		try {
			return this.deviceService.registerDevice(company_id, company_sk, device_id, device_desc);
		} catch (Exception e) {
			e.printStackTrace();

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			JSONObject object = new JSONObject();
			object.put("message", e.toString());

			return object;
		}
	}

//	@RequestMapping(value = { "/online.url" }, produces = { "application/json;charset=UTF-8" })
//	@ResponseBody
//	@CrossOrigin(origins = { "*" })
//	public JSONObject device_online(@RequestBody JSONObject device_online_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String device_id = device_online_object.getString("clientid");
//		long ts = device_online_object.getLong("ts");
//		String username = device_online_object.optString("username");
//		String ipaddress = device_online_object.optString("ipaddress");
//		int status = username.isEmpty() ? 0 : 1;
//
//		JSONObject result = this.deviceService.setOnlineStatus(device_id, username, status, ts, ipaddress);
//		return result;
//	}
}
