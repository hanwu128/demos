package com.lenovo.iot.devicemanager.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lenovo.iot.devicemanager.dao.DeviceDao;
import com.lenovo.iot.devicemanager.model.Device;
import com.lenovo.iot.devicemanager.model.DeviceEx;
import com.lenovo.iot.devicemanager.model.DeviceImport;
import com.lenovo.iot.devicemanager.model.DeviceOnlineHistory;
import com.lenovo.iot.devicemanager.model.MqttDeviceMessages;
import com.lenovo.iot.devicemanager.model.MqttMessage;
import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;
import com.lenovo.iot.devicemanager.model.bo.App;
import com.lenovo.iot.devicemanager.model.bo.StreamingCallbackFlight;
import com.lenovo.iot.devicemanager.mqtt.EmqttInstance;
import com.lenovo.iot.devicemanager.util.Md5;
import com.lenovo.iot.devicemanager.util.Pagination;
import com.lenovo.iot.devicemanager.util.ReadProperties;
import com.lenovo.iot.devicemanager.util.WebUtil;

@Service
public class DeviceService {
	private static final Logger log = LoggerFactory.getLogger(DeviceService.class);
	
	private static List<String> TOPIC_GROUPS = null;

	@Autowired
	private DeviceDao deviceDao;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private DeviceShadowService deviceShadowService;
	
	@Autowired
	private IApacheAdgentMetaDataService _ApacheAdgentMetaDataServiceImpl;
	@Autowired
	private IStreamingDeviceAppService _StreamingDeviceAppServiceImpl;
	@Autowired
	private IAppService _AppServiceImpl;
    
    @Autowired
    private EmqttInstance emqttInstance;

	public JSONObject getDevice(long company_id, String device_id) throws IOException, ServletException {
		if (device_id == null || device_id.isEmpty()) {
			log.debug("empty device id");
			return JSONObject.fromObject(new Device());
		} else if (company_id == 0) {
			log.debug("empty company id");
			return JSONObject.fromObject(new Device());
		} else {
			Device device = deviceDao.getDevice(device_id);
			if (device != null) {
				if(device.getCompany_id() != company_id) {
					log.debug("device is not in company");
					return JSONObject.fromObject(new Device());
				}
				
				JSONObject object = getDeviceJsonObject(device);

				ApacheAdgentMetaData metaData = new ApacheAdgentMetaData();
				metaData.setHardware_device_id(device_id);
				List<ApacheAdgentMetaData> metaDataList = _ApacheAdgentMetaDataServiceImpl.findApacheAdgentMetaDataByParameter_deviceid(metaData);
				if (metaDataList != null && metaDataList.size() > 0) {
					ApacheAdgentMetaData metaResult = metaDataList.get(0);
					if(metaResult.getEdgent_agent_app_list() != null && !metaResult.getEdgent_agent_app_list().isEmpty()) {
						//获取app对应的 streaming信息
						StreamingCallbackFlight streamingCallbackFlight = new StreamingCallbackFlight();
						streamingCallbackFlight.setDeviceidcode(device_id);
				        List<StreamingCallbackFlight> listStreamingCallbackFlight =  _StreamingDeviceAppServiceImpl.getDeviceStreamings(streamingCallbackFlight);

				        JSONArray objAppList_new = new JSONArray();
				        
						JSONArray objApplist = JSONArray.fromObject(metaResult.getEdgent_agent_app_list());
						for (int i = 0; i < objApplist.size(); i++) {
							JSONObject objApp = (JSONObject) objApplist.get(i);
							String app_name = objApp.getString("app_name");
							//String app_version = objApp.getString("app_version");
							
							boolean found = false;
							for(StreamingCallbackFlight sf : listStreamingCallbackFlight) {
								//因为设备上不可能运行app的多个版本，因此无需判断版本号
								if(sf.getAppname() != null && sf.getAppname().equalsIgnoreCase(app_name) /*&& sf.getAppversion().equalsIgnoreCase(app_version)*/) {
									String streaming_name = sf.getStreamingname();
									String status = sf.getStatus();
									objApp.put("streaming_name", streaming_name);
									objApp.put("status", status);
									
									objAppList_new.add(objApp);
									found = true;
									break;
								}
							}
							if(!found) {
								objApp.put("streaming_name", "");
								objApp.put("status", "");
								
								objAppList_new.add(objApp);
							}
						}
						
						metaResult.setEdgentagentapplist(objAppList_new.toString());
					}
					JSONObject objMeta = JSONObject.fromObject(metaResult);
					object.put("meta", objMeta);
				} else {
					JSONObject objMeta = JSONObject.fromObject(new ApacheAdgentMetaData());
					object.put("meta", objMeta);
				}

				return object;
			} else {
				log.debug("no device found");
				return JSONObject.fromObject(new Device());
			}
		}
	}

