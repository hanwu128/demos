package com.lenovo.iot.devicemanager.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lenovo.iot.devicemanager.dao.DeviceDao;
import com.lenovo.iot.devicemanager.dao.DeviceShadowDao;
import com.lenovo.iot.devicemanager.dao.DeviceShadowItemDao;
import com.lenovo.iot.devicemanager.dao.IAppDao;
import com.lenovo.iot.devicemanager.model.Device;
import com.lenovo.iot.devicemanager.model.DeviceShadow;
import com.lenovo.iot.devicemanager.model.DeviceShadowEx;
import com.lenovo.iot.devicemanager.model.DeviceShadowItem;
import com.lenovo.iot.devicemanager.model.MqttMessage;
import com.lenovo.iot.devicemanager.model.bo.App;
import com.lenovo.iot.devicemanager.mqtt.EmqttInstance;
import com.lenovo.iot.devicemanager.util.Pagination;
import com.lenovo.iot.devicemanager.util.WebUtil;

@Service
public class DeviceShadowService {
	private static final Logger log = LoggerFactory.getLogger(DeviceShadowService.class);

	@Autowired
	private DeviceShadowDao deviceShadowDao;
	@Autowired
	private DeviceShadowItemDao deviceShadowItemDao;
	@Autowired
	private DeviceDao deviceDao;

    @Autowired
    private IAppDao appDao;
    //@Autowired
    //private IAppSourceDao appSourceDao;
    
    @Autowired
    private EmqttInstance emqttInstance;
	
	/// 返回影子信息，包含影子属性列表
	public JSONObject getDeviceShadow(long company_id, String shadow_name) throws IOException, ServletException {
		if(shadow_name == null || shadow_name.isEmpty()) {
			log.debug("empty device name");
			return JSONObject.fromObject(new DeviceShadow());
		} else if(company_id == 0) {
			log.debug("empty company id");
			return JSONObject.fromObject(new DeviceShadow());
		} else {
			DeviceShadow deviceShadow = deviceShadowDao.getDeviceShadow(company_id, shadow_name);
			if(deviceShadow != null) {
				Device device = null;
				if(deviceShadow.getDevice_id() != null && !deviceShadow.getDevice_id().isEmpty()) {
					device = deviceDao.getDevice(deviceShadow.getDevice_id());
				}
				List<DeviceShadowItem> deviceShadowItems = deviceShadowItemDao.getDeviceShadowItemList(company_id, shadow_name);
				JSONObject object = getDeviceJsonObject(device, deviceShadow, deviceShadowItems);
	
				App app = new App();
		        app.setAppname(deviceShadow.getApp_name());
		        app.setAppversion(deviceShadow.getApp_version());
		        app.setAppdesc(deviceShadow.getApp_desc());
		        object.put("app", app);

				return object;
			} else {
				log.debug("no device shadow found");
				return JSONObject.fromObject(new DeviceShadow());
			}
		}
	}
	
	/// 返回影子信息，包含影子属性列表
	public JSONObject getDeviceShadowCount(long company_id, String device_id, String app_name) {
		boolean result;
		int count;

		if(device_id == null || device_id.isEmpty()) {
			log.debug("empty device_id");
			result = false;
			count = 0;
		} else if(company_id == 0) {
			log.debug("empty company id");
			result = false;
			count = 0;
		} else {
			count = deviceShadowDao.getDeviceShadowCount(company_id, device_id, app_name);
			result = true;
		}
		
		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("count", count);
		
		return object;
	}
	
	private JSONObject getDeviceJsonObject(Device device, DeviceShadow deviceShadow, List<DeviceShadowItem> deviceShadowItems) {
		JSONObject object = new JSONObject();
		if(deviceShadow != null) {
			object.put("shadow_name", deviceShadow.getShadow_name());
			object.put("device_id", deviceShadow.getDevice_id());
			object.put("app_name", deviceShadow.getApp_name());
			
			String shadow_desc = deviceShadow.getShadow_desc();
			if (shadow_desc == null) {
				shadow_desc = "";
			}
			object.put("shadow_desc", shadow_desc);
	
			String create_stamp = WebUtil.format(deviceShadow.getCreate_stamp());
			object.put("create_stamp", create_stamp);
			String update_stamp = WebUtil.format(deviceShadow.getUpdate_stamp());
			object.put("update_stamp", update_stamp);
			
			String access_key = device.getAccess_key();
			if (access_key == null) {
				access_key = "";
			}
			object.put("access_key", access_key);
			
			object.put("items", getDeviceItemsJsonObject(deviceShadowItems));
		}
		
		return object;
	}
	
