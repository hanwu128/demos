package com.lenovo.iot.devicemanager.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lenovo.iot.devicemanager.dao.DeviceDao;
import com.lenovo.iot.devicemanager.dao.SensorDao;
import com.lenovo.iot.devicemanager.model.Device;
import com.lenovo.iot.devicemanager.model.Sensor_Gps;
import com.lenovo.iot.devicemanager.model.Sensor_Label;
import com.lenovo.iot.devicemanager.model.Sensor_Temperature_Alarm;
import com.lenovo.iot.devicemanager.model.Sensor_Temperature_Day;
import com.lenovo.iot.devicemanager.model.Sensor_Temperature_Device;
import com.lenovo.iot.devicemanager.model.Tuple;
import com.lenovo.iot.devicemanager.util.OpenTSDBClient;
import com.lenovo.iot.devicemanager.util.OpenTSDBResult;
import com.lenovo.iot.devicemanager.util.Pagination;
import com.lenovo.iot.devicemanager.util.WebUtil;

@Service
public class SensorService {

	private static final String TSD_URL = "http://172.17.201.239:4242/api";
	private static final Logger log = LoggerFactory.getLogger(SensorService.class);
	private final SimpleDateFormat FF_DAY = new SimpleDateFormat("yyyyMMdd");
	private final SimpleDateFormat FF_DAY2 = new SimpleDateFormat("yyyy-MM-dd");
	private final SimpleDateFormat FF_MIN = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private SensorDao sensorDao;

	@Autowired
	private DeviceDao deviceDao;

	public JSONObject getDeviceList() throws IOException, ServletException {
		JSONObject object = new JSONObject();

		List<Sensor_Temperature_Device> deviceList = sensorDao.getDeviceList();

		JSONArray objarray = new JSONArray();
		if (deviceList != null) {
			for (Sensor_Temperature_Device dev : deviceList) {
				JSONObject obj = new JSONObject();
				obj.put("id", dev.getId());
				obj.put("device_id", dev.getDevice_id());
				obj.put("device_desc", dev.getDevice_name());
				obj.put("temperature_upper", dev.getTemperature_upper());
				obj.put("temperature_lower", dev.getTemperature_lower());
				obj.put("duration", dev.getDuration());
				obj.put("period", dev.getPeriod());

				String create_stamp = WebUtil.format(dev.getCreate_stamp());
				obj.put("create_stamp", create_stamp);

				objarray.add(obj);
			}
		}

		object.put("rows", objarray);

		return object;
	}
	
	public JSONObject update_device(JSONArray device_objects) {
		boolean result;
		String error;

		if(device_objects != null && device_objects.size() > 0) {
			List<Sensor_Temperature_Device> list = new ArrayList<Sensor_Temperature_Device>(device_objects.size());
			
			for(Object o : device_objects) {
				JSONObject obj = (JSONObject)o;
				
				Sensor_Temperature_Device device = new Sensor_Temperature_Device();
				device.setDevice_id(obj.optString("device_id"));
				device.setDevice_name(obj.optString("device_desc"));
				device.setTemperature_upper(obj.optDouble("temperature_upper"));
				device.setTemperature_lower(obj.optDouble("temperature_lower"));
				device.setPeriod(obj.optInt("period"));
				
				list.add(device);
				
				if(device.getDevice_id() == null || device.getDevice_id().isEmpty()) {
					result = true;
					error = "empty device_id";
					break;
				}
				if(device.getDevice_name() == null || device.getDevice_name().isEmpty()) {
					result = true;
					error = "empty device_desc";
					break;
				}
			}
			
			int row = sensorDao.update_device(list);
			if (row > 0) {
				result = true;
				error = "success";
			} else {
				result = false;
				error = "db error";
			}
		} else {
			result = false;
			error = "no device to update";
		}

		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);

