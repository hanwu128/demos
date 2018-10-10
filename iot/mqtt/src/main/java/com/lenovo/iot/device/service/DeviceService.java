package com.lenovo.iot.device.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.lenovo.iot.device.dao.DeviceDao;
import com.lenovo.iot.device.model.Device;
import com.lenovo.iot.device.model.Meta;
import com.lenovo.iot.util.Config;
import com.lenovo.iot.util.Md5;

@Service
public class DeviceService {
	private Logger log = Logger.getLogger("device");

    @Autowired
    private Config config;

    @Autowired
	private DeviceDao deviceDao;

	@Autowired
	private CompanyService companyService;

	private DateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public JsonObject getDevice(long company_id, String access_key) throws Exception {
		if (access_key == null || access_key.isEmpty()) {
			throw new IllegalArgumentException("empty device id");
		} else if (company_id == 0) {
			throw new IllegalArgumentException("empty company id");
		} else {
			Device device = deviceDao.getDeviceByAccessKey(company_id, access_key);
			if (device != null) {
				if (device.getCompany_id() != company_id) {
					throw new IllegalArgumentException("device is not in company");
				}

				JsonObject object = getDeviceJsonObject(device);

				return object;
			} else {
				throw new IllegalArgumentException("no device found: access key " + access_key);
			}
		}
	}

	private JsonObject getDeviceJsonObject(Device device) {
		JsonObject object = new JsonObject();
		if (device != null) {
			object.addProperty("id", device.getId());
			object.addProperty("device_id", device.getDevice_id());
			object.addProperty("company_id", device.getCompany_id());
			object.addProperty("access_key", device.getAccess_key());
			object.addProperty("secret_key", device.getSecret_key());
			object.addProperty("topic_group", device.getTopic_group());

			String device_desc = device.getDevice_desc();
			if (device_desc == null) {
				device_desc = "";
			}
			object.addProperty("device_desc", device_desc);

			String create_stamp = YYYYMMDDHHMMSS.format(device.getCreate_stamp());
			object.addProperty("create_stamp", create_stamp);
			String update_stamp = YYYYMMDDHHMMSS.format(device.getUpdate_stamp());
			object.addProperty("update_stamp", update_stamp);
			object.addProperty("status", device.getOnline());
		}

		return object;
	}

	// for mq sdk
	private String genDevice_id_v2(long company_id, String access_key) {
		String id_ver = "2";
		String id_company = String.format("%08d", company_id);
		String id = id_ver + id_company + "$" + access_key;
		return id;
	}

