package com.lenovo.iot.device.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDao {
	@Autowired
	private JdbcTemplate jdbcTemplateMysql;

	public String getSecretKey(long company_id) {
		String sql = "select company_sk from company where company_id = ?";
		Object sk = this.jdbcTemplateMysql.queryForObject(sql, new Object[] { Long.valueOf(company_id) }, Object.class);

		return sk == null ? null : sk.toString();
	}
}
