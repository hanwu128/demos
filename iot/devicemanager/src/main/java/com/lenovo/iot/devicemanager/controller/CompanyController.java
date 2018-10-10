package com.lenovo.iot.devicemanager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lenovo.iot.devicemanager.model.Company;
import com.lenovo.iot.devicemanager.service.CompanyService;
import com.lenovo.iot.devicemanager.util.LoginedAccountHolder;

/**
 * 企业管理
 * @author lidong
 *
 */
@RequestMapping("/company")
@Controller
public class CompanyController {

//	private static Logger log = Logger.getLogger("fordebug");

	@Autowired
	private CompanyService companyService;
    
	@RequestMapping(value = "/get.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"company:information"})
	public JSONObject get(HttpServletRequest request, HttpServletResponse response) throws Exception {

		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		JSONObject result = companyService.get(company_id);
		
		return result;
	}
	
	@RequestMapping(value = "/get_secret.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"company:setting"})
	public String getSecretKey(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		return companyService.getSecretKey(company_id);
	}
	
	@RequestMapping(value = "/update.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"company:setting"})
	public JSONObject update(@RequestBody JSONObject device_object, HttpServletRequest request, HttpServletResponse response) throws Exception {

		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		String company_name = device_object.getString("company_name");
		String address = device_object.optString("address");
		String contact = device_object.optString("contact");
		String email = device_object.optString("email");
		String remark = device_object.optString("remark");

		Company company = new Company();
		company.setCompany_id(company_id);
		company.setCompany_name(company_name);
		company.setAddress(address);
		company.setContact(contact);
		company.setEmail(email);
		company.setRemark(remark);

		JSONObject result = companyService.update(company);
		return result;
	}

	@RequestMapping(value = "/update_secret.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"company:setting"})
	public JSONObject update_secret(@RequestBody JSONObject device_secret_object, HttpServletRequest request, HttpServletResponse response) throws Exception {

		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		String company_sk = device_secret_object.getString("company_sk");

		JSONObject result = companyService.updateSecretKey(company_id, company_sk);
		return result;
	}
}
