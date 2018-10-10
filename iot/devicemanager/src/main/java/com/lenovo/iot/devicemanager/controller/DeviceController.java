package com.lenovo.iot.devicemanager.controller;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.lenovo.iot.devicemanager.model.Device;
import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;
import com.lenovo.iot.devicemanager.service.DeviceService;
import com.lenovo.iot.devicemanager.service.IApacheAdgentMetaDataService;
import com.lenovo.iot.devicemanager.util.LoginedAccountHolder;
import com.lenovo.iot.devicemanager.util.Pagination;
import com.lenovo.iot.devicemanager.util.WebUtil;

/**
 * 设备接入
 * @author lidong
 *
 */
@RequestMapping("/device")
@Controller
public class DeviceController {

//	private static Logger log = Logger.getLogger("fordebug");

	@Autowired
	private DeviceService deviceService;
    @Autowired
    private IApacheAdgentMetaDataService metaService;
    
	@RequestMapping(value = "/get.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"edge:manage"})
	public JSONObject device_get(HttpServletRequest request, HttpServletResponse response) throws Exception {

		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		String device_id = WebUtil.getPara(request, "device_id");
		JSONObject result = deviceService.getDevice(company_id, device_id);
		
		return result;
	}

	/**
	 * auto register device
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/register.url", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject device_register(@RequestBody JSONObject device_object, HttpServletRequest request, HttpServletResponse response) throws Exception {

		long company_id = device_object.optLong("company_id");
		String company_sk = device_object.optString("company_sk");
		String access_key = device_object.optString("access_key");
		JSONObject result = deviceService.registerDevice(company_id, company_sk, access_key);
		
		return result;
	}
	
    /**
     * update device meta
     * @param _Messenger
     * @param _HttpServletRequest
     * @param _HttpServletResponse
     * @return
     */
    @RequestMapping(value = "/meta.url")
    @ResponseBody
    @CrossOrigin(origins="*")
    public JSONObject device_meta(@RequestBody JSONObject device_meta, HttpServletRequest request, HttpServletResponse response) {
/*
    	{
    	    "deviceId ": "",
    	    "hardware_model": "",
    	    "hardware_manufactor": "",
    	    "hardware_os": "Raspbian",
    	    "hardware_os_version": "8.0",
    	    "hardware_location": "bj_ZN",
    	    "firmware_name": "",
    	    "firmware_version": "",
    	    "host_ip": "192.168.0.101"
    	}
*/
    	String device_id = device_meta.getString("deviceId");
    	String hardware_model = device_meta.optString("hardware_model");
    	String hardware_manufactor = device_meta.optString("hardware_manufactor");
    	String hardware_os = device_meta.optString("hardware_os");
    	String hardware_os_version = device_meta.optString("hardware_os_version");
    	String hardware_location = device_meta.optString("hardware_location");
    	//String firmware_name = device_meta.optString("firmware_name");
    	String firmware_version = device_meta.optString("firmware_version");
    	String host_ip = device_meta.optString("host_ip");
    	
        ApacheAdgentMetaData _ApacheAdgentMetaDataParameters = new ApacheAdgentMetaData();
        _ApacheAdgentMetaDataParameters.setHardware_device_id(device_id);
        List<ApacheAdgentMetaData> listApacheAdgentMetaDataCheck = metaService.findApacheAdgentMetaDataByParameter_deviceid(_ApacheAdgentMetaDataParameters);

        //更新元数据
    	ApacheAdgentMetaData _ApacheAdgentMetaData = new ApacheAdgentMetaData();
    	_ApacheAdgentMetaData.setHardwaredeviceid(device_id);
    	_ApacheAdgentMetaData.setHardwaremodel(hardware_model);
    	_ApacheAdgentMetaData.setHardwaremanufactor(hardware_manufactor);
    	_ApacheAdgentMetaData.setHardwareos(hardware_os);
    	_ApacheAdgentMetaData.setHardwareosversion(hardware_os_version);
    	_ApacheAdgentMetaData.setHardwarelocation(hardware_location);
    	_ApacheAdgentMetaData.setFirmwareversion(firmware_version);
    	_ApacheAdgentMetaData.setHostip(host_ip);
        if(null != listApacheAdgentMetaDataCheck && listApacheAdgentMetaDataCheck.size() > 0){
        	_ApacheAdgentMetaData.setId(listApacheAdgentMetaDataCheck.get(0).getId());
        	metaService.doUpdateApacheAdgentMetaData(_ApacheAdgentMetaData);
        } else {
	        metaService.doInsertApacheAdgentMetaData(_ApacheAdgentMetaData);
        }
        
        JSONObject _JSONObjectResult = new JSONObject();
        _JSONObjectResult.put("result", true);
        return _JSONObjectResult;
    }

	@RequestMapping(value = "/add.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"edge:manage"})
	public JSONObject device_add(@RequestBody JSONObject device_object, HttpServletRequest request, HttpServletResponse response) throws Exception {

		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		String access_key = device_object.optString("access_key");
		String device_desc = device_object.optString("device_desc");

		Device device = new Device();
		device.setCompany_id(company_id);
		device.setAccess_key(access_key);
		device.setDevice_desc(device_desc);

		JSONObject result = deviceService.addDevice(device);
		return result;
	}

	@RequestMapping(value = "/add_batch_check.do")
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"edge:manage"})
	public JSONObject add_batch_check(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject result = null;
		
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if(multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
            Iterator iter = multiRequest.getFileNames();
            while(iter.hasNext())
            {
                //一次遍历所有文件
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if(file != null)
                {
                	result = deviceService.addDevice_batch_check(company_id, file.getInputStream());
                	break;
                }
            }
        }
        
        if(result == null) {
        	result = new JSONObject();
        	result.put("result", false);
        	result.put("error", "未上传附件");
        }
        
        return result;
	}
	
