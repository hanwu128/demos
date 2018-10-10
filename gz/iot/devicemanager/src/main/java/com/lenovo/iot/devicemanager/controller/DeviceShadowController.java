package com.lenovo.iot.devicemanager.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lenovo.iot.devicemanager.model.DeviceEx;
import com.lenovo.iot.devicemanager.model.DeviceShadow;
import com.lenovo.iot.devicemanager.model.DeviceShadowEx;
import com.lenovo.iot.devicemanager.model.DeviceShadowItem;
import com.lenovo.iot.devicemanager.model.MqttMessage;
import com.lenovo.iot.devicemanager.model.vo.Messenger;
import com.lenovo.iot.devicemanager.mqtt.EmqttInstance;
import com.lenovo.iot.devicemanager.service.DeviceService;
import com.lenovo.iot.devicemanager.service.DeviceShadowService;
import com.lenovo.iot.devicemanager.util.LoginedAccountHolder;
import com.lenovo.iot.devicemanager.util.Pagination;
import com.lenovo.iot.devicemanager.util.SessionUtil;
import com.lenovo.iot.devicemanager.util.WebUtil;

@Controller
public class DeviceShadowController {

//	private static Logger log = Logger.getLogger("fordebug");

	@Autowired
	private DeviceShadowService deviceShadowService;
	@Autowired
	private DeviceService deviceService;

	/// 返回影子信息，包含影子属性列表
	@RequestMapping(value = "/deviceshadow/get.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"devmirror:manage"})
	public JSONObject deviceShadow_get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long company_id = 100000; //LoginedAccountHolder.getLoginedAccount().getCompanyId();
		String shadow_name = WebUtil.getPara(request, "shadow_name");
		JSONObject result = deviceShadowService.getDeviceShadow(company_id, shadow_name);
		return result;
	}
	
	/// 返回影子信息，包含影子属性列表
	@RequestMapping(value = "/deviceshadow/exist.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject deviceShadow_exist(@RequestBody JSONObject deviceShadow_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		String device_id = deviceShadow_object.getString("device_id");
		String app_name = deviceShadow_object.getString("app_name");
		JSONObject result = deviceShadowService.getDeviceShadowCount(company_id, device_id, app_name);
		
		return result;
	}

