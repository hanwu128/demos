package com.lenovo.iot.auth.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lenovo.iot.auth.dao.DeviceDao;
import com.lenovo.iot.auth.model.Device;
import com.lenovo.iot.auth.service.AuthService;

@Service
public class AuthService {
	
	@Autowired
	private DeviceDao deviceDao;
	
	private static Logger log = Logger.getLogger("fordebug");
	
	/***
	 * 设备接入验证
	 * @param clientId 设备唯一标识
	 * @param username 用户注册时提供的设备标识
	 * @param password Secret Key
	 * @return
	 */
	public boolean auth(String clientId, String username, String password) {
		log.debug("device auth().clientId:" + clientId + " username:" + username + " password:" + password);

		Device device = null;
		try {
			int ver = Integer.parseInt(clientId.substring(0, 1));
			if(ver == 1) {
				long company_id = Long.parseLong(clientId.substring(1, 11));
				device = deviceDao.getDeviceByAccessKey(company_id, username);
			} else if(ver == 2) {
				long company_id = Long.parseLong(clientId.substring(1, 9));
				device = deviceDao.getDeviceByAccessKey(company_id, username);
			}
			if(device != null) {
				boolean result = device.getDevice_id().equals(clientId) && device.getSecret_key().equals(password);
				return result;
			} else {
				log.debug("device auth failed, no device(access key) was found:" + clientId + "," + username);
			}
			
		} catch (Exception e) {
			log.debug("device auth failed, invalid device id:" + clientId);
			log.debug(e.getMessage());
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean acl(String access, String username, String clientId, String ipAddr, String topic) {
		//log.debug("acl.do");
		//log.debug("access:" + access + "\tusername:" + username + "\tclientid:" + clientId + "\tipaddr:" + ipAddr + "\ttopic:" + topic);
		
		return true;
	}
}
