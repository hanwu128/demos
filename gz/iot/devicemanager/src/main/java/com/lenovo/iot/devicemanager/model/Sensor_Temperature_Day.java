package com.lenovo.iot.devicemanager.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

public class Sensor_Temperature_Day {

	private Long id;
	private String label_id;
	private Double current_value;
	private Double max_value;
	private Double min_value;
	private String what_day;
	private Timestamp update_stamp;
	private Timestamp max_stamp;
	private Timestamp min_stamp;
	private Double last_value;
	private int status;
	private int last_status;

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

	public Double getCurrent_value() {
		return current_value;
	}

	public void setCurrent_value(Double current_value) {
		this.current_value = current_value;
	}

	public Double getMax_value() {
		return max_value;
	}

	public void setMax_value(Double max_value) {
		this.max_value = max_value;
	}

	public Double getMin_value() {
		return min_value;
	}

	public void setMin_value(Double min_value) {
		this.min_value = min_value;
	}

	public String getWhat_day() {
		return what_day;
	}

	public void setWhat_day(String what_day) {
		this.what_day = what_day;
	}

	public Timestamp getUpdate_stamp() {
		return update_stamp;
	}

	public void setUpdate_stamp(Timestamp update_stamp) {
		this.update_stamp = update_stamp;
	}
	
	public Timestamp getMax_stamp() {
		return max_stamp;
	}

	public void setMax_stamp(Timestamp max_stamp) {
		this.max_stamp = max_stamp;
	}

	public Timestamp getMin_stamp() {
		return min_stamp;
	}

	public void setMin_stamp(Timestamp min_stamp) {
		this.min_stamp = min_stamp;
	}

	public Double getLast_value() {
		return last_value;
	}

	public void setLast_value(Double last_value) {
		this.last_value = last_value;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getLast_status() {
		return last_status;
	}

	public void setLast_status(int last_status) {
		this.last_status = last_status;
	}

	public Sensor_Temperature_Day() {}
	
	//RowMapper
	 public static final RowMapper<Sensor_Temperature_Day> MAP = new RowMapper<Sensor_Temperature_Day>(){
		    public Sensor_Temperature_Day mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	Sensor_Temperature_Day obj = new Sensor_Temperature_Day();
		    	obj.setId(rs.getLong("id"));
		    	obj.setLabel_id(rs.getString("label_id"));
		    	obj.setCurrent_value(rs.getDouble("current_value"));
		    	obj.setMax_value(rs.getDouble("max_value"));
		    	obj.setMin_value(rs.getDouble("min_value"));
		    	obj.setWhat_day(rs.getString("what_day"));
		    	obj.setUpdate_stamp(rs.getTimestamp("update_stamp"));
		    	obj.setMax_stamp(rs.getTimestamp("max_stamp"));
		    	obj.setMin_stamp(rs.getTimestamp("min_stamp"));
		    	obj.setLast_value(rs.getDouble("last_value"));
		    	obj.setStatus(rs.getInt("status"));
		    	obj.setLast_status(rs.getInt("last_status"));
		    	
		    	return obj;
	    }
	};
}