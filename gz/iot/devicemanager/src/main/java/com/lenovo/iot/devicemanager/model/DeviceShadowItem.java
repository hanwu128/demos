package com.lenovo.iot.devicemanager.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

public class DeviceShadowItem {

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShadow_name() {
		return shadow_name;
	}

	public void setShadow_name(String shadow_name) {
		this.shadow_name = shadow_name;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getItem_display_name() {
		return item_display_name;
	}

	public void setItem_display_name(String item_display_name) {
		this.item_display_name = item_display_name;
	}

	public String getItem_map_name() {
		return item_map_name;
	}

	public void setItem_map_name(String item_map_name) {
		this.item_map_name = item_map_name;
	}

	public String getItem_default() {
		return item_default;
	}

	public void setItem_default(String item_default) {
		this.item_default = item_default;
	}

	public String getItem_datatype() {
		return item_datatype;
	}

	public void setItem_datatype(String item_datatype) {
		this.item_datatype = item_datatype;
	}

	public String getItem_unit() {
		return item_unit;
	}

	public void setItem_unit(String item_unit) {
		this.item_unit = item_unit;
	}

	public Timestamp getCreate_stamp() {
		return create_stamp;
	}

	public void setCreate_stamp(Timestamp create_stamp) {
		this.create_stamp = create_stamp;
	}

	public String getItem_value() {
		return item_value;
	}

	public void setItem_value(String item_value) {
		this.item_value = item_value;
	}

	public Timestamp getItemvalue_stamp() {
		return itemvalue_stamp;
	}

	public void setItemvalue_stamp(Timestamp itemvalue_stamp) {
		this.itemvalue_stamp = itemvalue_stamp;
	}

	public String getExpect_value() {
		return expect_value;
	}

	public void setExpect_value(String expect_value) {
		this.expect_value = expect_value;
	}

	public Timestamp getExpectvalue_stamp() {
		return expectvalue_stamp;
	}

	public void setExpectvalue_stamp(Timestamp expectvalue_stamp) {
		this.expectvalue_stamp = expectvalue_stamp;
	}

	private Long id;
	private String shadow_name;
	private String item_name;
	private String item_display_name;
	private String item_map_name;
	private String item_default;
	private String item_datatype;
	private String item_unit;
	private Timestamp create_stamp;
	private String item_value;
	private Timestamp itemvalue_stamp;
	private String expect_value;
	private Timestamp expectvalue_stamp;

	public static RowMapper<DeviceShadowItem> getMap() {
		return MAP;
	}
	
	public DeviceShadowItem() {}

	//RowMapper
	 public static final RowMapper<DeviceShadowItem> MAP = new RowMapper<DeviceShadowItem>(){
		    public DeviceShadowItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	DeviceShadowItem obj = new DeviceShadowItem();
		    	obj.setId(rs.getLong("id"));
		    	obj.setShadow_name(rs.getString("shadow_name"));
		    	obj.setItem_name(rs.getString("item_name"));
		    	obj.setItem_display_name(rs.getString("item_display_name"));
		    	obj.setItem_map_name(rs.getString("item_map_name"));
		    	obj.setItem_default(rs.getString("item_default"));
		    	obj.setItem_datatype(rs.getString("item_datatype"));
		    	obj.setItem_unit(rs.getString("item_unit"));
		    	obj.setCreate_stamp(rs.getTimestamp("create_stamp"));
		    	obj.setItem_value(rs.getString("item_value"));
		    	obj.setItemvalue_stamp(rs.getTimestamp("itemvalue_stamp"));
		    	obj.setExpect_value(rs.getString("expect_value"));
		    	obj.setExpectvalue_stamp(rs.getTimestamp("expectvalue_stamp"));
		    	
		    	return obj;
	    }
	};
}