		return object;
	}

	public boolean validStream(String device_id, String app_name) {
		int count = sensorDao.getRunningStream(device_id, app_name);
		return (count > 0);
	}

	public JSONObject registerLabel(List<Sensor_Label> list) {
		boolean result;
		String error;

		for (Sensor_Label label : list) {
			label.setCreate_stamp(new Timestamp(System.currentTimeMillis()));
			label.setUpdate_stamp(new Timestamp(System.currentTimeMillis()));
		}

		int row = sensorDao.addLabels(list);
		if (row > 0) {
			result = true;
			error = "success";
		} else {
			result = false;
			error = "fail";
		}

		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);

		return object;
	}

	public Sensor_Label getSensorLabel(String label_id) {
		Sensor_Label label = sensorDao.getSensorLabel(label_id);
		return label;
	}

	public JSONObject updateSensorLabel(List<Sensor_Label> label_list, String device_id, String app_name) {
		boolean result;
		String error;

		List<Sensor_Label> list = new ArrayList<Sensor_Label>();
		for (Sensor_Label label : label_list) {
			if (label.getDevice_id() == null) {
				label.setDevice_id("");
			}
			if (label.getApp_name() == null) {
				label.setApp_name("");
			}

			if (!label.getDevice_id().equalsIgnoreCase(device_id) || !label.getApp_name().equalsIgnoreCase(app_name)) {
				if (!label.getDevice_id().equalsIgnoreCase(device_id)) {
					label.setDevice_id(device_id);
					// 仅在标签的 device_id 发生变化时才更新时间戳（时间戳作为标签在该设备停留的时长）
					label.setUpdate_stamp(new Timestamp(System.currentTimeMillis()));
				}
				if (!label.getApp_name().equalsIgnoreCase(app_name)) {
					label.setApp_name(app_name);
				}

				list.add(label);
			}
		}

		if (list.size() > 0) {
			int row = sensorDao.updateSensorLabel(list);

			if (row > 0) {
				result = true;
				error = "success";
			} else {
				result = false;
				error = "failed to update label";
				System.out.println(error);
			}
		} else {
			result = true;
			error = "success";
		}

		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);

		return object;
	}

	public JSONObject addTemperatureDay(String device_id, String app_name, JSONObject reported, long timestamp) throws IOException, ServletException {
		Timestamp sensor_stamp;
		if (timestamp > 0) {
			sensor_stamp = new Timestamp(timestamp);
		} else {
			sensor_stamp = new Timestamp(System.currentTimeMillis());
		}

		Sensor_Temperature_Device device = sensorDao.getDevice(device_id);

		// final List<Sensor_Temperature> pairs = new
		// ArrayList<Sensor_Temperature>();
		final List<Sensor_Temperature_Day> pairs_day = new ArrayList<Sensor_Temperature_Day>();

		Date d = new Date();
		String today = FF_DAY.format(d);

		Iterator<String> keys = reported.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			Object value = reported.get(key);

			Double current_value = Double.parseDouble(value.toString());
			if (Math.abs(current_value - 99.99d) < 0.0000001) { // 99.99为非法温度数据
				current_value = 0d;
			}

			// //历史记录
			// Sensor_Temperature sensor = new Sensor_Temperature();
			// sensor.setLabel_id(label_id);
			// sensor.setValue(current_value);
			// sensor.setSensor_stamp(sensor_stamp);
			// sensor.setDevice_id(device_id);
			// pairs.add(sensor);

			// 报警
			int status = 0;
			if (device != null) {
				// 温度超高/超低报警
				if ((device.getTemperature_upper() != 0 || device.getTemperature_lower() != 0) && (current_value > device.getTemperature_upper() || current_value < device.getTemperature_lower())) {
					double limit_value = 0d;
					if (current_value > device.getTemperature_upper()) {
						status = 1; // 温度过高
						limit_value = device.getTemperature_upper();
					} else {
						status = -1; // 温度过低
						limit_value = device.getTemperature_lower();
					}
					Sensor_Temperature_Alarm alarm = new Sensor_Temperature_Alarm();
					alarm.setDevice_id(device_id);
					alarm.setLabel_id(key);
					alarm.setType(status);
					alarm.setValue(current_value);
					alarm.setValue_limit(limit_value);
					alarm.setCreate_stamp(new Timestamp(System.currentTimeMillis()));

					sensorDao.addAlarm(alarm);
				}
			}

			// 当日
			Sensor_Temperature_Day day = sensorDao.getSensorTemperatureDay(key, today);
			if (day == null) {
				day = new Sensor_Temperature_Day();
				day.setId(0l);
				day.setLabel_id(key);
				day.setCurrent_value(current_value);
				day.setMax_value(current_value);
				day.setMin_value(current_value);
				day.setWhat_day(today);
				day.setUpdate_stamp(sensor_stamp);
				day.setMax_stamp(null);
				day.setMin_stamp(null);
				day.setLast_value(0d);
				day.setLast_status(0);
				day.setStatus(status);
			} else {
				day.setLast_value(day.getCurrent_value());
				day.setCurrent_value(current_value);
				day.setUpdate_stamp(sensor_stamp);
				if (Math.abs(day.getMax_value()) < 0.0000001 || current_value > day.getMax_value()) {
					day.setMax_value(current_value);
					day.setMax_stamp(sensor_stamp);
				}
				if (Math.abs(day.getMin_value()) < 0.0000001 || current_value < day.getMin_value()) {
					day.setMin_value(current_value);
					day.setMin_stamp(sensor_stamp);
				}
				day.setLast_status(day.getStatus());
				day.setStatus(status);
			}
			
			pairs_day.add(day);
		}

		JSONObject object = new JSONObject();
		boolean result;
		String error;
		if (pairs_day.size() > 0) {
			//更新当日实时状态
			int row = sensorDao.addTemperatureDay(/* pairs, */pairs_day);
			if (row > 0) {
				long ts_s = timestamp;
				if (ts_s > 1000000000000L) {
					ts_s = ts_s / 1000;
				}

				// 写入 OpenTSDB
				JSONArray datapoints = new JSONArray();
				for (Sensor_Temperature_Day day : pairs_day) {
					String label_id = day.getLabel_id();
					double current_value = day.getCurrent_value();
					int status = day.getStatus();

					JSONObject tags = new JSONObject();
					tags.put("device", device_id);
					tags.put("label", label_id);
					tags.put("status", status);

					JSONObject datapoint = new JSONObject();
					datapoint.put("metric", app_name);
					datapoint.put("tags", tags);
					datapoint.put("timestamp", ts_s);
					datapoint.put("value", current_value);

					datapoints.add(datapoint);
				}

				if (datapoints.size() > 0) {
					OpenTSDBResult tsdb_response = OpenTSDBClient.httpPost(TSD_URL + "/put", datapoints.toString());
					if (tsdb_response.getStatus_code() != 204) {
						System.out.println(tsdb_response.getResponse());
					};
				} else {
					System.out.println("warning: temperature no data point.");
				}

				result = true;
				error = "success";
				object.put("count", row);
			} else {
				result = false;
				error = "failed to update temperature";

				StringBuilder sb = new StringBuilder();
				for (Sensor_Temperature_Day day : pairs_day) {
					sb.append(day.getLabel_id() + ":" + day.getCurrent_value());
				}
				error = error + sb.toString();
			}
		} else {
			result = true;
			error = "warning: now row added";
		}

		object.put("result", result);
		object.put("error", error);

		return object;
	}

	public JSONObject addGps(String device_id, String app_name, JSONObject reported, long timestamp) throws IOException, ServletException {
		Timestamp sensor_stamp;
		if (timestamp > 0) {
			sensor_stamp = new Timestamp(timestamp);
		} else {
			sensor_stamp = new Timestamp(System.currentTimeMillis());
		}

		final Sensor_Gps gps = new Sensor_Gps();
		gps.setDevice_id(device_id);
		gps.setLongitude(reported.getDouble("longitude"));
		gps.setLatitude(reported.getDouble("latitude"));
		gps.setGps_stamp(new Timestamp(reported.getLong("timestamp")));
		gps.setDevice_stamp(sensor_stamp);
		gps.setServer_stamp(new Timestamp(System.currentTimeMillis()));

		int row = sensorDao.addGps(gps);

		boolean result;
		String error;
		if (row > 0) {
			long ts_s = timestamp;
			if (ts_s > 1000000000000L) {
				ts_s = ts_s / 1000;
			}

			// 写入 OpenTSDB
			JSONArray datapoints = new JSONArray();
			Iterator<String> keys = reported.keys();
			while(keys.hasNext()) {
				String key = keys.next();
				Object value = reported.get(key);
				
		    	JSONObject tags = new JSONObject();
		    	tags.put("device", device_id);
		    	tags.put("gps", key);
	
		    	JSONObject datapoint = new JSONObject();
		    	datapoint.put("metric", app_name);
		    	datapoint.put("timestamp", ts_s);
		    	datapoint.put("value", value);
		    	datapoint.put("tags", tags);
		    	
		    	datapoints.add(datapoint);
			}	
			
			if (datapoints.size() > 0) {
				OpenTSDBResult tsdb_response = OpenTSDBClient.httpPost(TSD_URL + "/put", datapoints.toString());
				if (tsdb_response.getStatus_code() != 204) {
					System.out.println(tsdb_response.getResponse());
				};
			} else {
				System.out.println("warning: gps no data point.");
			}
			
			result = true;
			error = "success";
		} else {
			result = false;
			error = "fail";
		}

		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);

		return object;
	}

	public JSONObject set_real_alarm(Date timestamp) {
		boolean result;
		String error;

		int row = sensorDao.updateAlarmStatus(new Timestamp(timestamp.getTime()));
		if (row >= 0) {
			result = true;
			error = "success";
		} else {
			result = false;
			error = "failed to set alarm flag";
		}

		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);

		return object;
	}

	public JSONObject get_real_alarm_brief() {
		JSONObject result = new JSONObject();

		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(System.currentTimeMillis());
		now.add(Calendar.DAY_OF_YEAR, -7);

		Pagination page = new Pagination();
		page.setPagesize(2);
		page.setCurrent(1);
		List<Sensor_Temperature_Alarm> alarmList = sensorDao.get_real_alarm(page, now.getTime());
		if (alarmList.size() > 0) {
			Sensor_Temperature_Alarm alarm = alarmList.get(0);
			String create_stamp = WebUtil.format(alarm.getCreate_stamp());
			result.put("timestamp", create_stamp);

			// long[] ids = new long[alarmList.size()];
			// for(int i = 0; i < alarmList.size(); i++) {
			// ids[i] = alarmList.get(i).getId();
			// }
			// result.put("ids", ids);
		}

		result.put("count", page.getTotal());

		return result;
	}

	public JSONObject get_real_alarm(Pagination page) {
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(System.currentTimeMillis());
		now.add(Calendar.DAY_OF_YEAR, -7);
		List<Sensor_Temperature_Alarm> alarmList = sensorDao.get_real_alarm(page, now.getTime());

		JSONArray objarray = new JSONArray();
		for (Sensor_Temperature_Alarm alarm : alarmList) {
			JSONObject obj = new JSONObject();
			obj.put("id", alarm.getId());
			obj.put("device_id", alarm.getDevice_id());
			obj.put("device_name", alarm.getDevice_name());
			obj.put("label_id", alarm.getLabel_id());
			obj.put("type", alarm.getType());
			obj.put("value", alarm.getValue());
			obj.put("value_limit", alarm.getValue_limit());

			String create_stamp = WebUtil.format(alarm.getCreate_stamp());
			obj.put("create_stamp", create_stamp);

			objarray.add(obj);
		}

		JSONObject object = new JSONObject();
		object.put("rows", objarray);
		if (page != null) {
			object.put("pagesize", page.getPagesize());
			object.put("current", page.getCurrent());
			object.put("total", page.getTotal());
		}

		return object;
	}

	public JSONObject getAlarmList(Pagination page, String device_id, Date day_begin, Date day_end) {
		List<Sensor_Temperature_Alarm> alarmList = sensorDao.getAlarmList(page, device_id, day_begin, day_end);

		JSONArray objarray = new JSONArray();
		for (Sensor_Temperature_Alarm alarm : alarmList) {
			JSONObject obj = new JSONObject();
			obj.put("id", alarm.getId());
			obj.put("device_id", alarm.getDevice_id());
			obj.put("device_name", alarm.getDevice_name());
			obj.put("label_id", alarm.getLabel_id());
			obj.put("type", alarm.getType());
			obj.put("value", alarm.getValue());
			obj.put("value_limit", alarm.getValue_limit());

			String create_stamp = WebUtil.format(alarm.getCreate_stamp());
			obj.put("create_stamp", create_stamp);

			objarray.add(obj);
		}
		JSONObject object = new JSONObject();
		object.put("rows", objarray);
		if (page != null) {
			object.put("pagesize", page.getPagesize());
			object.put("current", page.getCurrent());
			object.put("total", page.getTotal());
		}

		return object;
	}

	public String get_breif() {
		int device_count = deviceDao.getDeviceCount_ColdChain();
		int label_count = sensorDao.getSensorLabelCount();
		int label_data_count = sensorDao.getSensor_Temperature_History_Count();
		int label_data_invalid_count = sensorDao.getSensor_Temperature_History_Invalid_Count();

		JSONObject object = new JSONObject();
		object.put("device_count", device_count);
		object.put("label_count", label_count);
		object.put("label_data_count", label_data_count);
		object.put("label_data_invalid_count", label_data_invalid_count);

		return object.toString();
	}

	public JSONObject getGPS(String device_id) {
		final JSONObject obj = new JSONObject();
		obj.put("device_id", device_id);

		final Sensor_Gps gps = sensorDao.getGPS(device_id);
		if (gps != null) {
			// {"longitude":116.403694, "latitude":39.916042, gpstime:
			// "2017-09-27 15:50:30", "status":1}
			obj.put("longitude", gps.getLongitude());
			obj.put("latitude", gps.getLatitude());
			obj.put("gpstime", FF_MIN.format(gps.getGps_stamp()));
		} else {
			obj.put("longitude", 0);
			obj.put("latitude", 0);
			obj.put("gpstime", "");
		}

		Device device = deviceDao.getDevice(device_id);
		if (device != null) {
			obj.put("status", device.getOnline());
		} else {
			obj.put("status", 0L);
		}
		return obj;
	}

	public JSONArray get_data_proportion(String device_id) {
		long valid_count = sensorDao.get_data_count(device_id, true);
		long invalid_count = sensorDao.get_data_count(device_id, false);

		JSONObject o1 = new JSONObject();
		o1.put("name", "正常采集");
		o1.put("value", valid_count);

		JSONObject o2 = new JSONObject();
		o2.put("name", "异常采集");
		o2.put("value", invalid_count);

		JSONArray array = new JSONArray();
		array.add(o1);
		array.add(o2);

		// final JSONObject obj = new JSONObject();
		// obj.put("device_id", device_id);

		return array;
	}

	public JSONObject get_data_trend(String startTimeAt, String endTimeAt, double max_value, double min_value) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(FF_DAY2.parse(endTimeAt));
			c.add(Calendar.DATE, 1);

			endTimeAt = FF_DAY2.format(c.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Tuple<String, Integer, Integer>> list = sensorDao.get_data_trend(startTimeAt, endTimeAt, max_value, min_value);

		List<String> listYString_Time = new ArrayList<String>();
		List<Integer> listX_High = new ArrayList<Integer>();
		List<Integer> listX_Low = new ArrayList<Integer>();

		// for(Tuple<String,Integer,Integer> t : list) {
		// listYString_Time.add(t.getV1());
		// listX_High.add(t.getV2());
		// listX_Low.add(t.getV3());
		// }

		int k = 0;
		for (int i = 0; i < 24; i++) {
			if (list.size() < i + 1) {
				listYString_Time.add(String.format("%02d", i + 1));
				listX_High.add(null);
				listX_Low.add(null);
			} else {
				Tuple<String, Integer, Integer> t = list.get(k);
				int hour = Integer.parseInt(t.getV1());
				if (hour == i) {
					listYString_Time.add(t.getV1());
					listX_High.add(t.getV2());
					listX_Low.add(t.getV3());
				} else if (hour > i) {
					listYString_Time.add(String.format("%02d", i + 1));
					listX_High.add(null);
					listX_Low.add(null);
				}

				k++;
			}
		}

		JSONObject _JSONObjectMap = new JSONObject();
		_JSONObjectMap.put("X", listYString_Time);

		JSONArray y = new JSONArray();
		JSONObject y1 = new JSONObject();
		y1.put("data", listX_High);
		y1.put("name", "超高异常");
		y.add(y1);
		JSONObject y2 = new JSONObject();
		y2.put("data", listX_Low);
		y2.put("name", "超低异常");
		y.add(y2);

		_JSONObjectMap.put("Y", y);

		return _JSONObjectMap;
	}
}