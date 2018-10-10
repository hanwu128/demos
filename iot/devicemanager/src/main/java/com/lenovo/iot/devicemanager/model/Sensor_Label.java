package com.lenovo.iot.devicemanager.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

public class Sensor_Label {
	
	private Long id;
	private String label_id;
	private String label_name;
	private String label_group;
	private String label_index;
	private String device_id;
	private Timestamp create_stamp;
	private Timestamp update_stamp;
	private String app_name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel_id() {
		return label_id;
	}

	public void setLabel_id(String label_id) {
		this.label_id = label_id;
	}

	public String getLabel_name() {
		return label_name;
	}

	public void setLabel_name(String label_name) {
		this.label_name = label_name;
	}

	public String getLabel_group() {
		return label_group;
	}

	public void setLabel_group(String label_group) {
		this.label_group = label_group;
	}

	public String getLabel_index() {
		return label_index;
	}

	public void setLabel_index(String label_index) {
		this.label_index = label_index;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
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
	
	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public Sensor_Label() {}
	
	//RowMapper
	 public static final RowMapper<Sensor_Label> MAP = new RowMapper<Sensor_Label>(){
		    public Sensor_Label mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	Sensor_Label obj = new Sensor_Label();
		    	obj.setId(rs.getLong("id"));
		    	obj.setLabel_id(rs.getString("label_id"));
		    	obj.setLabel_name(rs.getString("label_name"));
		    	obj.setLabel_group(rs.getString("label_group"));
		    	obj.setLabel_index(rs.getString("label_index"));
		    	obj.setDevice_id(rs.getString("device_id"));
		    	obj.setCreate_stamp(rs.getTimestamp("create_stamp"));
		    	obj.setUpdate_stamp(rs.getTimestamp("update_stamp"));
		    	obj.setApp_name(rs.getString("app_name"));
		    	
		    	return obj;
	    }
	};
}
