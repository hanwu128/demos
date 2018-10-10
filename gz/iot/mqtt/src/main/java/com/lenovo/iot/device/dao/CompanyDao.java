package com.lenovo.iot.device.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDao {

	@Autowired
	private JdbcTemplate jdbcTemplateMysql;

	public String getSecretKey(long company_id) {
		String sk;
		
		try {
			String sql = "select company_sk from company where company_id = ?";
			sk = jdbcTemplateMysql.queryForObject(sql, new Object[] { company_id }, String.class);
		} catch (EmptyResultDataAccessException e) {
			sk = null;
		}
		
		return sk;
	}
}