	@RequestMapping(value = "/add_batch.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"edge:manage"})
	public JSONObject add_batch_check(@RequestBody JSONObject device_object, HttpServletRequest req, HttpServletResponse resp) {
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		boolean result = device_object.getBoolean("result");
		if(result) {
			JSONArray device_list = device_object.optJSONArray("rows");
			return deviceService.addDevice_batch(company_id, device_list);
		} else {
			JSONObject object = new JSONObject();
			object.put("result", result);
			object.put("error", "文件格式错误");
			return object;
		}
	}
	
	@RequestMapping(value = "/delete.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"edge:manage"})
	public JSONObject device_delete(@RequestBody JSONObject device_object, HttpServletRequest request, HttpServletResponse response) throws Exception {

		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		String device_id = device_object.getString("device_id");
		JSONObject result = deviceService.deleteDevice(company_id, device_id);
		return result;
	}

//	@RequestMapping(value = "/update.do", produces = { "application/json;charset=UTF-8" })
//	@ResponseBody
//	@CrossOrigin(origins="*")
//	@RequiresPermissions({"edge:manage"})
//	public JSONObject device_update(@RequestBody JSONObject device_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
//		String device_id = device_object.getString("device_id");
//		//String policy_name = device_object.getString("policy_name");
//		String access_key = device_object.getString("access_key");
//		String device_desc = device_object.optString("device_desc");
//
//		Device device = new Device();
//		device.setDevice_id(device_id);
//		device.setCompany_id(company_id);
//		//device.setPolicy_name(policy_name);
//		device.setAccess_key(access_key);
//		device.setDevice_desc(device_desc);
//
//		JSONObject result = deviceService.updateDevice(device);
//		return result;
//	}

	@RequestMapping(value = "/update_secret.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"edge:manage"})
	public JSONObject device_update_secret(@RequestBody JSONObject device_secret_object, HttpServletRequest request, HttpServletResponse response) throws Exception {

		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		String device_id = device_secret_object.getString("device_id");
		String secret_old = device_secret_object.getString("secret");

		JSONObject result = deviceService.updateDeviceSecretKey(device_id, company_id, secret_old);
		return result;
	}

	@RequestMapping(value = "/list.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"edge:manage"})
	public JSONObject device_list(@RequestBody JSONObject device_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		String access_key = device_object.optString("access_key");
		String adgent_version = device_object.optString("edgent_version");
		int pagesize = device_object.optInt("pagesize");
		int current = device_object.optInt("current");
		String orderby = device_object.optString("sort_orderby");
		String asc = device_object.optString("sort_rule");
		
		Pagination page = new Pagination();
		page.setPagesize(pagesize);
		page.setCurrent(current);
		page.setOrder_by(orderby);
		page.setAsc(asc);
		
		JSONObject result = deviceService.getDeviceListData(page, company_id, access_key, adgent_version, false);
		return result;
	}

	@RequestMapping(value = "/list_version.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"edge:manage"})
	public JSONArray device_list_version(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		JSONArray result = deviceService.device_list_version();
		return result;
	}
	
	@RequestMapping(value = "/online_history.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject device_online_history(@RequestBody JSONObject device_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String device_id = device_object.optString("device_id");
		int pagesize = device_object.optInt("pagesize");
		int current = device_object.optInt("current");
		String orderby = device_object.optString("sort_orderby");
		String asc = device_object.optString("sort_rule");
		
		Pagination page = new Pagination();
		page.setPagesize(pagesize);
		page.setCurrent(current);
		page.setOrder_by(orderby);
		page.setAsc(asc);
		
		JSONObject result = deviceService.getDeviceOnlineHistory(page, device_id);
		return result;
	}
	
	@RequestMapping(value = "/list2.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject device_list2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		String access_key = WebUtil.getPara(request, "access_key");
		JSONObject result = deviceService.getDeviceListData(null, company_id, access_key, "", true);
		return result;
	}
	
	@RequestMapping(value = "/reboot.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	@RequiresPermissions({"edge:manage"})
	public JSONObject device_reboot(@RequestBody JSONObject device_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String device_id = device_object.optString("device_id");
		JSONObject result = deviceService.reboot(device_id);
		return result;
	}

	@RequestMapping(value = "/online.url", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject device_online(@RequestBody JSONObject device_online_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
/*
		{
			"clientid": "10000100000DC30AA8C6D1319AA69432",
			"username": "deviceid_for_tomcat1",
			"ipaddress": "172.17.171.17",
			"clean_sess": false,
			"protocol": 4,
			"connack": 0,
			"ts": 1526472492
		}
*/
/*
		{
			"clientid": "10000100000E9BBCF0BBFF7B4B075929",
			"reason": "closed",
			"ts": 1526472489
		}
 */
		String device_id = device_online_object.getString("clientid");
		long ts = device_online_object.getLong("ts");
		String username = device_online_object.optString("username");
		String ipaddress = device_online_object.optString("ipaddress");
		int status = (username.isEmpty() ? 0 : 1);
		
		JSONObject result = deviceService.setOnlineStatus(device_id, username, status, ts, ipaddress);
		return result;
	}
	
	@RequestMapping(value = "/get_message_statistics.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONArray get_message_statistics(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String device_id = WebUtil.getPara(request, "device_id");
		String app_name = WebUtil.getPara(request, "app_name");
		JSONArray result = deviceService.getDeviceMqttMessageStatistic(device_id, app_name);
		return result;
	}
	
	@RequestMapping(value = "/get_message_list.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject get_message_list(@RequestBody JSONObject message_object, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String device_id = message_object.optString("device_id");
		String app_name = message_object.optString("app_name");
		int pagesize = message_object.optInt("pagesize");
		int current = message_object.optInt("current");
		String orderby = message_object.optString("sort_orderby");
		String asc = message_object.optString("sort_rule");
		
		Pagination page = new Pagination();
		page.setPagesize(pagesize);
		page.setCurrent(current);
		page.setOrder_by(orderby);
		page.setAsc(asc);
		JSONObject result = deviceService.getDeviceMqttMessageList(page, device_id, app_name, false);
		return result;
	}
	
	//待作废
//	@RequestMapping(value = "/get_applist.do", produces = { "application/json;charset=UTF-8" })
//	@ResponseBody
//	@CrossOrigin(origins="*")
//	public JSONObject get_applist(@RequestBody JSONObject app, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
//
//		Long did = 0l;
//		if(app.containsKey("did")) {
//			did = app.getLong("did");
//		}
//		String device_id = null;
//		if(app.containsKey("device_id")) {
//			device_id = app.getString("device_id");
//		}
//		String app_type = app.getString("app_type");
//		List<App> listApp = deviceService.get_applist(did, device_id, company_id, app_type);
//		
//		JSONObject _JSONObjectResult = new JSONObject();
//        _JSONObjectResult.put("actionFlag",true);
//        _JSONObjectResult.put("actionResult",listApp);
//
//        return _JSONObjectResult;
//    }
	
	@RequestMapping(value = "/get_applist2.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONObject get_applist2(@RequestBody JSONObject app, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();

		String device_id = app.optString("device_id");
		String app_type = app.optString("app_type");
		JSONArray listApp = deviceService.get_applist2(device_id, app_type);
		
		JSONObject _JSONObjectResult = new JSONObject();
        _JSONObjectResult.put("actionFlag", true);
        _JSONObjectResult.put("actionResult", listApp);

        return _JSONObjectResult;
    }
}
