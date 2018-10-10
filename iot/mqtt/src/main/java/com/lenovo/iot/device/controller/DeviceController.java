package com.lenovo.iot.device.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lenovo.iot.device.service.DeviceService;

/**
 * 设备接入
 * 
 * @author lidong
 *
 */
@RequestMapping("/device")
@RestController
public class DeviceController {

	private Logger log = Logger.getLogger("device");

	@Autowired
	private DeviceService deviceService;

	@RequestMapping(value = "/get.url", produces = { "application/json;charset=UTF-8" })
	@CrossOrigin(origins = "*")
	public String device_get(@RequestBody String device, HttpServletRequest request, HttpServletResponse response) {

		try {
			JsonParser parser = new JsonParser();
			JsonObject device_object = (JsonObject)parser.parse(device);

			long company_id = device_object.get("company_id").getAsLong();
			String access_key = device_object.get("access_key").getAsString();

			return deviceService.getDevice(company_id, access_key).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			JsonObject object = new JsonObject();
			object.addProperty("code", 400);
			object.addProperty("message", e.toString());
			
			return object.toString();
		}
	}

	/**
	 * auto register device
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/register.url", produces = { "application/json;charset=UTF-8" })
	@CrossOrigin(origins = "*")
	public String device_register(@RequestBody String device, HttpServletRequest request, HttpServletResponse response) {
		log.debug(device);

		try {
			JsonParser parser = new JsonParser();
			JsonObject device_object = (JsonObject)parser.parse(device);
			
			long company_id = device_object.get("company_id").getAsLong();
			String company_sk = device_object.get("company_sk").getAsString();
			String device_id = device_object.get("device_id").getAsString();
			String device_desc = null;
			if(device_object.has("device_desc")) {
				device_desc = device_object.get("device_desc").getAsString();
			}

			JsonObject result = deviceService.registerDevice(company_id, company_sk, device_id, device_desc);
			return result.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			JsonObject object = new JsonObject();
			object.addProperty("code", 400);
			object.addProperty("message", e.toString());
			
			log.debug("register device failed: " + e.toString());

			return object.toString();
		}
	}

/*
	{
	    "deviceId ": "",
	    "hardware_model": "",
	    "hardware_manufactor": "",
	    "hardware_os": "Raspbian",
	    "hardware_os_version": "8.0",
	    "hardware_location": "bj_ZN",
	    "firmware_name": "",
	    "firmware_version": "",
	    "host_ip": "192.168.0.101"
	}
*/
   @RequestMapping(value = "/meta.url", produces = { "application/json;charset=UTF-8" })
    @CrossOrigin(origins="*")
    public String device_meta(@RequestBody String meta, HttpServletRequest request, HttpServletResponse response) {
		log.debug(meta);

		try {
			JsonParser parser = new JsonParser();
			JsonObject device_meta = (JsonObject)parser.parse(meta);
	    	
			JsonObject result = deviceService.updateDeviceMeta(device_meta);
			return result.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	
			JsonObject object = new JsonObject();
			object.addProperty("code", 400);
			object.addProperty("message", e.toString());
			
			log.debug("update device meta failed: " + e.toString());
	
			return object.toString();
		}
   }
    
    /**
		{
			"clientid": "10000100000DC30AA8C6D1319AA69432",
			"username": "deviceid_for_tomcat1",
			"ipaddress": "172.17.171.17",
			"clean_sess": false,
			"protocol": 4,
			"connack": 0,
			"ts": 1526472492
		}

		{
			"clientid": "10000100000E9BBCF0BBFF7B4B075929",
			"reason": "closed",
			"ts": 1526472489
		}
	 * @param device_online_object
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/online.url", produces = { "application/json;charset=UTF-8" })
	@CrossOrigin(origins = "*")
	public String device_online(@RequestBody JsonObject device_online_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String device_id = device_online_object.get("clientid").getAsString();
		long ts = device_online_object.get("ts").getAsLong();
		String username = device_online_object.get("username").getAsString();
		String ipaddress = device_online_object.get("ipaddress").getAsString();
		int status = (username.isEmpty() ? 0 : 1);

		return deviceService.setOnlineStatus(device_id, username, status, ts, ipaddress).toString();
	}
}
