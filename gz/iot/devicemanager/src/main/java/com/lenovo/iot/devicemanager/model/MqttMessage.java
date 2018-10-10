package com.lenovo.iot.devicemanager.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

public class MqttMessage {

	private Long id;
	private String device_id;
	private String app_name;
	private String app_type;
	private String topic;
	private String message;
	private int qos;
	private int payload;
	private Timestamp message_stamp;
	private boolean received;
	private boolean isvalid;

	private Timestamp create_stamp;

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

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getQos() {
		return qos;
	}

	public void setQos(int qos) {
		this.qos = qos;
	}

	public int getPayload() {
		return payload;
	}

	public void setPayload(int payload) {
		this.payload = payload;
	}

	public Timestamp getMessage_stamp() {
		return message_stamp;
	}

	public void setMessage_stamp(Timestamp message_stamp) {
		this.message_stamp = message_stamp;
	}

	public Timestamp getCreate_stamp() {
		return create_stamp;
	}

	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}
	
	public boolean isIsvalid() {
		return isvalid;
	}

	public void setIsvalid(boolean isvalid) {
		this.isvalid = isvalid;
	}

	public void setCreate_stamp(Timestamp create_stamp) {
		this.create_stamp = create_stamp;
	}

	public MqttMessage() {}
	
	//RowMapper
	 public static final RowMapper<MqttMessage> MAP = new RowMapper<MqttMessage>(){
		    public MqttMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	MqttMessage obj = new MqttMessage();
		    	obj.setId(rs.getLong("id"));
		    	obj.setDevice_id(rs.getString("device_id"));
		    	obj.setApp_name(rs.getString("app_name"));
		    	obj.setApp_type(rs.getString("app_type"));
		    	obj.setTopic(rs.getString("topic"));
		    	obj.setMessage(rs.getString("message"));
		    	obj.setQos(rs.getInt("qos"));
		    	obj.setPayload(rs.getInt("payload"));
		    	obj.setMessage_stamp(rs.getTimestamp("message_stamp"));
		    	obj.setReceived(rs.getBoolean("received"));
		    	obj.setIsvalid(rs.getBoolean("isvalid"));
		    	obj.setCreate_stamp(rs.getTimestamp("create_stamp"));
		    	
		    	return obj;
	    }
	};
}