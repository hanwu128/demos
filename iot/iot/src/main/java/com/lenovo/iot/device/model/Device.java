package com.lenovo.iot.device.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.springframework.jdbc.core.RowMapper;

public class Device {
	private Long id;
	private String device_id;
	private long company_id;
	private String policy_name;
	private String access_key;
	private String secret_key;
	private String group;
	private String device_desc;
	private Timestamp create_stamp;
	private Timestamp update_stamp;
	private Long online;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDevice_id() {
		return this.device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public long getCompany_id() {
		return this.company_id;
	}

	public void setCompany_id(long company_id) {
		this.company_id = company_id;
	}

	public String getPolicy_name() {
		return this.policy_name;
	}

	public void setPolicy_name(String policy_name) {
		this.policy_name = policy_name;
	}

	public String getAccess_key() {
		return this.access_key;
	}

	public void setAccess_key(String access_key) {
		this.access_key = access_key;
	}

	public String getSecret_key() {
		return this.secret_key;
	}

	public void setSecret_key(String secret_key) {
		this.secret_key = secret_key;
	}

	public String getGroup() {
		return this.group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getDevice_desc() {
		return this.device_desc;
	}

	public void setDevice_desc(String device_desc) {
		this.device_desc = device_desc;
	}

	public Timestamp getCreate_stamp() {
		return this.create_stamp;
	}

	public void setCreate_stamp(Timestamp create_stamp) {
		this.create_stamp = create_stamp;
	}

	public Timestamp getUpdate_stamp() {
		return this.update_stamp;
	}

	public void setUpdate_stamp(Timestamp update_stamp) {
		this.update_stamp = update_stamp;
	}

	public static RowMapper<Device> getMap() {
		return MAP;
	}

	public Long getOnline() {
		return this.online;
	}

	public void setOnline(Long online) {
		this.online = online;
	}

	public static final RowMapper<Device> MAP = new RowMapper<Device>() {
		public Device mapRow(ResultSet rs, int rowNum) throws SQLException {
			Device obj = new Device();
			obj.setId(Long.valueOf(rs.getLong("id")));
			obj.setDevice_id(rs.getString("device_id"));
			obj.setCompany_id(rs.getLong("company_id"));
			obj.setPolicy_name(rs.getString("policy_name"));
			obj.setAccess_key(rs.getString("access_key"));
			obj.setSecret_key(rs.getString("secret_key"));
			obj.setGroup(rs.getString("topic_group"));
			obj.setDevice_desc(rs.getString("device_desc"));
			obj.setCreate_stamp(rs.getTimestamp("create_stamp"));
			obj.setUpdate_stamp(rs.getTimestamp("update_stamp"));
			obj.setOnline(Long.valueOf(rs.getLong("online")));

			return obj;
		}
	};
}
