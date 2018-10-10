package com.lenovo.iot.devicemanager.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

public class Company {

	public long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(long company_id) {
		this.company_id = company_id;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCompany_sk() {
		return company_sk;
	}

	public void setCompany_sk(String company_sk) {
		this.company_sk = company_sk;
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

	private long company_id;
	private String company_name;
	private String address;
	private String contact;
	private String email;
	private String remark;
	private String company_sk;
	private Timestamp create_stamp;
	private Timestamp update_stamp;

	
	public Company() {}

	//RowMapper
	 public static final RowMapper<Company> MAP = new RowMapper<Company>(){
		    public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	Company obj = new Company();
		    	obj.setCompany_id(rs.getLong("company_id"));
		    	obj.setCompany_name(rs.getString("company_name"));
		    	obj.setAddress(rs.getString("address"));
		    	obj.setContact(rs.getString("contact"));
		    	obj.setEmail(rs.getString("email"));
		    	obj.setRemark(rs.getString("remark"));
		    	//obj.setCompany_sk(rs.getString("company_sk"));
		    	obj.setCreate_stamp(rs.getTimestamp("create_stamp"));
		    	obj.setUpdate_stamp(rs.getTimestamp("update_stamp"));
		    	
		    	return obj;
	    }
	};
}