	///新增影子，包括属性集合
	@RequestMapping(value = "/deviceshadow/add.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"devmirror:manage"})
	public JSONObject deviceShadow_add(@RequestBody JSONObject params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// {shadow_name: "22", device_id: "test_deviceid_01", app_name: "app.topology.services.AdlinkStreamApp", shadow_desc:"33", items:[,…]}
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		String shadow_name = params.getString("shadow_name");
		String device_id = params.getString("device_id");
		String app_name = params.getString("app_name");
		String shadow_desc = params.optString("shadow_desc");
//		String app_version = params.optString("app_version");
//		String app_desc = params.optString("app_desc");
		JSONArray shadow_items = params.getJSONArray("items");
		
		DeviceShadow deviceShadow = new DeviceShadow();
		deviceShadow.setShadow_name(shadow_name);
		deviceShadow.setDevice_id(device_id);
		deviceShadow.setCompany_id(company_id);
		deviceShadow.setApp_name(app_name);
		deviceShadow.setShadow_desc(shadow_desc);
		JSONObject app = deviceService.get_app(device_id, app_name);
		if(app != null) {
			deviceShadow.setApp_version(app.optString("app_version"));
			deviceShadow.setApp_desc(app.optString("desc"));
		}
		
		List<DeviceShadowItem> deviceShadowItems = new ArrayList<DeviceShadowItem>();
		for (int i = 0; i < shadow_items.size(); i++) {
			JSONObject jo = (JSONObject) shadow_items.get(i);
			DeviceShadowItem item = new DeviceShadowItem();
			item.setShadow_name(shadow_name);
			item.setItem_name(jo.optString("sourcename"));
			item.setItem_display_name(jo.optString("displayVal"));
			item.setItem_map_name(jo.optString("inputVal"));
			item.setItem_default(jo.optString("sourcedefault"));
			item.setItem_datatype(jo.optString("sourcedatatype"));
			item.setItem_unit(jo.optString("sourceunit"));
			
			deviceShadowItems.add(item);
		}

		JSONObject result = deviceShadowService.addDeviceShadow(deviceShadow, deviceShadowItems);
		return result;
	}

	/// 删除影子(及其属性列表)
	@RequestMapping(value = "/deviceshadow/delete.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"devmirror:manage"})
	public JSONObject deviceShadow_delete(@RequestBody JSONObject shadow_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		String shadow_name = shadow_object.getString("shadow_name");
		JSONObject result = deviceShadowService.deleteDeviceShadow(company_id, shadow_name);
		return result;
	}

	/// 更新影子基本信息
	@RequestMapping(value = "/deviceshadow/update.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"devmirror:manage"})
	public JSONObject deviceShadow_update(@RequestBody JSONObject shadow_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		String shadow_name = shadow_object.getString("shadow_name");
		String device_id = shadow_object.getString("device_id");
		String app_name = shadow_object.getString("app_name");
		String shadow_desc = shadow_object.getString("shadow_desc");

		DeviceShadow deviceShadow = new DeviceShadow();
		deviceShadow.setShadow_name(shadow_name);
		deviceShadow.setDevice_id(device_id);
		deviceShadow.setCompany_id(company_id);
		deviceShadow.setApp_name(app_name);
		deviceShadow.setShadow_desc(shadow_desc);

		JSONObject result = deviceShadowService.updateDeviceShadow(deviceShadow);
		return result;
	}

	/// 获取设备影子列表
	/// 若不传入device_id则表示查询全部
	@RequestMapping(value = "/deviceshadow/list.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"devmirror:manage"})
	public JSONObject deviceShadow_list(@RequestBody JSONObject shadow_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		String access_key = shadow_object.optString("access_key");
		int pagesize = shadow_object.optInt("pagesize");
		int current = shadow_object.optInt("current");
		String orderby = shadow_object.optString("sort_orderby");
		String asc = shadow_object.optString("sort_rule");

		Pagination page = new Pagination();
		page.setPagesize(pagesize);
		page.setCurrent(current);
		page.setOrder_by(orderby);
		page.setAsc(asc);

		JSONObject result = deviceShadowService.getDeviceShadowList(page, company_id, access_key);
		return result;
	}
	
/*	
	///新增影子属性
	@RequestMapping(value = "/deviceshadow/add_item.do")
	@CrossOrigin(origins="*")
	public void deviceShadow_add_item(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String shadow_name = WebUtil.getPara(request, "shadow_name");
		String item_name = WebUtil.getPara(request, "item_name");
		String item_display_name = WebUtil.getPara(request, "item_display_name");
		String item_map_name = WebUtil.getPara(request, "item_map_name");
		String item_default = WebUtil.getPara(request, "item_default");
		String item_datatype = WebUtil.getPara(request, "item_datatype");
		String item_unit = WebUtil.getPara(request, "item_unit");

		DeviceShadowItem deviceShadowItem = new DeviceShadowItem();
		deviceShadowItem.setShadow_name(shadow_name);
		deviceShadowItem.setItem_name(item_name);
		deviceShadowItem.setItem_display_name(item_display_name);
		deviceShadowItem.setItem_map_name(item_map_name);
		deviceShadowItem.setItem_default(item_default);
		deviceShadowItem.setItem_datatype(item_datatype);
		deviceShadowItem.setItem_unit(item_unit);

		String result = deviceShadowService.addDeviceShadowItem(deviceShadowItem);
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(result);
	}
	
	/// 删除影子属性
	@RequestMapping(value = "/deviceshadow/delete_item.do")
	@CrossOrigin(origins="*")
	public void deviceShadow_delete_item(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String shadow_name = WebUtil.getPara(request, "shadow_name");
		String item_name = WebUtil.getPara(request, "item_name");
		String result = deviceShadowService.deleteDeviceShadowItem(shadow_name, item_name);
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(result);
	}

	/// 更新影子 map_name
	@RequestMapping(value = "/deviceshadow/update_item_mapname.do")
	@CrossOrigin(origins="*")
	public void deviceShadow_update_item_mapname(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String shadow_name = WebUtil.getPara(request, "shadow_name");
		String item_name = WebUtil.getPara(request, "item_name");
		String item_map_name = WebUtil.getPara(request, "item_map_name");

		DeviceShadowItem deviceShadowItem = new DeviceShadowItem();
		deviceShadowItem.setShadow_name(shadow_name);
		deviceShadowItem.setItem_name(item_name);
		deviceShadowItem.setItem_map_name(item_map_name);

		String result = deviceShadowService.updateDeviceShadowItemMapName(deviceShadowItem);
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(result);
	}
*/

	/// 更新影子属性期望值，并发送请求至 mqtt server
	/*
	desired = {
		"temprature": 29,
		"humidity": 50
	}
	 */
	@RequestMapping(value = "/deviceshadow/update_item_expectvalue.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"devmirror:manage"})
	public JSONObject deviceShadow_update_item_expectvalue(@RequestBody JSONObject params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// {device_id: "111", desired: {1: "22"}}
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		String device_id = params.getString("device_id");
		String app_name = params.optString("app_name");
		JSONObject items = params.getJSONObject("desired");
		
		JSONObject result = deviceShadowService.updateDeviceShadowItemExpectValues(company_id, device_id, app_name, items);
				
		return result;
	}

	/// 更新影子属性值  - 由 mqttsubscriber接收消息并发送请求至此
	/*
	reported = {
		"humidity": 50,
		"temprature": 29
	}
	*/
	@RequestMapping(value = "/deviceshadow/update_item_value.url", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject deviceShadow_update_item_value(@RequestBody JSONObject shadow_item_object, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String device_id = shadow_item_object.getString("device_id");
		String mqtt_topic = shadow_item_object.getString("topic");
		JSONObject mqtt_message = shadow_item_object.getJSONObject("message");
		int mqtt_qos = shadow_item_object.getInt("qos");
		
		String app_name = mqtt_message.getString("app_name");
		long ts = mqtt_message.getLong("timestamp");
		String tsString = String.format("%1$-13s", ts).replace(' ', '0');
		final Timestamp timestamp = new Timestamp(Long.valueOf(tsString));
		
		//历史记录
		MqttMessage message = new MqttMessage();
		message.setDevice_id(device_id);
		message.setApp_name(app_name);
		message.setApp_type("shadow");
		message.setTopic(mqtt_topic);
		message.setMessage(mqtt_message.toString());
		message.setQos(mqtt_qos);
		message.setMessage_stamp(timestamp);
		message.setReceived(false);
		deviceService.addMqttMessage(message);

		//更新影子
		JSONObject reported = mqtt_message.getJSONObject("reported");
		
		final List<Pair<String, String>> pairs = new ArrayList<Pair<String, String>>();
		Iterator<String> keys = reported.keys();
        while(keys.hasNext()) {  
        	String key = keys.next();
            String value = reported.get(key).toString();
            
            pairs.add(new Pair<String, String>(key, value));
        }
        
        JSONObject result = deviceShadowService.updateDeviceShadowItemValues(device_id, pairs);
		return result;
	}

	/// 获取设备影子的属性列表
	@RequestMapping(value = "/deviceshadow/itemlist.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject deviceShadow_item_list(@RequestBody JSONObject shadow_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		String shadow_name = shadow_object.getString("shadow_name");
		JSONObject result = deviceShadowService.getDeviceShadowItemList(company_id, shadow_name);
		return result;
	}
}