	private JSONObject getDeviceJsonObject(Device device) {
		JSONObject object = new JSONObject();
		if (device != null) {
			object.put("id", device.getId());
			object.put("device_id", device.getDevice_id());
			object.put("company_id", device.getCompany_id());
			// object.put("policy_name", device.getPolicy_name());
			object.put("access_key", device.getAccess_key());
			object.put("secret_key", device.getSecret_key());
			object.put("topic_group", device.getTopic_group());

			String device_desc = device.getDevice_desc();
			if (device_desc == null) {
				device_desc = "";
			}
			object.put("device_desc", device_desc);

			String create_stamp = WebUtil.format(device.getCreate_stamp());
			object.put("create_stamp", create_stamp);
			String update_stamp = WebUtil.format(device.getUpdate_stamp());
			object.put("update_stamp", update_stamp);
			object.put("status", device.getOnline());
		}

		return object;
	}

	// for mq sdk
//	private String genDevice_id_v2(long company_id, String access_key) {
//		String id_ver = "2";
//		String id_company = String.format("%010d", company_id);		
//		String id = id_ver + id_company + "$" + access_key;
//		
//		return id;
//	}

	private String genDevice_id(long company_id, String accessKey) {
		String now = Long.toString(System.currentTimeMillis());
		String id_md5 = Md5.encryption(accessKey + now);
		String id_ver = "1";
		String id_company = String.format("%010d", company_id);
		String id_right = now.substring(now.length() - 6, now.length() - 1);
		String id = id_md5.substring(8, 24).toUpperCase();
		
		id = id_ver + id_company + id + id_right;
		return id;
	}

	private String genSecret_key(String Access_key) {
		String uuid = UUID.randomUUID().toString();
		// String secret_key = Md5.encryption(Access_key + "_"
		// +System.currentTimeMillis());
		// secret_key = secret_key.substring(8, 24);
		// secret_key = Base64.encodeBase64String(secret_key.getBytes());
		return uuid.replace("-", "").toUpperCase();
	}

	private String genTopic_group(String device_id) {
		if(TOPIC_GROUPS == null) {
			//ReadProperties _ReadProperties = new ReadProperties();
			//String file_server = _ReadProperties.readProperties("/mqtt.conf", "topic_group");
			TOPIC_GROUPS =  Arrays.asList(emqttInstance.getDeviceTopicGroup().split(","));
		}
		
		String topic_group = "device";
		
		String x = device_id.substring(device_id.length() - 3, device_id.length());
		try {
			int a = Integer.parseInt(x);
			int i = a % TOPIC_GROUPS.size();
			topic_group = TOPIC_GROUPS.get(i);
		} catch (Exception e) {
			log.debug(e.getMessage());
			e.printStackTrace();
		}
		
		return topic_group;
	}

