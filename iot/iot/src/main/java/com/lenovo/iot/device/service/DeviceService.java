package com.lenovo.iot.device.service;

import com.lenovo.iot.device.dao.DeviceDao;
import com.lenovo.iot.device.model.Device;
import com.lenovo.iot.util.Md5;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.UUID;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {
	private static final Logger log = LoggerFactory.getLogger(DeviceService.class);
	private String brokerUrl;
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private CompanyService companyService;
	private DateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public DeviceService() {
		try {
			InputStream is = getClass().getResourceAsStream("/mqtt.conf");

			Properties dbProps = new Properties();
			dbProps.load(is);

			this.brokerUrl = dbProps.getProperty("broker");

			is.close();
		} catch (Exception e) {
			System.err.println("failed to read config file.");
			e.printStackTrace();
		}
	}

	public JSONObject getDevice(long company_id, String access_key) {
		if ((access_key == null) || (access_key.isEmpty())) {
			log.debug("empty device id");
			return JSONObject.fromObject(new Device());
		}
		if (company_id == 0L) {
			log.debug("empty company id");
			return JSONObject.fromObject(new Device());
		}
		Device device = this.deviceDao.getDeviceByAccessKey(company_id, access_key);
		if (device != null) {
			if (device.getCompany_id() != company_id) {
				log.debug("device is not in company");
				return JSONObject.fromObject(new Device());
			}
			JSONObject object = getDeviceJsonObject(device);

			return object;
		}
		log.debug("no device found");
		return JSONObject.fromObject(new Device());
	}

	private JSONObject getDeviceJsonObject(Device device) {
		JSONObject object = new JSONObject();
		if (device != null) {
			object.put("id", device.getId());
			object.put("device_id", device.getDevice_id());
			object.put("company_id", Long.valueOf(device.getCompany_id()));

			object.put("access_key", device.getAccess_key());
			object.put("secret_key", device.getSecret_key());
			object.put("topic_group", device.getGroup());

			String device_desc = device.getDevice_desc();
			if (device_desc == null) {
				device_desc = "";
			}
			object.put("device_desc", device_desc);

			String create_stamp = this.YYYYMMDDHHMMSS.format(device.getCreate_stamp());
			object.put("create_stamp", create_stamp);
			String update_stamp = this.YYYYMMDDHHMMSS.format(device.getUpdate_stamp());
			object.put("update_stamp", update_stamp);
			object.put("status", device.getOnline());
		}
		return object;
	}

	private String genDevice_id_v2(long company_id, String access_key) {
		String id_ver = "2";
		String id_company = String.format("%08d", new Object[] { Long.valueOf(company_id) });
		String id = id_ver + id_company + "$" + access_key;
		return id;
	}

	private String genDevice_id(long company_id, String access_key) {
		String now = Long.toString(System.currentTimeMillis());
		String id_md5 = Md5.encryption(access_key + now);
		String id_ver = "1";
		String id_company = String.format("%010d", new Object[] { Long.valueOf(company_id) });
		String id_right = now.substring(now.length() - 6, now.length() - 1);
		String id = id_md5.substring(8, 24).toUpperCase();

		id = id_ver + id_company + id + id_right;
		return id;
	}

	private String genSecret_key(String Access_key) {
		String uuid = UUID.randomUUID().toString();

		return uuid.replace("-", "").toUpperCase();
	}

	private String genTopic_group(Device device) {
		return Long.toString(device.getCompany_id());
	}

	public JSONObject registerDevice(long company_id, String company_sk, String device_id, String device_desc) throws Exception {
		if (company_id == 0L) {
			throw new IllegalArgumentException("empty company_id");
		}
		if ((company_sk == null) || (company_sk.isEmpty())) {
			throw new IllegalArgumentException("empty company_sk");
		}
		if ((device_id == null) || (device_id.isEmpty())) {
			throw new IllegalArgumentException("empty device_id(access key)");
		}
		if (this.companyService.validSecretKey(company_id, company_sk)) {
			Device existingDevice = this.deviceDao.getDeviceByAccessKey(company_id, device_id);
			if (existingDevice != null) {
				int pos_last = this.brokerUrl.lastIndexOf(':');
				String port = this.brokerUrl.substring(pos_last + 1);
				String broker = this.brokerUrl.substring(0, pos_last);

				JSONObject object = new JSONObject();
				object.put("client_id", existingDevice.getDevice_id());
				object.put("broker", broker);
				object.put("port", Integer.valueOf(Integer.parseInt(port)));
				object.put("device_id", existingDevice.getAccess_key());
				object.put("secret_key", existingDevice.getSecret_key());
				object.put("group", existingDevice.getGroup());
				object.put("keep_alive", Integer.valueOf(60));
				object.put("clean_session", Boolean.valueOf(true));
				object.put("timestamp", Long.valueOf(System.currentTimeMillis()));

				log.debug("register new device ok, device id(access key): " + device_id);

				this.deviceDao.updateDeviceDesc(existingDevice.getDevice_id(), device_desc);

				return object;
			}
			Device device = new Device();
			device.setDevice_id(genDevice_id(company_id, device_id));
			device.setAccess_key(device_id);

			device.setCompany_id(company_id);

			device.setSecret_key(genSecret_key(device_id));

			device.setGroup(genTopic_group(device));
			device.setDevice_desc(device_desc);

			int row = this.deviceDao.addDevice(device);
			if (row > 0) {
				int pos_last = this.brokerUrl.lastIndexOf(':');
				String port = this.brokerUrl.substring(pos_last + 1);
				String broker = this.brokerUrl.substring(0, pos_last);

				JSONObject object = new JSONObject();
				object.put("client_id", device.getDevice_id());
				object.put("broker", broker);
				object.put("port", Integer.valueOf(Integer.parseInt(port)));
				object.put("device_id", device.getAccess_key());
				object.put("secret_key", device.getSecret_key());
				object.put("group", device.getGroup());
				object.put("keep_alive", Integer.valueOf(60));
				object.put("clean_session", Boolean.valueOf(true));
				object.put("timestamp", Long.valueOf(System.currentTimeMillis()));

				log.debug("register existing device ok, device id(access key): " + device_id);

				return object;
			}
			throw new IllegalArgumentException("failed to register new device");
		}
		throw new IllegalArgumentException("wrong company secret key");
	}

//	public JSONObject setOnlineStatus(String device_id, String username, int status, long timestamp, String ipaddress) {
//		int row = this.deviceDao.setOnlineStatus(device_id, username, status, timestamp, ipaddress);
//
//		JSONObject object = new JSONObject();
//		object.put("result", Integer.valueOf(row));
//		return object;
//	}
}
