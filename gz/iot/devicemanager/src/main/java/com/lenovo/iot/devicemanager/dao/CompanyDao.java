package com.lenovo.iot.devicemanager.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;

import com.lenovo.iot.devicemanager.model.Company;

@Repository
public class CompanyDao {

//	private static final Logger log = LoggerFactory.getLogger(CompanyDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplateMysql;
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	public Company get(long company_id) {
		String sql = "select company_id, company_name, address, contact, email, remark, create_stamp, update_stamp from company where company_id = ?";
		List<Company> list = jdbcTemplateMysql.query(sql, new Object[]{ company_id }, Company.MAP);
		if(list != null && list.size() > 0 ){
			return (Company)list.get(0);
		}
		
		return null;
	}

	public int update(Company company) {
		String sql = "update company set company_name = ?, address = ?, contact = ?, email = ?, remark = ?, update_stamp = now() where company_id = ?";
		int row = jdbcTemplateMysql.update(sql, new Object[]{ company.getCompany_name(), company.getAddress(), company.getContact(), company.getEmail(), company.getRemark(), company.getCompany_id() });
    	
		return row;
	}
	
	public String getSecretKey(long company_id) {
		String sql = "select company_sk from company where company_id = ?";
		Object sk = jdbcTemplateMysql.queryForObject(sql, new Object[]{ company_id }, Object.class);
    	
		return (sk == null ? null : sk.toString());
	}
	
	public int updateSecretKey(long company_id, String secret_key) {
		String sql = "update device set company_sk = ?, update_stamp = now() where company_id = ?";
		int row = jdbcTemplateMysql.update(sql, new Object[]{ secret_key, company_id });
    	
		return row;
	}
}
