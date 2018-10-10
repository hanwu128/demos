package com.lenovo.iot.devicemanager.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class DeviceEx extends Device {

	private String edgent_version;
	
	public String getEdgent_version() {
		return edgent_version;
	}

	public void setEdgent_version(String edgent_version) {
		this.edgent_version = edgent_version;
	}

	public DeviceEx() {	}

	//RowMapper
	 public static final RowMapper<DeviceEx> MAP = new RowMapper<DeviceEx>(){
		    public DeviceEx mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	DeviceEx obj = new DeviceEx();
		    	obj.setId(rs.getLong("id"));
		    	obj.setDevice_id(rs.getString("device_id"));
		    	//obj.setLeap_id(rs.getString("leap_id"));
		    	obj.setPolicy_name(rs.getString("policy_name"));
		    	obj.setAccess_key(rs.getString("access_key"));
		    	obj.setSecret_key(rs.getString("secret_key"));
		    	obj.setDevice_desc(rs.getString("device_desc"));
		    	obj.setCreate_stamp(rs.getTimestamp("create_stamp"));
		    	obj.setUpdate_stamp(rs.getTimestamp("update_stamp"));
		    	obj.setOnline(rs.getLong("online"));
		    	obj.setEdgent_version(rs.getString("edgent_version"));
		    	
		    	return obj;
	    }
	};
}