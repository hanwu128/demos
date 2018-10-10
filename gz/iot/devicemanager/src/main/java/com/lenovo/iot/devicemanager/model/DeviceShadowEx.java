package com.lenovo.iot.devicemanager.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class DeviceShadowEx extends DeviceShadow {

	private String access_key;
	
	public String getAccess_key() {
		return access_key;
	}

	public void setAccess_key(String access_key) {
		this.access_key = access_key;
	}

	public DeviceShadowEx() {	}

	//RowMapper
	 public static final RowMapper<DeviceShadowEx> MAP = new RowMapper<DeviceShadowEx>(){
		    public DeviceShadowEx mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	DeviceShadowEx obj = new DeviceShadowEx();
		    	obj.setId(rs.getLong("id"));
		    	obj.setShadow_name(rs.getString("shadow_name"));
		    	obj.setDevice_id(rs.getString("device_id"));
		    	obj.setCompany_id(rs.getLong("company_id"));
		    	obj.setApp_name(rs.getString("app_name"));
		    	obj.setShadow_desc(rs.getString("shadow_desc"));
		    	obj.setApp_version(rs.getString("app_version"));
		    	obj.setApp_desc(rs.getString("app_desc"));
		    	obj.setCreate_stamp(rs.getTimestamp("create_stamp"));
		    	obj.setUpdate_stamp(rs.getTimestamp("update_stamp"));
		    	obj.setAccess_key(rs.getString("access_key"));
		    	
		    	return obj;
	    }
	};
}