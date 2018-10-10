package com.lenovo.iot.device.service;

import com.lenovo.iot.device.dao.CompanyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
	@Autowired
	private CompanyDao companyDao;

	public boolean validSecretKey(long company_id, String secretkey_old) {
		if (company_id != 0) {
			String secretkey = this.companyDao.getSecretKey(company_id);
			if ((secretkey != null) && (secretkey.equals(secretkey_old))) {
				return true;
			}
		}
		return false;
	}
}
