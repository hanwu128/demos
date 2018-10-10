package com.lenovo.iot.devicemanager.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

public class Sensor_Temperature_Alarm {
	
	private Long id;
	private String device_id;
	private String device_name;
	private String label_id;
	private int type;
	private double value;
	private double value_limit;
	private Timestamp create_stamp;
	private int status;

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

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public String getLabel_id() {
		return label_id;
	}

	public void setLabel_id(String label_id) {
		this.label_id = label_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getValue_limit() {
		return value_limit;
	}

	public void setValue_limit(double value_limit) {
		this.value_limit = value_limit;
	}

	public Timestamp getCreate_stamp() {
		return create_stamp;
	}

	public void setCreate_stamp(Timestamp create_stamp) {
		this.create_stamp = create_stamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Sensor_Temperature_Alarm() {}
	
	//RowMapper
	 public static final RowMapper<Sensor_Temperature_Alarm> MAP = new RowMapper<Sensor_Temperature_Alarm>(){
		    public Sensor_Temperature_Alarm mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	Sensor_Temperature_Alarm obj = new Sensor_Temperature_Alarm();
		    	obj.setId(rs.getLong("id"));
		    	obj.setDevice_id(rs.getString("device_id"));
		    	obj.setDevice_name(rs.getString("device_name"));
		    	obj.setLabel_id(rs.getString("label_id"));
		    	obj.setType(rs.getInt("type"));
		    	obj.setValue(rs.getDouble("value"));
		    	obj.setValue_limit(rs.getDouble("value_limit"));
		    	obj.setCreate_stamp(rs.getTimestamp("create_stamp"));
		    	obj.setStatus(rs.getInt("status"));
		    	
		    	return obj;
	    }
	};
}
