package com.lenovo.iot.devicemanager.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

public class Sensor_Temperature {

	private Long id;
	private String label_id;
	private double value;
	private Timestamp create_stamp;
	private Timestamp sensor_stamp;
	private String device_id;

	public String getLabel_id() {
		return label_id;
	}

	public void setLabel_id(String label_id) {
		this.label_id = label_id;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Timestamp getCreate_stamp() {
		return create_stamp;
	}

	public void setCreate_stamp(Timestamp create_stamp) {
		this.create_stamp = create_stamp;
	}
	
	public Timestamp getSensor_stamp() {
		return sensor_stamp;
	}

	public void setSensor_stamp(Timestamp sensor_stamp) {
		this.sensor_stamp = sensor_stamp;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	
	public Sensor_Temperature() {}
	
	//RowMapper
	 public static final RowMapper<Sensor_Temperature> MAP = new RowMapper<Sensor_Temperature>(){
		    public Sensor_Temperature mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	Sensor_Temperature obj = new Sensor_Temperature();
		    	obj.setId(rs.getLong("id"));
		    	obj.setLabel_id(rs.getString("label_id"));
		    	obj.setValue(rs.getDouble("value"));
		    	obj.setCreate_stamp(rs.getTimestamp("create_stamp"));
		    	obj.setSensor_stamp(rs.getTimestamp("sensor_stamp"));
		    	obj.setDevice_id(rs.getString("device_id"));
		    	
		    	return obj;
	    }
	};
}