	private JSONArray getDeviceItemsJsonObject(List<DeviceShadowItem> deviceShadowItems) {
		JSONArray objarray = new JSONArray();
		if(deviceShadowItems != null && deviceShadowItems.size() > 0) {
			for(DeviceShadowItem item : deviceShadowItems) {
				JSONObject item_obj = new JSONObject();
				item_obj.put("id", item.getId());
				item_obj.put("shadow_name", item.getShadow_name());
				item_obj.put("item_name", item.getItem_name());
				item_obj.put("item_display_name", item.getItem_display_name());
				item_obj.put("item_map_name", item.getItem_map_name());
				item_obj.put("item_default", item.getItem_default() == null ? "" : item.getItem_default());
				item_obj.put("item_datatype", item.getItem_datatype() == null ? "" : item.getItem_datatype());
				item_obj.put("item_unit", item.getItem_unit() == null ? "" : item.getItem_unit());
				item_obj.put("item_value", item.getItem_value() == null ? "" : item.getItem_value());
				item_obj.put("itemvalue_stamp", item.getItemvalue_stamp() == null ? "" : WebUtil.format(item.getItemvalue_stamp()));
				item_obj.put("expect_value", item.getExpect_value() == null ? "" : item.getItem_value());
				item_obj.put("expectvalue_stamp", item.getExpectvalue_stamp() == null ? "" : WebUtil.format(item.getExpectvalue_stamp()));
				
				objarray.add(item_obj);
			}
		}
		
		return objarray;
	}

	///新增影子，包括从app解析出来的的属性集合
	public JSONObject addDeviceShadow(DeviceShadow deviceShadow, List<DeviceShadowItem> deviceShadowItems) throws IOException, ServletException {
		boolean result;
		String error;
		
		if(deviceShadow.getShadow_name() == null || deviceShadow.getShadow_name().isEmpty()) {
			result = false;
			error = "empty device shadow name";
		} else if(deviceShadow.getCompany_id() == 0) {
			result = false;
			error = "empty company id";
		} else {
			try {
				if (deviceShadowDao.getDeviceShadow(deviceShadow.getCompany_id(), deviceShadow.getShadow_name()) != null) {
					result = false;
					error = "Device shadow already exists";
				} else {
					int row = deviceShadowDao.addDeviceShadow(deviceShadow, deviceShadowItems);
					if(row > 0) {
						result = true;
						error = "success";
					} else {
						result = false;
						error = "fail";
					}
				}
			} catch (RuntimeException e) {
				log.debug(e.getMessage());
				result = false;
				error = e.getMessage();
			}
		}
		
		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);
		
		if(result) {
			//将 MAP 发送到设备
			/*
			{"temperature":"temperature", "humidity":"humidity"}";
			*/
			String topic = "$MOC/control/" + deviceShadow.getDevice_id() + "/map/update";
			
			JSONObject mapObject = new JSONObject();
			for(DeviceShadowItem item : deviceShadowItems) {
				mapObject.put(item.getItem_name(), item.getItem_map_name());
			}
			String message = EmqttInstance.buildMap(deviceShadow.getApp_name(), mapObject.toString());
			
			MqttMessage msgObj = new MqttMessage();
			msgObj.setTopic(topic);
			msgObj.setMessage(message);
			msgObj.setQos(0);
			msgObj.setDevice_id(deviceShadow.getDevice_id());
			msgObj.setApp_name(deviceShadow.getApp_name());
			//msgObj.setApp_type("shadow");
			emqttInstance.pulishMessage(msgObj);
			
			//向设备请求shadow
			/*
			{"app_name": ""}
			*/
			topic = "$MOC/control/" + deviceShadow.getDevice_id() + "/shadow/request";
			message = "{\"app_name\": \"" + deviceShadow.getApp_name() + "\"}";
			MqttMessage msgObj2 = new MqttMessage();
			msgObj2.setTopic(topic);
			msgObj2.setMessage(message);
			msgObj2.setQos(0);
			msgObj2.setDevice_id(deviceShadow.getDevice_id());
			msgObj2.setApp_name(deviceShadow.getApp_name());
			//msgObj2.setApp_type("shadow");
			emqttInstance.pulishMessage(msgObj2);
		}
		
