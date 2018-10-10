package com.lenovo.iot.devicemanager.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lenovo.iot.devicemanager.model.MqttMessage;
import com.lenovo.iot.devicemanager.model.Sensor_Label;
import com.lenovo.iot.devicemanager.service.DeviceService;
import com.lenovo.iot.devicemanager.service.SensorService;
import com.lenovo.iot.devicemanager.util.Pagination;

/**
 * 冷链
 * 
 * @author lidong
 * 
 */
@RequestMapping("/stream")
@Controller
public class SensorController {

	private static Logger log_data = Logger.getLogger("fordata");	

	@Autowired
	private SensorService sensorService;
	@Autowired
	private DeviceService deviceService;

	// 温控设备列表
	@RequestMapping(value = "/devicelist.url")
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject get_device_list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject result = sensorService.getDeviceList();
		return result;
	}

	// 修改设备信息 - 温度上下界
	@RequestMapping(value = "/update_device.url", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject update_device(@RequestBody JSONObject device_objects, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONArray device_list = device_objects.optJSONArray("devices");
		return sensorService.update_device(device_list);
	}

	// 标签注册(包含条码信息) - 暂不支持标签重复使用
	@RequestMapping(value = "/rfid_register.url", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins = "*")
	public JSONObject rfid_register(@RequestBody JSONObject rfid_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONArray ja = rfid_object.getJSONArray("reported");
		List<Sensor_Label> list = new ArrayList<Sensor_Label>();
		for (int i = 0; i < ja.size(); i++) {
		    JSONObject jo = (JSONObject) ja.get(i);
		    String barcode = jo.optString("barcode");
		    String rfid = jo.optString("rfid");
		    
		    Sensor_Label label = new Sensor_Label();
		    label.setLabel_id(rfid);
		    label.setLabel_index(barcode);
		    label.setLabel_name(barcode + "(" + rfid + ")");
		    
		    list.add(label);
		}
		
		JSONObject result = sensorService.registerLabel(list);
		return result;
	}
	
	// 写入温度数据：由 网关设备调用，不走 MQTT 协议
	@RequestMapping(value = "/rfid_upload.url", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins = "*")
	public JSONObject rfid_upload(@RequestBody JSONObject rfid_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log_data.info(rfid_object.toString());
		
		JSONObject reported = rfid_object.optJSONObject("reported");
		if(reported == null) {
			JSONObject object = new JSONObject();
    		object.put("result", false);
    		object.put("error", "empty reported");
    		return object;
		}
		
		String device_id = rfid_object.optString("deviceId");
		if(device_id == null || device_id.isEmpty()) {
			JSONObject object = new JSONObject();
    		object.put("result", false);
    		object.put("error", "empty deviceId");
    		return object;
		}
		
		String app_name = rfid_object.optString("appName");
		if(app_name == null || app_name.isEmpty()) {
			JSONObject object = new JSONObject();
    		object.put("result", false);
    		object.put("error", "empty appName");
    		return object;
		}
		
		app_name = app_name.toLowerCase();
		
		Long ts = rfid_object.optLong("timestamp");
		if(ts == null || ts == 0l) {
			JSONObject object = new JSONObject();
    		object.put("result", false);
    		object.put("error", "empty timestamp");
    		return object;
		}
		
		// 更新当日实时状态
		JSONObject result = sensorService.addTemperatureDay(device_id, app_name, reported, ts);
    	if(!result.optBoolean("result")) {
    		System.out.println(result.optString("error"));
    	}
    	
		int count = result.optInt("count");
		if(count > 0) {
		    // 更新标签的设备信息
			List<Sensor_Label> label_list = new ArrayList<Sensor_Label>();

			Iterator<String> keys = reported.keys();
			while (keys.hasNext()) {
				String label_id = keys.next();
				
				Sensor_Label label = sensorService.getSensorLabel(label_id);
				if(label != null) {
					label_list.add(label);
				} else {
					label = new Sensor_Label();
					label.setId(0l);
					label.setLabel_id(label_id);
					label.setLabel_name(label_id);
					label.setCreate_stamp(new Timestamp(System.currentTimeMillis()));
					label.setUpdate_stamp(new Timestamp(System.currentTimeMillis()));
					label_list.add(label);
					System.out.println("no label was found: " + label_id + ". now add it");
				}
			}
	    	
			sensorService.updateSensorLabel(label_list, device_id, app_name);
		}
    	
    	return result;
	}
	
	// 写入温度数据：由  mqtt subscriber 调用
	@RequestMapping(value = "/upload.url", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins = "*")
	public JSONObject upload(@RequestBody JSONObject mqtt_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log_data.info(mqtt_object.toString());

		String device_id = mqtt_object.getString("device_id");
		String mqtt_topic = mqtt_object.getString("topic");
		String mqtt_message = mqtt_object.getString("message");
		int mqtt_qos = mqtt_object.optInt("qos", 0);
		
		JSONObject js = JSONObject.fromObject(mqtt_message);
		String app_name = js.getString("app_name");
		long ts = js.getLong("timestamp");
		String tsString = String.format("%1$-13s", ts).replace(' ', '0');
		final Timestamp timestamp = new Timestamp(Long.valueOf(tsString));
		
		boolean isvalid = sensorService.validStream(device_id, app_name);
		
		//历史记录
		MqttMessage message = new MqttMessage();
		message.setDevice_id(device_id);
		message.setApp_name(app_name);
		message.setApp_type("stream");
		message.setTopic(mqtt_topic);
		message.setMessage(mqtt_message);
		message.setQos(mqtt_qos);
		message.setMessage_stamp(timestamp);
		message.setReceived(false);
		message.setIsvalid(isvalid);
		deviceService.addMqttMessage(message);
		
		app_name = app_name.toLowerCase();
		
		//仅添加了数据流的设备消息才会被处理
		if(isvalid) {
			//记录温度采集数据
			if (app_name.equalsIgnoreCase("app.topology.services.AdlinkStreamApp") || app_name.equalsIgnoreCase("app.topology.services.TemperatureDemoApp")) {
				JSONObject reported = js.getJSONObject("reported");
	
				JSONObject result = sensorService.addTemperatureDay(device_id, app_name, reported, ts);
				return result;
			} else if(app_name.equalsIgnoreCase("app.topology.services.GpsStreamApp") || app_name.equalsIgnoreCase("app.topology.services.GpsDemoApp")) {
				JSONObject reported = js.getJSONObject("reported");
	
				JSONObject result = sensorService.addGps(device_id, app_name, reported, ts);
				return result;
			}
		}
		
		JSONObject object = new JSONObject();
		object.put("result", false);
		object.put("error", "invalid message");
		return object;
	}

	// 设置报警已读
	@RequestMapping(value = "/set_real_alarm.url", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject set_real_alarm(@RequestBody JSONObject alarm_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String timestamp = alarm_object.optString("timestamp");
		if(timestamp != null && !timestamp.isEmpty()) {
			SimpleDateFormat FF_MIN = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date time = FF_MIN.parse(timestamp);
			return sensorService.set_real_alarm(time);
		}
		
		JSONObject object = new JSONObject();
		object.put("result", false);
		object.put("error", "invalid parameters");
		return object;
	}

	// 查询实时报警 - 查询所有未读的报警信息
	@RequestMapping(value = "/get_real_alarm_brief.url", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject get_real_alarm_brief(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return sensorService.get_real_alarm_brief();
	}

	// 查询实时报警 - 查询所有未读的报警信息
	@RequestMapping(value = "/get_real_alarm.url", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject get_real_alarm(@RequestBody JSONObject alarm_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int pagesize = alarm_object.optInt("pagesize");
		int current = alarm_object.optInt("current");
		String orderby = alarm_object.optString("sort_orderby");
		String asc = alarm_object.optString("sort_rule");
		
		Pagination page = new Pagination();
		page.setPagesize(pagesize);
		page.setCurrent(current);
		page.setOrder_by(orderby);
		page.setAsc(asc);

		return sensorService.get_real_alarm(page);
	}
	
	// 查询历史报警
	@RequestMapping(value = "/get_alarm_list.url", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject get_alarm_list(@RequestBody JSONObject alarm_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String device_id = alarm_object.optString("device_id");
		String day_begin_String = alarm_object.optString("day_begin");
		String day_end_String = alarm_object.optString("day_end");
		
		SimpleDateFormat HHMMSS = new SimpleDateFormat("yyyy-MM-dd");
		Date day_begin = HHMMSS.parse(day_begin_String);
		Date day_end = HHMMSS.parse(day_end_String);
		
		//+1 day
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day_end);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		
		day_end = calendar.getTime();
		
		int pagesize = alarm_object.optInt("pagesize");
		int current = alarm_object.optInt("current");
		String orderby = alarm_object.optString("sort_orderby");
		String asc = alarm_object.optString("sort_rule");
		
		Pagination page = new Pagination();
		page.setPagesize(pagesize);
		page.setCurrent(current);
		page.setOrder_by(orderby);
		page.setAsc(asc);
		
		JSONObject result = sensorService.getAlarmList(page, device_id, day_begin, day_end);
		return result;
	}
	
	@RequestMapping(value = "/get_breif.url", produces = { "text/plain;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	public String get_breif(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String result = sensorService.get_breif();
		return result;
	}
	
	@RequestMapping(value = "/get_gps.url")
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject get_gps(@RequestBody JSONObject params, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String device_id = params.getString("device_id");
		JSONObject result = sensorService.getGPS(device_id);
		
		return result;
	}

	@RequestMapping(value = "/get_data_proportion.url", produces = {"application/json; charset=UTF-8"})
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONArray get_data_proportion(@RequestBody JSONObject params, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String device_id = params.getString("device_id");
		JSONArray result = sensorService.get_data_proportion(device_id);
		
		return result;
	}

	@RequestMapping(value = "/get_data_trend.url", produces = {"application/json; charset=UTF-8"})
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject get_data_trend(@RequestBody JSONObject params, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//{"startTimeAt":"2017-09-28", "endTimeAt":"2017-09-28", "device_id":"test_deviceid_02", "max_value":40, "min_value":10}
		//String device_id = params.getString("device_id");
		String startTimeAt = params.getString("startTimeAt");
		String endTimeAt = params.getString("endTimeAt");
		Double max_value = params.getDouble("max_value");
		Double min_value = params.getDouble("min_value");
		JSONObject result = sensorService.get_data_trend(startTimeAt, endTimeAt, max_value, min_value);
		
		return result;
	}
}