	public JSONObject registerDevice(long company_id, String company_sk, String access_key) throws IOException, ServletException {
		if (company_id == 0) {
			JSONObject object = new JSONObject();
			object.put("result", false);
			object.put("error", "empty company_id");

			return object;
		}
		if (company_sk == null || company_sk.isEmpty()) {
			JSONObject object = new JSONObject();
			object.put("result", false);
			object.put("error", "empty company_sk");

			return object;
		}
		if (access_key == null || access_key.isEmpty()) {
			JSONObject object = new JSONObject();
			object.put("result", false);
			object.put("error", "empty access_key");

			return object;
		}
		
		// 验证 company sk
		if(companyService.validSecretKey(company_id, company_sk)) {
			try {
				Device existingDevice = deviceDao.getDeviceByAccessKey(company_id, access_key);
				if (existingDevice != null) {
					String broker = emqttInstance.getBrokerUrl();
					int pos_last = broker.lastIndexOf(':');
					String port = broker.substring(pos_last + 1);
					broker = broker.substring(0, pos_last);
					
					// JSONObject object = getDeviceJsonObject(existingDevice);
					JSONObject object = new JSONObject();
					object.put("device_id", existingDevice.getDevice_id());
					object.put("company_id", existingDevice.getCompany_id());
					object.put("broker", broker);
					object.put("port", Integer.parseInt(port));
					object.put("access_key", existingDevice.getAccess_key());
					object.put("secret_key", existingDevice.getSecret_key());
					object.put("group", existingDevice.getTopic_group());
					object.put("keep_alive", 90);
					object.put("clean_session", false);
	
					object.put("result", true);
	
					return object;
				} else {
					Device device = new Device();
					device.setDevice_id(genDevice_id(company_id, access_key));
					// device.setPolicy_name(policy_name);
					// device.setDevice_desc(device_desc);
					device.setAccess_key(access_key);
	
					device.setCompany_id(company_id);
					// 生成 SecretKey
					// String keyPair[] = RSA.generateRSAKeyPair();
					// device.setSecret_key(keyPair[1]);
					// device.setPublic_key(keyPair[0]);
					device.setSecret_key(genSecret_key(access_key));
					
					device.setTopic_group(genTopic_group(device.getDevice_id()));
	
					int row = deviceDao.addDevice(device);
					if (row > 0) {
						String broker = emqttInstance.getBrokerUrl();
						int pos_last = broker.lastIndexOf(':');
						String port = broker.substring(pos_last + 1);
						broker = broker.substring(0, pos_last);
						
						// JSONObject object = getDeviceJsonObject(device);
						JSONObject object = new JSONObject();
						object.put("device_id", device.getDevice_id());
						object.put("company_id", device.getCompany_id());
						object.put("broker", broker);
						object.put("port", Integer.parseInt(port));
						object.put("access_key", device.getAccess_key());
						object.put("secret_key", device.getSecret_key());
						object.put("group", device.getTopic_group());
						object.put("keep_alive", 90);
						object.put("clean_session", false);
	
						object.put("result", true);
	
						return object;
					} else {
						JSONObject object = new JSONObject();
						object.put("result", false);
						object.put("error", "failed to insert new device");
	
						return object;
					}
				}
			} catch (RuntimeException e) {
				log.debug(e.getMessage());
				JSONObject object = new JSONObject();
				object.put("result", false);
				object.put("error", e.getMessage());
	
				return object;
			}
		} else {
			JSONObject object = new JSONObject();
			object.put("result", false);
			object.put("error", "wrong secret key");

			return object;
		}
	}

