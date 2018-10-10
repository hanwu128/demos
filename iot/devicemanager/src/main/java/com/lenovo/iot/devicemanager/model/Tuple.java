package com.lenovo.iot.devicemanager.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Tuple<A, B, C> {

	private A v1;
	private B v2;
	private C v3;

	public A getV1() {
		return v1;
	}

	public void setV1(A v1) {
		this.v1 = v1;
	}

	public B getV2() {
		return v2;
	}

	public void setV2(B v2) {
		this.v2 = v2;
	}

	public C getV3() {
		return v3;
	}

	public void setV3(C v3) {
		this.v3 = v3;
	}

	public Tuple() {}

	//RowMapper
	 public static final RowMapper<Tuple<String,Integer,Integer>> MAP_SII = new RowMapper<Tuple<String,Integer,Integer>>(){
		    public Tuple<String,Integer,Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	Tuple<String,Integer,Integer> obj = new Tuple<String,Integer,Integer>();
		    	obj.setV1(rs.getString("v1"));
		    	obj.setV2(rs.getInt("v2"));
		    	obj.setV3(rs.getInt("v3"));
		    	
		    	return obj;
	    }
	};
}