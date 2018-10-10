package com.lenovo.iot.devicemanager.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

public class MqttDeviceMessages {

	private Long id;
	private String device_id;
	private String app_name;
	private String app_type;
	private int received;
	private int transmitted;
	private int transmitted_invalid;
	private Timestamp update_stamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getApp_type() {
		return app_type;
	}

	public void setApp_type(String app_type) {
		this.app_type = app_type;
	}

	public int getReceived() {
		return received;
	}

	public void setReceived(int received) {
		this.received = received;
	}

	public int getTransmitted() {
		return transmitted;
	}

	public void setTransmitted(int transmitted) {
		this.transmitted = transmitted;
	}

	public int getTransmitted_invalid() {
		return transmitted_invalid;
	}

	public void setTransmitted_invalid(int transmitted_invalid) {
		this.transmitted_invalid = transmitted_invalid;
	}

	public Timestamp getUpdate_stamp() {
		return update_stamp;
	}

	public void setUpdate_stamp(Timestamp update_stamp) {
		this.update_stamp = update_stamp;
	}

	public MqttDeviceMessages() {}
	
	//RowMapper
	 public static final RowMapper<MqttDeviceMessages> MAP = new RowMapper<MqttDeviceMessages>(){
		    public MqttDeviceMessages mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	MqttDeviceMessages obj = new MqttDeviceMessages();
		    	obj.setId(rs.getLong("id"));
		    	obj.setDevice_id(rs.getString("device_id"));
		    	obj.setApp_name(rs.getString("app_name"));
		    	obj.setApp_type(rs.getString("app_type"));
		    	obj.setReceived(rs.getInt("received"));
		    	obj.setTransmitted(rs.getInt("transmitted"));
		    	obj.setTransmitted_invalid(rs.getInt("received_invalid"));
		    	obj.setUpdate_stamp(rs.getTimestamp("update_stamp"));
		    	
		    	return obj;
	    }
	};
}