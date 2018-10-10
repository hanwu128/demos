package com.lenovo.iot.devicemanager.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

public class Sensor_Temperature_Minitue {
	
	private Long id;
	private String label_id;
	private double label_value;
	private String what_day;
	private String what_minitue;
	private Timestamp update_stamp;

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
	public double getLabel_value() {
		return label_value;
	}
	public void setLabel_value(double label_value) {
		this.label_value = label_value;
	}
	public String getWhat_day() {
		return what_day;
	}
	public void setWhat_day(String what_day) {
		this.what_day = what_day;
	}
	public String getWhat_minitue() {
		return what_minitue;
	}
	public void setWhat_minitue(String what_minitue) {
		this.what_minitue = what_minitue;
	}
	public Timestamp getUpdate_stamp() {
		return update_stamp;
	}
	public void setUpdate_stamp(Timestamp update_stamp) {
		this.update_stamp = update_stamp;
	}
	
	public Sensor_Temperature_Minitue() {}

	//RowMapper
	 public static final RowMapper<Sensor_Temperature_Minitue> MAP = new RowMapper<Sensor_Temperature_Minitue>(){
		    public Sensor_Temperature_Minitue mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	Sensor_Temperature_Minitue obj = new Sensor_Temperature_Minitue();
		    	obj.setId(rs.getLong("id"));
		    	obj.setLabel_id(rs.getString("label_id"));
		    	obj.setLabel_value(rs.getDouble("label_value"));
		    	obj.setWhat_day(rs.getString("what_day"));
		    	obj.setWhat_minitue(rs.getString("what_minitue"));
		    	obj.setUpdate_stamp(rs.getTimestamp("update_stamp"));
		    	
		    	return obj;
	    }
	};
}