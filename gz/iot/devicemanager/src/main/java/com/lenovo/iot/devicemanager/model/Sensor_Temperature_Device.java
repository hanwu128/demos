package com.lenovo.iot.devicemanager.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

public class Sensor_Temperature_Device {

	private Long id;
	private String device_id;
	private String device_name;
	private double temperature_upper;
	private double temperature_lower;
	private int duration;
	private int period;
	private int stage;
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

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public double getTemperature_upper() {
		return temperature_upper;
	}

	public void setTemperature_upper(double temperature_upper) {
		this.temperature_upper = temperature_upper;
	}

	public double getTemperature_lower() {
		return temperature_lower;
	}

	public void setTemperature_lower(double temperature_lower) {
		this.temperature_lower = temperature_lower;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public Timestamp getCreate_stamp() {
		return create_stamp;
	}

	public void setCreate_stamp(Timestamp create_stamp) {
		this.create_stamp = create_stamp;
	}

	public Sensor_Temperature_Device() {}
	
	//RowMapper
	 public static final RowMapper<Sensor_Temperature_Device> MAP = new RowMapper<Sensor_Temperature_Device>(){
		    public Sensor_Temperature_Device mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	Sensor_Temperature_Device obj = new Sensor_Temperature_Device();
		    	obj.setId(rs.getLong("id"));
		    	obj.setDevice_id(rs.getString("device_id"));
		    	obj.setDevice_name(rs.getString("device_name"));
		    	obj.setTemperature_upper(rs.getDouble("temperature_upper"));
		    	obj.setTemperature_lower(rs.getDouble("temperature_lower"));
		    	obj.setDuration(rs.getInt("duration"));
		    	obj.setPeriod(rs.getInt("period"));
		    	obj.setStage(rs.getInt("stage"));
		    	obj.setCreate_stamp(rs.getTimestamp("create_stamp"));
		    	
		    	return obj;
	    }
	};
}