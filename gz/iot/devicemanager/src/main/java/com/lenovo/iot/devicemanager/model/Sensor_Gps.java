package com.lenovo.iot.devicemanager.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

public class Sensor_Gps {

	private Long id;
	private String device_id;
	private Double longitude;
	private Double latitude;
	private Timestamp gps_stamp;
	private Timestamp device_stamp;
	private Timestamp server_stamp;

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

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Timestamp getGps_stamp() {
		return gps_stamp;
	}

	public void setGps_stamp(Timestamp gps_stamp) {
		this.gps_stamp = gps_stamp;
	}

	public Timestamp getDevice_stamp() {
		return device_stamp;
	}

	public void setDevice_stamp(Timestamp device_stamp) {
		this.device_stamp = device_stamp;
	}

	public Timestamp getServer_stamp() {
		return server_stamp;
	}

	public void setServer_stamp(Timestamp server_stamp) {
		this.server_stamp = server_stamp;
	}

	public Sensor_Gps() {}
	
	//RowMapper
	 public static final RowMapper<Sensor_Gps> MAP = new RowMapper<Sensor_Gps>(){
		    public Sensor_Gps mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	Sensor_Gps obj = new Sensor_Gps();
		    	obj.setId(rs.getLong("id"));
		    	obj.setDevice_id(rs.getString("device_id"));
		    	obj.setLongitude(rs.getDouble("longitude"));
		    	obj.setLatitude(rs.getDouble("latitude"));
		    	obj.setGps_stamp(rs.getTimestamp("gps_stamp"));
		    	obj.setDevice_stamp(rs.getTimestamp("device_stamp"));
		    	obj.setServer_stamp(rs.getTimestamp("server_stamp"));
		    	
		    	return obj;
	    }
	};
}
