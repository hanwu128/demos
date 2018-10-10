package com.lenovo.iot.auth.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lenovo.iot.auth.model.Device;

@Repository
public class DeviceDao {

	//private static Logger log = Logger.getLogger("fordebug");
	
	@Autowired
	private JdbcTemplate jdbcTemplateMysql;
	
	public Device getDevice(String device_id) {
		String sql = "select id, device_id, company_id, policy_name, access_key, secret_key, device_desc, create_stamp, update_stamp from device where device_id = ?";
		List<Device> list = jdbcTemplateMysql.query(sql, new Object[]{ device_id }, Device.MAP);
		if(list != null && list.size() > 0 ){
			return (Device)list.get(0);
		}
		
		return null;
	}
	
	public Device getDeviceByAccessKey(long company_id, String access_key) {
		String sql = "select id, device_id, company_id, policy_name, access_key, secret_key, device_desc, create_stamp, update_stamp from device where company_id = ? and access_key = ?";
		List<Device> list = jdbcTemplateMysql.query(sql, new Object[]{ company_id, access_key }, Device.MAP);
		if(list != null && list.size() > 0 ){
			return (Device)list.get(0);
		}
		
		return null;
	}
}