	public JSONObject addDevice(Device device) throws IOException, ServletException {
		boolean result;
		String error;

//		if (device.getAccess_key() == null || device.getAccess_key().isEmpty()) {
//			result = false;
//			error = "101"; //"empty device id";
//		} else 
		if (device.getAccess_key() == null || device.getAccess_key().isEmpty()) {
			result = false;
			error = "102"; //"empty device access key";
		} else if (device.getCompany_id() == 0) {
			result = false;
			error = "100"; //"empty company id";
		} else {
			// 生成 Device Id
			device.setDevice_id(genDevice_id(device.getCompany_id(), device.getAccess_key()));
			// 生成 SecretKey
			// String keyPair[] = RSA.generateRSAKeyPair();
			// device.setSecret_key(keyPair[1]);
			// device.setPublic_key(keyPair[0]);
			device.setSecret_key(genSecret_key(device.getAccess_key()));
			
			device.setTopic_group(genTopic_group(device.getDevice_id()));

			try {
				if (deviceDao.getDeviceByAccessKey(device.getCompany_id(), device.getAccess_key()) != null) {
					result = false;
					error = "103"; //"access key already exists";
				} else {
					int row = deviceDao.addDevice(device);
					if (row > 0) {
						result = true;
						error = "0";
					} else {
						result = false;
						error = "104"; //database error
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

		return object;
	}
	
	public JSONObject addDevice_batch_check(long company_id, InputStream sis) {
		try {
			final int BUFFER_SIZE = 8 * 1024;
			byte[] buffer = new byte[BUFFER_SIZE];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int length = sis.read(buffer);
			while (length > 0) {
				baos.write(buffer, 0, length);
				length = sis.read(buffer);
			}
	
			InputStream stream = new ByteArrayInputStream(baos.toByteArray()); 
			ImportService importService = new ImportService();
			JSONObject result = importService.checkImportDevice(stream);
			boolean ok = result.getBoolean("result");
			if(ok) {
				//模板格式校验通过，进行设备名称重复检查
				List<DeviceImport> error_device_list = new ArrayList<DeviceImport>();
				
				JSONArray deviceArray = result.optJSONArray("rows");
				List<DeviceImport> di_list = JSONArray.toList(deviceArray, DeviceImport.class);
				for(DeviceImport di : di_list) {
					try {
						if (deviceDao.getDeviceByAccessKey(company_id, di.getAccess_key()) != null) {
							DeviceImport ed = new DeviceImport();
							ed.setError(true);
							ed.setError_message("设备名称已存在");
							ed.setRow(di.getRow());
							ed.setAccess_key(di.getAccess_key());
							ed.setDevice_desc(di.getDevice_desc());
							error_device_list.add(ed);
						}
					} catch (RuntimeException e) {
						DeviceImport ed = new DeviceImport();
						ed.setError(true);
						ed.setError_message("设备名称检查失败");
						ed.setRow(di.getRow());
						ed.setAccess_key(di.getAccess_key());
						ed.setDevice_desc(di.getDevice_desc());
						error_device_list.add(ed);
					}
				}
				
				if(error_device_list.size() > 0) {
					JSONObject obj = new JSONObject();
					obj.put("rows", error_device_list);
					obj.put("result", false);
					obj.put("error", "设备名称已存在");
					return obj;
				} else {
					return result;
				}
			} else {
				return result;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			JSONObject obj = new JSONObject();
			obj.put("result", false);
			obj.put("error", e.getMessage());
			return obj;
		}
	}
	
	public JSONObject addDevice_batch(long company_id, JSONArray deviceArray) {
		boolean result = true;
		String error = "";
		
		List<DeviceImport> di_list = JSONArray.toList(deviceArray, DeviceImport.class);
		if(di_list != null && di_list.size() > 0) {
			List<Device> device_list = new ArrayList<Device>();
			for(DeviceImport di : di_list) {
				Device d = new Device();
				d.setCompany_id(company_id);
				d.setAccess_key(di.getAccess_key());
				d.setDevice_desc(di.getDevice_desc());
				
				if (d.getAccess_key() == null || d.getAccess_key().isEmpty()) {
					result = false;
					error = "102"; //"empty device access key";
				} else if (d.getCompany_id() == 0) {
					result = false;
					error = "100"; //"empty company id";
				} else {
					// 生成 Device Id
					d.setDevice_id(genDevice_id(d.getCompany_id(), d.getAccess_key()));
					d.setSecret_key(genSecret_key(d.getAccess_key()));
					d.setTopic_group(genTopic_group(d.getDevice_id()));
					try {
						if (deviceDao.getDeviceByAccessKey(d.getCompany_id(), d.getAccess_key()) != null) {
							result = false;
							error = "103"; //"access key already exists";
							
							break;
						} else {
							device_list.add(d);
						}
					} catch (RuntimeException e) {
						log.debug(e.getMessage());
						result = false;
						error = e.getMessage();
					}
				}
			}
			
			//设备均合法
			if(result) {
				result = false;
				int rows = deviceDao.addDevices(device_list);
				if (rows > 0) {
					result = true;
					error = "";
				} else {
					result = false;
					error = "104"; //database error
				}
			}
		} else {
			result = false;
			error = "101";
		}
		
		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);
		
		return object;
	}

	public JSONObject deleteDevice(long company_id, String device_id) throws IOException, ServletException {
		boolean result;
		String error;

		if (device_id == null || device_id.isEmpty()) {
			result = false;
			error = "empty device id";
		} else if (company_id == 0) {
			result = false;
			error = "empty company id";
		} else {
			try {
				Device existingDevice = deviceDao.getDevice(device_id);
				if(existingDevice != null) {
					if(existingDevice.getCompany_id() != company_id) {
						result = false;
						error = "device not in company";
					} else if(existingDevice.getOnline() == 0) {
						int row = deviceDao.deleteDevice(device_id);
						if (row >= 0) {
							result = true;
							error = "success";
							
							//删除数据流
							StreamingCallbackFlight streaming = new StreamingCallbackFlight();
							streaming.setIslive("false");
							streaming.setDeviceid(existingDevice.getId());
							List<StreamingCallbackFlight> listForStreamingCallbackFlight = _StreamingDeviceAppServiceImpl.findStreamingDeviceAppVOBaseByParameters(streaming);
							if(listForStreamingCallbackFlight != null && listForStreamingCallbackFlight.size() > 0) {
								for(StreamingCallbackFlight s : listForStreamingCallbackFlight) {
									_StreamingDeviceAppServiceImpl.deleteStreamingDeviceApp(company_id, s.getTopid());
								}
							}
							//删除设备镜像
							JSONObject deviceShadowList = deviceShadowService.getDeviceShadowListByDeviceId(company_id, device_id);
							JSONArray objarray = deviceShadowList.optJSONArray("items");
							if(objarray != null && objarray.size() > 0) {
								for(Object shadow : objarray) {
									JSONObject s = (JSONObject)shadow;
									String shadow_name = s.getString("shadow_name");
									deviceShadowService.deleteDeviceShadow(company_id, shadow_name);
								}
							}
						} else {
							result = false;
							error = "now row deleted";
						}
					} else {
						result = false;
						error = "device is online";
					}
				} else {
					result = false;
					error = "device " + device_id + " not exist";
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

//	public JSONObject updateDevice(Device device) throws IOException, ServletException {
//		boolean result;
//		String error;
//
//		if (device.getDevice_id() == null || device.getDevice_id().isEmpty()) {
//			result = false;
//			error = "empty device id";
//		} else if (device.getCompany_id() == 0) {
//			result = false;
//			error = "empty company id";
//		} else {
//			try {
//				int row = deviceDao.updateDevice(device);
//				if (row > 0) {
//					result = true;
//					error = "success";
//				} else {
//					result = false;
//					error = "fail";
//				}
//			} catch (RuntimeException e) {
//				log.debug(e.getMessage());
//				result = false;
//				error = e.getMessage();
//			}
//		}
//
//		JSONObject object = new JSONObject();
//		object.put("result", result);
//		object.put("error", error);
//
//		return object;
//	}

	public JSONObject updateDeviceSecretKey(String device_id, long company_id, String secret_old) throws IOException, ServletException {
		boolean result;
		String error;

		if (device_id == null || device_id.isEmpty()) {
			result = false;
			error = "empty device id";
		} else if (company_id == 0) {
			result = false;
			error = "empty company id";
		} else {
			Device device = deviceDao.getDevice(device_id);
			if (device == null) {
				result = false;
				error = "no device found";
			} else if(device.getCompany_id() != company_id) {
				result = false;
				error = "device not in company";
			} else if (!device.getSecret_key().equals(secret_old)) {
				result = false;
				error = "wrong secret";
			} else {
				// String keyPair[] = RSA.generateRSAKeyPair();
				String secret_Key = genSecret_key(device.getAccess_key());
				try {
					int row = deviceDao.updateDeviceSecretKey(device_id, company_id, secret_Key);
					if (row > 0) {
						result = true;
						error = secret_Key;
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
		}

		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);

		return object;
	}

	public JSONObject getDeviceListData(Pagination page, long company_id, String access_key, String adgent_version, boolean online_only) throws IOException, ServletException {
		JSONArray objarray = new JSONArray();
		if(company_id > 0) {
			List<DeviceEx> deviceList = deviceDao.getDeviceListEx(page, company_id, access_key, adgent_version, online_only);
			if (deviceList != null) {
				for (DeviceEx dev : deviceList) {
					JSONObject obj = new JSONObject();
					obj.put("id", dev.getId());
					obj.put("device_id", dev.getDevice_id());
					//obj.put("leap_id", dev.getLeap_id());
					obj.put("policy_name", dev.getPolicy_name());
					obj.put("access_key", dev.getAccess_key());
					// obj.put("secret_key", dev.getSecret_key());
					obj.put("topic_group", dev.getTopic_group());
	
					String device_desc = dev.getDevice_desc();
					if (device_desc == null) {
						device_desc = "";
					}
					obj.put("device_desc", device_desc);
	
					String create_stamp = WebUtil.format(dev.getCreate_stamp());
					obj.put("create_stamp", create_stamp);
					String update_stamp = WebUtil.format(dev.getUpdate_stamp());
					obj.put("update_stamp", update_stamp);
					obj.put("status", dev.getOnline());
					obj.put("edgent_version", dev.getEdgent_version());
	
					objarray.add(obj);
				}
			}
		}

		JSONObject object = new JSONObject();
		object.put("rows", objarray);
		if(page != null) {
			object.put("pagesize", page.getPagesize());
			object.put("current", page.getCurrent());
			object.put("total", page.getTotal());
		}

		return object;
	}
	
	public JSONArray device_list_version() {
		List<String> result = deviceDao.getEdgentVersionList();
		JSONArray obj = JSONArray.fromObject(result);
		return obj;
	}
	
	//待作废
//	public List<App> get_applist(Long did, String device_id, long company_id, String app_type) {
//		App _app = new App();
//		_app.setApptype(app_type);
//		List<App> listApp = _AppServiceImpl.findAppByParameter(_app);
//		
//		if(did > 0l) {
//			Device device = deviceDao.getDeviceByPrimaryKey(did);
//			if(device != null) {
//				device_id = device.getDevice_id();
//			}
//		}
//		
//		List<App> listApp_new = new ArrayList<App>();
//		if(device_id != null && !device_id.isEmpty()) {
//	        ApacheAdgentMetaData _ApacheAdgentMetaDataParameters = new ApacheAdgentMetaData();
//	        _ApacheAdgentMetaDataParameters.setHardware_device_id(device_id);
//	        List<ApacheAdgentMetaData> listApacheAdgentMetaData = _ApacheAdgentMetaDataServiceImpl.findApacheAdgentMetaDataByParameter_deviceid(_ApacheAdgentMetaDataParameters);
//	        if(listApacheAdgentMetaData != null && listApacheAdgentMetaData.size() == 1) {
//	        	String applist = listApacheAdgentMetaData.get(0).getEdgent_agent_app_list();
//	        	JSONArray objApplist = JSONArray.fromObject(applist);
//	        	for (int i = 0; i < objApplist.size(); i++) {
//	        		JSONObject objApp = (JSONObject) objApplist.get(i);
//	        		String app_name = objApp.getString("app_name");
//	        		String app_version = objApp.getString("app_version");
//	        		for(App app : listApp) {
//		        		if(app_name.equalsIgnoreCase(app.getAppname()) && app_version.equalsIgnoreCase(app.getAppversion())) {
//		        			listApp_new.add(app);
//		        			break;
//		        		}
//	        		}
//	        	}
//	        }
//		}
//		
//        return listApp_new;
//	}

	public JSONArray get_applist2(String device_id, String app_type) {
/*	    {
	        "app_name": "ShadowStatusApp",
	        "is_running": false,
	        "app_type": "shadow",
	        "app_version": "0.2.1",
	        "app_update_time": 1512425447909,
	        "attributes": [
	            {
	                "unit": "",
	                "name": "light",
	                "type": "string",
	                "desc": "LED灯"
	            }
	        ],
	        "class_name": "app.topology.services.ShadowStatusApp",
	        "desc": "LED 处理模块"
	    }
*/
	    if(device_id != null && !device_id.isEmpty()) {
	        ApacheAdgentMetaData _ApacheAdgentMetaDataParameters = new ApacheAdgentMetaData();
	        _ApacheAdgentMetaDataParameters.setHardware_device_id(device_id);
	        List<ApacheAdgentMetaData> listApacheAdgentMetaData = _ApacheAdgentMetaDataServiceImpl.findApacheAdgentMetaDataByParameter_deviceid(_ApacheAdgentMetaDataParameters);
	        if(listApacheAdgentMetaData != null && listApacheAdgentMetaData.size() == 1) {
	    		JSONArray result = new JSONArray();
	    		
	        	String applist = listApacheAdgentMetaData.get(0).getEdgent_agent_app_list();
	        	JSONArray objApplist = JSONArray.fromObject(applist);
	        	for(Object obj : objApplist) {
	        		JSONObject objApp = (JSONObject)obj;
	        		String temp_app_name = objApp.optString("app_name");
	        		String temp_app_type = objApp.optString("app_type");
	        		String temp_app_version = objApp.optString("app_version");
	        		String temp_app_desc = objApp.optString("desc");
	        		
	        		if(temp_app_type.equals(app_type)) {
		        		JSONObject o = new JSONObject();
		        		o.put("appname", temp_app_name);
		        		o.put("apptype", temp_app_type);
		        		o.put("appversion", temp_app_version);
		        		o.put("appdesc", temp_app_desc);
		        		
		        		JSONArray result_attr = new JSONArray();
		        		JSONArray objAppAttributelist = objApp.optJSONArray("attributes");
		        		if(objAppAttributelist != null && objAppAttributelist.size() > 0) {
		    	        	for(Object attr : objAppAttributelist) {
		    	        		JSONObject objAttr = (JSONObject)attr;
		    	        		
		    	        		JSONObject a = new JSONObject();
		    	        		a.put("sourcename", objAttr.optString("name"));
		    	        		a.put("sourcedatatype", objAttr.optString("type"));
		    	        		a.put("sourceunit", objAttr.optString("unit"));
		    	        		a.put("sourcedefault", "");
		    	        		a.put("sourcedisplayname", objAttr.optString("desc"));
		    	        		
		    	        		result_attr.add(a);
		    	        	}
		    	        	
		    	        	o.put("appsourcearray", result_attr);
		        		}
		        		
			        	result.add(o);
	        		}
	        	}
	        	
	        	return result;
	        }
		}
		
        return null;
	}
	
	// 获取设备的某个应用信息
	public JSONObject get_app(String device_id, String app_name) {
		JSONObject result = null;
		
	    if(device_id != null && !device_id.isEmpty()) {
	        ApacheAdgentMetaData _ApacheAdgentMetaDataParameters = new ApacheAdgentMetaData();
	        _ApacheAdgentMetaDataParameters.setHardware_device_id(device_id);
	        List<ApacheAdgentMetaData> listApacheAdgentMetaData = _ApacheAdgentMetaDataServiceImpl.findApacheAdgentMetaDataByParameter_deviceid(_ApacheAdgentMetaDataParameters);
	        if(listApacheAdgentMetaData != null && listApacheAdgentMetaData.size() == 1) {
	    		String applist = listApacheAdgentMetaData.get(0).getEdgent_agent_app_list();
	        	JSONArray objApplist = JSONArray.fromObject(applist);
	        	for(Object obj : objApplist) {
	        		JSONObject objApp = (JSONObject)obj;
	        		if(objApp.optString("app_name").equalsIgnoreCase(app_name)) {
	        			result = objApp;
	        			break;
	        		}
	        	}
	        }
		}
		
        return result;
	}

	public JSONObject getDeviceOnlineHistory(Pagination page, String device_id) throws IOException, ServletException {
		// order by
		if(page.getOrder_by() == null || page.getOrder_by().isEmpty()) {
			page.setOrder_by("create_stamp");
		}
		if(page.getAsc() == null || page.getAsc().isEmpty()) {
			page.setAsc("desc");
		}

		List<DeviceOnlineHistory> onlineList = deviceDao.getDeviceOnlineHistory(page, device_id);

		JSONArray objarray = new JSONArray();
		if (onlineList != null) {
			for (DeviceOnlineHistory online : onlineList) {
				JSONObject obj = new JSONObject();
				obj.put("id", online.getId());
				obj.put("device_id", online.getDevice_id());
				obj.put("is_online", online.isIs_online() ? true : false);

				String ip = online.getIp();
				if (ip == null) {
					ip = "";
				}
				obj.put("ip", ip);

				String username = online.getUsername();
				if (username == null) {
					username = "";
				}
				obj.put("username", username);

				String broker = online.getBroker();
				if (broker == null) {
					broker = "";
				}
				obj.put("broker", broker);

				String create_stamp = WebUtil.format(online.getCreate_stamp());
				obj.put("create_stamp", create_stamp);
				String online_stamp = WebUtil.format(online.getOnline_stamp());
				obj.put("online_stamp", online_stamp);

				objarray.add(obj);
			}
		}

		JSONObject object = new JSONObject();
		object.put("rows", objarray);
		
		object.put("pagesize", page.getPagesize());
		object.put("current", page.getCurrent());
		object.put("total", page.getTotal());

		return object;
	}

	public Device getDeviceByPrimaryKey(Long id) {
		Device _Device = deviceDao.getDeviceByPrimaryKey(id);
		return _Device;
	}

	public Device getDeviceObject(String id) {
		Device _Device = deviceDao.getDevice(id);
		return _Device;
	}

	public JSONObject setOnlineStatus(String device_id, String username, int status, long timestamp, String ipaddress) {
		int row = deviceDao.setOnlineStatus(device_id, username, status, timestamp, ipaddress);
		
		JSONObject object = new JSONObject();
		object.put("result", row);
		return object;
	}

	public JSONObject reboot(String device_id) {
		boolean result;
		String error;

		if (device_id != null && !device_id.isEmpty()) {
			Device device = deviceDao.getDevice(device_id);
			if(device != null) {
				if(device.getOnline() == 1L) {
					String topic = "$MOC/control/" + device_id + "/dev/reboot";
					
					JSONObject obj = new JSONObject();
					obj.put("refresh_sk", true);
					obj.put("profileVersion", 0);
					String message = obj.toString();
		
					MqttMessage msgObj = new MqttMessage();
					msgObj.setTopic(topic);
					msgObj.setMessage(message);
					msgObj.setQos(0);
					msgObj.setDevice_id(device_id);
					msgObj.setApp_name("com.lenovo.edgent.agent.EdgentAgent");
					msgObj.setApp_type("edgent");
					result = emqttInstance.pulishMessage(msgObj);
					error = "";
				} else {
					result = false;
					error = "device is offline";
				}
			} else {
				result = false;
				error = "no device found";
			}
		} else {
			result = false;
			error = "empty device id";
		}

		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);
		return object;
	}

	public String addMqttMessage(MqttMessage message) {
		boolean result;
		String error;

		try {
			if(message.getMessage() != null && !message.getMessage().isEmpty()) {
				message.setPayload(message.getMessage().getBytes("UTF-8").length);
			}
			
			int row = deviceDao.addMqttMessage(message);
			if (row > 0) {
				result = true;
				error = "success";
			} else {
				result = false;
				error = "fail";
			}
		} catch (Exception e) {
			log.debug(e.getMessage());
			result = false;
			error = e.getMessage();
		}

		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);

		return object.toString();
	}

	public JSONArray getDeviceMqttMessageStatistic(String device_id, String app_name) {
		try {
			List<MqttDeviceMessages> mqttMessages = deviceDao.getDeviceMqttMessageStatistic(device_id, app_name);

			JSONArray objarray = new JSONArray();
			for (MqttDeviceMessages msg : mqttMessages) {
				JSONObject obj = new JSONObject();
				obj.put("id", msg.getId());
				obj.put("device_id", msg.getDevice_id());
				obj.put("app_name", msg.getApp_name());
				obj.put("app_type", msg.getApp_type());
				obj.put("received", msg.getReceived());
				obj.put("transmitted", msg.getTransmitted());
				obj.put("transmitted_invalid", msg.getTransmitted_invalid());
				obj.put("transmitted_valid", msg.getTransmitted() - msg.getTransmitted_invalid());

				String update_stamp = WebUtil.format(msg.getUpdate_stamp());
				obj.put("update_stamp", update_stamp);

				objarray.add(obj);
			}
			return objarray;
		} catch (Exception e) {
			log.debug(e.toString());
			e.printStackTrace();
			return new JSONArray();
		}
	}

	public JSONObject getDeviceMqttMessageList(Pagination page, String device_id, String app_name, boolean received) {
		JSONArray objarray = new JSONArray();
		try {
			// order by
			if(page.getOrder_by() == null || page.getOrder_by().isEmpty()) {
				page.setOrder_by("create_stamp");
			}
			if(page.getAsc() == null || page.getAsc().isEmpty()) {
				page.setOrder_by("desc");
			}

			List<MqttMessage> mqttMessages = deviceDao.getDeviceMqttMessageList(page, device_id, app_name, received ? 1 : 0);

			//ObjectMapper mapper = new ObjectMapper();
		    //Object json = mapper.readValue(input, Object.class);
		    //String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		    
			//ObjectMapper mapper = new ObjectMapper();
			for (MqttMessage msg : mqttMessages) {
				JSONObject obj = new JSONObject();
				obj.put("id", msg.getId());
				obj.put("device_id", msg.getDevice_id());
				obj.put("app_name", msg.getApp_name());
				obj.put("app_type", msg.getApp_type());
				obj.put("topic", msg.getTopic());
				obj.put("message", msg.getMessage());
				
				//Object obj_temp = mapper.readValue(msg.getMessage(), Object.class);
				//obj.put("message", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj_temp));
				
				obj.put("qos", msg.getQos());
				obj.put("received", msg.isReceived() ? 1 : 0);
				obj.put("isvalid", msg.isIsvalid() ? 1 : 0);
				
				String message_stamp = WebUtil.format(msg.getMessage_stamp());
				obj.put("message_stamp", message_stamp);

				String create_stamp = WebUtil.format(msg.getCreate_stamp());
				obj.put("create_stamp", create_stamp);

				objarray.add(obj);
			}
		} catch (Exception e) {
			log.debug(e.toString());
			e.printStackTrace();
		}
		
		JSONObject object = new JSONObject();
		object.put("rows", objarray);
		
		object.put("pagesize", page.getPagesize());
		object.put("current", page.getCurrent());
		object.put("total", page.getTotal());
		
		return object;
	}
}