	private String genDevice_id(long company_id, String access_key) {
		String now = Long.toString(System.currentTimeMillis());
		String id_md5 = Md5.encryption(access_key + now);
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

	private String genTopic_group(Device device) {
		// String topic_group = "device";

		// String device_id = device.getDevice_id();
		// String x = device_id.substring(device_id.length() - 3, device_id.length());
		// try {
		// int a = Integer.parseInt(x);
		// int i = a % TOPIC_GROUPS.size();
		// topic_group = TOPIC_GROUPS.get(i);
		// } catch (Exception e) {
		// log.debug(e.getMessage());
		// e.printStackTrace();
		// }

		return Long.toString(device.getCompany_id());
	}

	public JsonObject registerDevice(long company_id, String company_sk, String device_id, String device_desc) throws Exception {
		if (company_id == 0) {
			throw new IllegalArgumentException("empty company_id");
		}
		if (company_sk == null || company_sk.isEmpty()) {
			throw new IllegalArgumentException("empty company_sk");
		}
		if (device_id == null || device_id.isEmpty()) {
			throw new IllegalArgumentException("empty device_id(access key)");
		}

		// 验证 company sk
		if (companyService.validSecretKey(company_id, company_sk)) {
			Device existingDevice = deviceDao.getDeviceByAccessKey(company_id, device_id);
			if (existingDevice != null) {
				String brokerUrl = config.getMqtt_broker_address();
				int pos_last = brokerUrl.lastIndexOf(':');
				String port = brokerUrl.substring(pos_last + 1);
				String broker = brokerUrl.substring(0, pos_last);

				JsonObject object = new JsonObject();
				object.addProperty("client_id", existingDevice.getDevice_id());
				object.addProperty("broker", broker);
				object.addProperty("port", Integer.parseInt(port));
				object.addProperty("device_id", existingDevice.getAccess_key());
				object.addProperty("secret_key", existingDevice.getSecret_key());
				object.addProperty("group", existingDevice.getTopic_group());
				object.addProperty("keep_alive", 60);
				object.addProperty("clean_session", true);
				object.addProperty("timestamp", System.currentTimeMillis());
				
				log.debug("register new device ok, device id(access key): " + device_id);
				
				if(device_desc != null) {
					deviceDao.updateDeviceDesc(existingDevice.getDevice_id(), device_desc);
				}

				return object;
			} else {
				Device device = new Device();
				device.setDevice_id(genDevice_id(company_id, device_id));
				device.setAccess_key(device_id);
				device.setCompany_id(company_id);
				device.setDevice_desc(device_desc);
				// 生成 SecretKey
				// String keyPair[] = RSA.generateRSAKeyPair();
				// device.setSecret_key(keyPair[1]);
				// device.setPublic_key(keyPair[0]);
				device.setSecret_key(genSecret_key(device_id));

				device.setTopic_group(genTopic_group(device));
				
				int row = deviceDao.addDevice(device);
				if (row > 0) {
					String brokerUrl = config.getMqtt_broker_address();
					int pos_last = brokerUrl.lastIndexOf(':');
					String port = brokerUrl.substring(pos_last + 1);
					String broker = brokerUrl.substring(0, pos_last);

					JsonObject object = new JsonObject();
					object.addProperty("client_id", device.getDevice_id());
					object.addProperty("broker", broker);
					object.addProperty("port", Integer.parseInt(port));
					object.addProperty("device_id", device.getAccess_key());
					object.addProperty("secret_key", device.getSecret_key());
					object.addProperty("group", device.getTopic_group());
					object.addProperty("keep_alive", 60);
					object.addProperty("clean_session", true);
					object.addProperty("timestamp", System.currentTimeMillis());
					
					log.debug("register existing device ok, device id(access key): " + device_id);

					return object;
				} else {
					throw new IllegalArgumentException("failed to register new device");
				}
			}
		} else {
			throw new IllegalArgumentException("wrong company secret key");
		}
	}

	public JsonObject updateDeviceMeta(JsonObject device_meta) throws Exception {    	
    	Meta meta = new Meta();
    	
    	if(device_meta.has("deviceId")) {
    		meta.setDevice_id(device_meta.get("deviceId").getAsString());
    	} else {
    		throw new IllegalArgumentException("deviceId is null");
    	}
    	if(device_meta.has("hardware_model")) {
    		meta.setHardware_model(device_meta.get("hardware_model").getAsString());
    	}
    	if(device_meta.has("hardware_manufactor")) {
    		meta.setHardware_model(device_meta.get("hardware_manufactor").getAsString());
    	}
    	if(device_meta.has("hardware_os")) {
    		meta.setHardware_model(device_meta.get("hardware_os").getAsString());
    	}
    	if(device_meta.has("hardware_os_version")) {
    		meta.setHardware_model(device_meta.get("hardware_os_version").getAsString());
    	}
    	if(device_meta.has("hardware_location")) {
    		meta.setHardware_model(device_meta.get("hardware_location").getAsString());
    	}
    	if(device_meta.has("firmware_name")) {
    		meta.setHardware_model(device_meta.get("firmware_name").getAsString());
    	}
    	if(device_meta.has("firmware_version")) {
    		meta.setHardware_model(device_meta.get("firmware_version").getAsString());
    	}
    	if(device_meta.has("host_ip")) {
    		meta.setHardware_model(device_meta.get("host_ip").getAsString());
    	}
    	
    	int row = 0;
    	
    	Meta existing_Meta = deviceDao.getMeta(meta.getDevice_id());
    	if(existing_Meta == null) {
    		row =deviceDao.addMeta(meta);
    	} else {
    		row = deviceDao.updateMeta(meta);
    	}
    	
		JsonObject object = new JsonObject();
		object.addProperty("result", row);
		return object;
    }
	
	public JsonObject setOnlineStatus(String device_id, String username, int status, long timestamp, String ipaddress) {
		int row = deviceDao.setOnlineStatus(device_id, username, status, timestamp, ipaddress);

		JsonObject object = new JsonObject();
		object.addProperty("result", row);
		return object;
	}
}
