package com.lenovo.iot.device.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lenovo.iot.device.dao.CompanyDao;

@Service
public class CompanyService {

	@Autowired
	private CompanyDao companyDao;

	
	public boolean validSecretKey(long company_id, String secretkey_old) throws Exception {
		if (company_id != 0) {
			String secretkey = companyDao.getSecretKey(company_id);
			if (secretkey == null) {
				throw new IllegalArgumentException("company does NOT exist");
			}
			
			if (secretkey.equals(secretkey_old)) {
				return true;
			}
		}
		
		return false;
	}
}