		return object;
	}
	
	/// 删除影子
	public JSONObject deleteDeviceShadow(long company_id, String shadow_name) throws IOException, ServletException {
		boolean result;
		String error;
		
		if(shadow_name == null || shadow_name.isEmpty()) {
			result = false;
			error = "empty device shadow name";
		} else if(company_id == 0) {
			result = false;
			error = "empty company id";
		} else {
			try {
				int row = deviceShadowDao.deleteDeviceShadow(company_id, shadow_name);
				if(row >= 0) {
					result = true;
					error = "success";
				} else {
					result = false;
					error = "now row deleted";
				}
			} catch (RuntimeException e) {
				log.debug(e.getMessage());
				result = false;
				error = e.getMessage();
			}
		}

		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);
		
		return object;
	}
	
	/// 更新影子基本信息
	public JSONObject updateDeviceShadow(DeviceShadow deviceShadow) throws IOException, ServletException {
		boolean result;
		String error;
		
		if(deviceShadow.getShadow_name() == null || deviceShadow.getShadow_name().isEmpty()) {
			result = false;
			error = "empty device shadow name";
		} else if(deviceShadow.getCompany_id() == 0) {
			result = false;
			error = "empty company id";
		} else {
			try {
				int row = deviceShadowDao.updateDeviceShadow(deviceShadow);
				if(row > 0) {
					result = true;
					error = "success";
				} else {
					result = false;
					error = "now row updated";
				}
			} catch (RuntimeException e) {
				log.debug(e.getMessage());
				result = false;
				error = e.getMessage();
			}
		}

		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);
		
		return object;
	}

	/// 获取设备影子列表
	public JSONObject getDeviceShadowList(Pagination page, long company_id, String access_key) throws IOException, ServletException {
		JSONArray objarray = new JSONArray();
		if (company_id > 0) {
			List<DeviceShadowEx> deviceShadowList = deviceShadowDao.getDeviceShadowList(page, company_id, access_key);
			if (deviceShadowList != null) {
				for (DeviceShadowEx deviceShadow : deviceShadowList) {
					JSONObject obj = new JSONObject();
					obj.put("shadow_name", deviceShadow.getShadow_name());
					obj.put("device_id", deviceShadow.getDevice_id());
					obj.put("app_name", deviceShadow.getApp_name());
					
					String shadow_desc = deviceShadow.getShadow_desc();
					if (shadow_desc == null) {
						shadow_desc = "";
					}
					obj.put("shadow_desc", shadow_desc);
					
					String create_stamp = WebUtil.format(deviceShadow.getCreate_stamp());
					obj.put("create_stamp", create_stamp);
					String update_stamp = WebUtil.format(deviceShadow.getUpdate_stamp());
					obj.put("update_stamp", update_stamp);
					
					obj.put("access_key", deviceShadow.getAccess_key());

					objarray.add(obj);
				}
			}
		}

		JSONObject object = new JSONObject();
		object.put("items", objarray);
		if(page != null) {
			object.put("pagesize", page.getPagesize());
			object.put("current", page.getCurrent());
			object.put("total", page.getTotal());
		}

		return object;
	}
	
	public JSONObject getDeviceShadowListByDeviceId(long company_id, String device_id) {
		JSONArray objarray = new JSONArray();
		List<DeviceShadow> deviceShadowList = deviceShadowDao.getDeviceShadowListByDeviceId(company_id, device_id);
		if (deviceShadowList != null) {
			for (DeviceShadow deviceShadow : deviceShadowList) {
				JSONObject obj = new JSONObject();
				obj.put("shadow_name", deviceShadow.getShadow_name());
				obj.put("device_id", deviceShadow.getDevice_id());
				obj.put("app_name", deviceShadow.getApp_name());
				
				String shadow_desc = deviceShadow.getShadow_desc();
				if (shadow_desc == null) {
					shadow_desc = "";
				}
				obj.put("shadow_desc", shadow_desc);
				
				String create_stamp = WebUtil.format(deviceShadow.getCreate_stamp());
				obj.put("create_stamp", create_stamp);
				String update_stamp = WebUtil.format(deviceShadow.getUpdate_stamp());
				obj.put("update_stamp", update_stamp);
	
				objarray.add(obj);
			}
		}
		
		JSONObject object = new JSONObject();
		object.put("items", objarray);
		return object;
	}

	/// 新增影子属性
	public String addDeviceShadowItem(DeviceShadowItem deviceShadowItem) throws IOException, ServletException {
		boolean result;
		String error;
		
		if(deviceShadowItem.getShadow_name() == null || deviceShadowItem.getShadow_name().isEmpty()) {
			result = false;
			error = "empty device shadow name";
			return error;
		}
		if(deviceShadowItem.getItem_name() == null || deviceShadowItem.getItem_name().isEmpty()) {
			result = false;
			error = "empty device shadow item name";
			return error;
		}

		try {
			DeviceShadowItem existingDeviceShadowItem = deviceShadowItemDao.getDeviceShadowItem(deviceShadowItem.getShadow_name(), deviceShadowItem.getItem_name());
			if(existingDeviceShadowItem != null) {
				result = false;
				error = "device shadow item already exist";
			} else {
				int row = deviceShadowItemDao.addDeviceShadowItem(deviceShadowItem);
				if(row > 0) {
					result = true;
					error = "success";
				} else {
					result = false;
					error = "fail";
				}
			}
		} catch (RuntimeException e) {
			log.debug(e.getMessage());
			result = false;
			error = e.getMessage();
		}
		
		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);
		
		return object.toString();
	}
	
	/// 删除影子属性
	public String deleteDeviceShadowItem(String shadow_name, String item_name) throws IOException, ServletException {
		boolean result;
		String error;
		
		if(shadow_name == null || shadow_name.isEmpty()) {
			result = false;
			error = "empty device shadow name";
			return error;
		}
		if(item_name == null || item_name.isEmpty()) {
			result = false;
			error = "empty device shadow item name";
			return error;
		}

		try {
			int row = deviceShadowItemDao.deleteDeviceShadowItem(shadow_name, item_name);
			if(row >= 0) {
				result = true;
				error = "success";
			} else {
				result = false;
				error = "now row deleted";
			}
		} catch (RuntimeException e) {
			log.debug(e.getMessage());
			result = false;
			error = e.getMessage();
		}

		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);
		
		return object.toString();
	}
	
	/// 更新影子属性 map_name
	public String updateDeviceShadowItemMapName(DeviceShadowItem deviceShadowItem) throws IOException, ServletException {
		boolean result;
		String error;
		
		if(deviceShadowItem.getShadow_name() == null || deviceShadowItem.getShadow_name().isEmpty()) {
			result = false;
			error = "empty device shadow name";
			return error;
		}
		if(deviceShadowItem.getItem_name() == null || deviceShadowItem.getItem_name().isEmpty()) {
			result = false;
			error = "empty device shadow item name";
			return error;
		}
		if(deviceShadowItem.getItem_map_name() == null || deviceShadowItem.getItem_map_name().isEmpty()) {
			result = false;
			error = "empty device shadow item map name";
			return error;
		}

		try {
			int row = deviceShadowItemDao.updateDeviceShadowItemMapName(deviceShadowItem);
			if(row > 0) {
				result = true;
				error = "success";
			} else {
				result = false;
				error = "now row updated";
			}
		} catch (RuntimeException e) {
			log.debug(e.getMessage());
			result = false;
			error = e.getMessage();
		}

		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);
		
		return object.toString();
	}
	
	/// 根据MQTT消息更新影子属性值
	public JSONObject updateDeviceShadowItemValues(String device_id, final List<Pair<String, String>> pairs) throws IOException, ServletException {
		boolean result;
		String error;
		
		if(device_id == null || device_id.isEmpty()) {
			result = false;
			error = "empty device_id";
		}
		if(pairs == null || pairs.size() == 0) {
			result = false;
			error = "empty pairs";
		}
		
		List<DeviceShadow> deviceShadowList = deviceShadowDao.getDeviceShadowListByDeviceId(0, device_id);
		if(deviceShadowList == null || deviceShadowList.size() == 0) {
			result = false;
			error = "no device shadow to update";
		} else {		
			List<String> shadow_names = new ArrayList<String>(deviceShadowList.size());
			for(DeviceShadow shadow : deviceShadowList) {
				shadow_names.add(shadow.getShadow_name());
			}
			
			try {
				int row = deviceShadowItemDao.updateDeviceShadowValue(shadow_names, pairs);
				if(row > 0) {
					result = true;
					error = "success";
				} else {
					result = false;
					error = "fail";
				}
			} catch (RuntimeException e) {
				log.debug(e.getMessage());
				result = false;
				error = e.getMessage();
			}
		}

		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);
		
		return object;
	}
	
	//更新影子属性期望值
	public JSONObject updateDeviceShadowItemExpectValues(long company_id, String device_id, String app_name, JSONObject params) throws IOException, ServletException {
		boolean result;
		String error;
		
		if(device_id == null || device_id.isEmpty()) {
			result = false;
			error = "empty device_id";
		} else {
			Device device = deviceDao.getDevice(device_id);
			if(device.getOnline() == 0) {
				result = false;
				error = "device is offline";
			} else {			
				final List<Pair<String, String>> pairs = new ArrayList<Pair<String, String>>();
				Iterator<String> keys = params.keys();
				while(keys.hasNext()) {  
					String key = keys.next();
					String value = params.get(key).toString();
		
					pairs.add(new Pair<String, String>(key, value));
				}
				
				List<DeviceShadow> deviceShadowList = deviceShadowDao.getDeviceShadowListByDeviceId(company_id, device_id);//, app_name);
				if(deviceShadowList == null || deviceShadowList.size() == 0) {
					result = false;
					error = "no device shadow to update";
				} else {		
					List<String> shadow_names = new ArrayList<String>(deviceShadowList.size());
					for(DeviceShadow shadow : deviceShadowList) {
						shadow_names.add(shadow.getShadow_name());
					}
			
					try {
						int row = deviceShadowItemDao.updateDeviceShadowExpectValue(shadow_names, pairs);
						if(row > 0) {
							result = true;
							error = "success";
						} else {
							result = false;
							error = "fail to update shadow database";
						}
					} catch (RuntimeException e) {
						log.debug(e.getMessage());
						result = false;
						error = e.getMessage();
					}
				}
				
				//将请求发送到MQTT设备
				/*
				"desired": {
					"temprature": 29,
					"humidity": 50
				}
				*/
				if(result) {
					String topic = "$MOC/control/" + device_id + "/shadow/update";
					
					JSONObject shadowObject = new JSONObject();
					shadowObject.put("app_name", app_name);
					shadowObject.put("desired", params);
					String message = shadowObject.toString();
					
					MqttMessage msgObj = new MqttMessage();
					msgObj.setTopic(topic);
					msgObj.setMessage(message);
					msgObj.setQos(0);
					msgObj.setDevice_id(device_id);
					msgObj.setApp_name(app_name);
					//msgObj.setApp_type("shadow");
					result = emqttInstance.pulishMessage(msgObj);
					if(!result) {
						error = "failed to send mqtt message";
					}
				}
			}
		}

		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);
		
		return object;
	}
	
	/// 获取设备影子的属性列表
	public JSONObject getDeviceShadowItemList(long company_id, String shadow_name) throws IOException, ServletException {
		List<DeviceShadowItem> deviceShadowItems;
		
		if(shadow_name == null || shadow_name.isEmpty()) {
			log.debug("empty shadow name");
			deviceShadowItems = new ArrayList<DeviceShadowItem>();
		} else if(company_id == 0) {
			log.debug("empty company id");
			deviceShadowItems = new ArrayList<DeviceShadowItem>();
		} else {
			deviceShadowItems = deviceShadowItemDao.getDeviceShadowItemList(company_id, shadow_name);
		}

		JSONObject object = new JSONObject();
		object.put("items", getDeviceItemsJsonObject(deviceShadowItems));
		return object;
	}
}
