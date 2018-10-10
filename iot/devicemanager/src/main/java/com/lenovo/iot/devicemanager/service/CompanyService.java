package com.lenovo.iot.devicemanager.service;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lenovo.iot.devicemanager.dao.CompanyDao;
import com.lenovo.iot.devicemanager.model.Company;
import com.lenovo.iot.devicemanager.model.Device;
import com.lenovo.iot.devicemanager.util.WebUtil;

@Service
public class CompanyService {
	private static final Logger log = LoggerFactory.getLogger(CompanyService.class);

	@Autowired
	private CompanyDao companyDao;

	public JSONObject get(long company_id) {
		if (company_id == 0) {
			log.debug("empty company id");
			return JSONObject.fromObject(new Device());
		} else {
			Company company = companyDao.get(company_id);
			if (company != null) {
				return getCompanyJsonObject(company);
			} else {
				log.debug("no device found");
				return getCompanyJsonObject(new Company());
			}
		}
	}

	private JSONObject getCompanyJsonObject(Company company) {
		JSONObject object = new JSONObject();
		if (company != null) {
			object.put("company_id", company.getCompany_id());
			String company_name = company.getCompany_name();
			if (company_name == null) {
				company_name = "";
			}
			object.put("company_name", company_name);
			
			String address = company.getAddress();
			if (address == null) {
				address = "";
			}
			object.put("address", address);
			
			String contact = company.getContact();
			if (contact == null) {
				contact = "";
			}
			object.put("contact", contact);
			
			String email = company.getEmail();
			if (email == null) {
				email = "";
			}
			object.put("email", email);
			
			String remark = company.getRemark();
			if (remark == null) {
				remark = "";
			}
			object.put("remark", remark);

			String create_stamp = WebUtil.format(company.getCreate_stamp());
			object.put("create_stamp", create_stamp);
			String update_stamp = WebUtil.format(company.getUpdate_stamp());
			object.put("update_stamp", update_stamp);
		}

		return object;
	}

	public JSONObject update(Company company) {
		boolean result;
		String error;

		if (company.getCompany_id() == 0) {
			result = false;
			error = "empty company id";
		} else {
			try {
				int row = companyDao.update(company);
				if (row > 0) {
					result = true;
					error = "success";
				} else {
					result = false;
					error = "fail";
				}
			} catch (RuntimeException e) {
				log.debug(e.getMessage());
				result = false;
				error = e.getMessage();
			}
		}

		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);

		return object;
	}

	private String genSecret_key() {
		String uuid = UUID.randomUUID().toString();
		// String secret_key = Md5.encryption(Access_key + "_"
		// +System.currentTimeMillis());
		// secret_key = secret_key.substring(8, 24);
		// secret_key = Base64.encodeBase64String(secret_key.getBytes());
		return uuid.replace("-", "").toUpperCase();
	}
	
	public String getSecretKey(long company_id) {
		return companyDao.getSecretKey(company_id);
	}
	
	public boolean validSecretKey(long company_id, String secretkey_old) {
		if (company_id != 0) {
			String secretkey = companyDao.getSecretKey(company_id);
			if (secretkey != null && secretkey.equals(secretkey_old)) {
				return true;
			}
		}
		
		return true;
	}

	public JSONObject updateSecretKey(long company_id, String secretkey_old) throws IOException, ServletException {
		boolean result;
		String error;

		if (validSecretKey(company_id, secretkey_old)) {
			// String keyPair[] = RSA.generateRSAKeyPair();
			String secret_key_new = genSecret_key();
			try {
				int row = companyDao.updateSecretKey(company_id, secret_key_new);
				if (row > 0) {
					result = true;
					error = secret_key_new;
				} else {
					result = false;
					error = "fail";
				}
			} catch (RuntimeException e) {
				log.debug(e.getMessage());
				result = false;
				error = e.getMessage();
			}
		} else {
			result = false;
			error = "wrong secret key";
		}

		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("error", error);

		return object;
	}
}
