package com.lenovo.iot.devicemanager.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

public class DeviceShadow {

	private Long id;
	private String shadow_name;
	private String device_id;
	private long company_id;
	private String app_name;
	private String shadow_desc;
	private String app_version;
	private String app_desc;
	private Timestamp create_stamp;
	private Timestamp update_stamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShadow_name() {
		return shadow_name;
	}

	public void setShadow_name(String shadow_name) {
		this.shadow_name = shadow_name;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(long company_id) {
		this.company_id = company_id;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getShadow_desc() {
		return shadow_desc;
	}

	public void setShadow_desc(String shadow_desc) {
		this.shadow_desc = shadow_desc;
	}
	
	public String getApp_version() {
		return app_version;
	}

	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}

	public String getApp_desc() {
		return app_desc;
	}

	public void setApp_desc(String app_desc) {
		this.app_desc = app_desc;
	}

	public Timestamp getCreate_stamp() {
		return create_stamp;
	}

	public void setCreate_stamp(Timestamp create_stamp) {
		this.create_stamp = create_stamp;
	}

	public Timestamp getUpdate_stamp() {
		return update_stamp;
	}

	public void setUpdate_stamp(Timestamp update_stamp) {
		this.update_stamp = update_stamp;
	}

	public static RowMapper<DeviceShadow> getMap() {
		return MAP;
	}

	public DeviceShadow() {}

	//RowMapper
	 public static final RowMapper<DeviceShadow> MAP = new RowMapper<DeviceShadow>(){
		    public DeviceShadow mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	DeviceShadow obj = new DeviceShadow();
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
		    	
		    	return obj;
	    }
	};
}