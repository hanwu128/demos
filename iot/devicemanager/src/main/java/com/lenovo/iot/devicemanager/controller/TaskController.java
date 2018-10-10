package com.lenovo.iot.devicemanager.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.devicemanager.model.Device;
import com.lenovo.iot.devicemanager.model.MqttMessage;
import com.lenovo.iot.devicemanager.model.bo.App;
import com.lenovo.iot.devicemanager.model.bo.AppSource;
import com.lenovo.iot.devicemanager.model.bo.TaskVO;
import com.lenovo.iot.devicemanager.mqtt.EmqttInstance;
import com.lenovo.iot.devicemanager.service.DeviceService;
import com.lenovo.iot.devicemanager.service.IAppService;
import com.lenovo.iot.devicemanager.service.IAppSourceService;
import com.lenovo.iot.devicemanager.service.IStreamingDeviceAppService;
import com.lenovo.iot.devicemanager.service.ITaskService;
import com.lenovo.iot.devicemanager.util.LoginedAccountHolder;
import com.lenovo.iot.devicemanager.util.ReadProperties;
import com.lenovo.iot.devicemanager.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2017/8/21.
 * SELECT SUBSTRING(from_unixtime(substring(createtimeat, 1, 10) ) ,1,19) from task
 * 按小时

 select DATE_FORMAT(create_time,'%Y%m%d%H') hours,count(caseid) count from tc_case group by hours;
 */
@RequestMapping("/TaskController")
@Controller
public class TaskController {
    protected static final Log log = LogFactory.getLog(TaskController.class);

    @Autowired
    private ITaskService _TaskServiceImpl;

    //IAppService
    @Autowired
    private IAppService _AppServiceImpl;

    @Autowired
    private IAppSourceService _AppSourceServiceImpl;

    @Autowired
    private IStreamingDeviceAppService _StreamingDeviceAppServiceImpl;

    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private EmqttInstance emqttInstance;

    // 下发app
    @RequestMapping(value = "/createTaskAndFireBatch.do")
    @ResponseBody
    @CrossOrigin(origins="*")
    @RequiresPermissions({"procmodule:manage"})
    public JSONObject createTaskAndFireBatch(@RequestBody TaskVO _TaskVO, HttpServletRequest _HttpServletRequest, HttpServletResponse _HttpServletResponse){
        JSONObject _JSONObjectResult = new JSONObject();
        JSONArray _JSONArrayResult = new JSONArray();

        Long appid = _TaskVO.getAppid();
    	if(appid == null || appid == 0) {
            _JSONObjectResult.put("actionFlag",false);
            _JSONObjectResult.put("errorMsg", "请选择需要安装的应用");
            _JSONObjectResult.put("actionResult",_JSONArrayResult);

            return _JSONObjectResult;
    	}
    	
        String[] deviceidArray = _TaskVO.getDeviceidArray();
        if(null == deviceidArray || deviceidArray.length <= 0){
            _JSONObjectResult.put("actionFlag",false);
            _JSONObjectResult.put("errorMsg", "请选择目标设备");
            _JSONObjectResult.put("actionResult",_JSONArrayResult);

            return _JSONObjectResult;
        }
        
        for(int i = 0 ; i < deviceidArray.length ; i++){
            String deviceid = deviceidArray[i];

            _TaskVO.setDeviceid(deviceid);
            JSONObject currentTranscantion = get_createTaskAndFire_Bussess(_TaskVO, _HttpServletRequest, _HttpServletResponse);
            _JSONArrayResult.add(currentTranscantion);
        }

        _JSONObjectResult.put("actionFlag", true);
        _JSONObjectResult.put("actionResult", _JSONArrayResult);

        return _JSONObjectResult;
    }

    public JSONObject get_createTaskAndFire_Bussess(TaskVO _TaskVO, HttpServletRequest _HttpServletRequest, HttpServletResponse _HttpServletResponse){
        JSONObject _JSONObjectResult = new JSONObject();

        long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
        App _App = new App();
        _App.setId(Long.valueOf(_TaskVO.getAppid()));
        List<App> list4App = _AppServiceImpl.findAppByParameter(_App);
        _App = list4App.get(0);
        String md5Str = _App.getMdfive();
        String app_name = _App.getAppname();
        String app_type = _App.getApptype();
        String app_version = _App.getAppversion();
        
        Device device = deviceService.getDeviceObject(_TaskVO.getDeviceid());
        if(device != null) {
            _JSONObjectResult.put("access_key", device.getAccess_key());
        }
        
        //目标设备上 app 版本判断
        net.sf.json.JSONObject deviceApp = deviceService.get_app(_TaskVO.getDeviceid(), app_name);
        if(deviceApp != null) {
	        String device_app_version = deviceApp.getString("app_version");
	        if(device_app_version != null && !device_app_version.isEmpty() && !StringUtil.compareVersions(device_app_version, app_version)) {
	            _JSONObjectResult.put("result", false);
	            _JSONObjectResult.put("error", "目标设备上的应用已经是新版本了");
	            return _JSONObjectResult;
	        }
        }

        if(app_type.equalsIgnoreCase("edgent")) {
            _TaskVO.setTasktype("edgent_update");
        } else {
            _TaskVO.setTasktype("app_update");
        }
        
		_TaskVO.setCompanyid(company_id);
        _TaskVO.setJarfilewholeforder(_App.getAppfilename());
        _TaskVO.setCreatetimeat(System.currentTimeMillis());
        _TaskVO.setUpdatetimeat(System.currentTimeMillis());
        TaskVO _TaskVOCallback = _TaskServiceImpl.doInsertTask(_TaskVO);

        //_JSONObjectResult.put("result", true);
        //_JSONObjectResult.put("actionResultDesc",JSONObject.toJSONString(_TaskVOCallback));

        //task create success and fire is true, send message to edget with mqtt
        if(null != _TaskVOCallback && _TaskVOCallback.getFire()){
            ReadProperties _ReadProperties = new ReadProperties();
            String file_server = _ReadProperties.readProperties("/mqtt.conf", "file_server");
            
            StringBuffer sbURL = new StringBuffer();
            sbURL.append(file_server);
            sbURL.append("/JarController/download.url?id=");
            sbURL.append(_TaskVO.getAppid());

            String topic = "$MOC/control/" + _TaskVO.getDeviceid() + "/app/update";
            
            JSONObject _JSONObjectPackage = new JSONObject();
            JSONObject _JSONObjectDesc = new JSONObject();
            _JSONObjectDesc.put("url", sbURL.toString());
            _JSONObjectDesc.put("taskId", _TaskVOCallback.getId()+"");
            _JSONObjectDesc.put("md5", md5Str);
            _JSONObjectPackage.put("desc", _JSONObjectDesc);
            _JSONObjectPackage.put("task_type", _TaskVO.getTasktype());
            String message = _JSONObjectPackage.toJSONString();
            
			MqttMessage map_msg = new MqttMessage();
			map_msg.setTopic(topic);
			map_msg.setMessage(message);
			map_msg.setQos(0);
			map_msg.setDevice_id(_TaskVO.getDeviceid());
			map_msg.setApp_name(_App.getAppname());
			map_msg.setApp_type(_App.getApptype());
			boolean result = emqttInstance.pulishMessage(map_msg);
            if(!result){
                _TaskVOCallback.setTaskstatus("failSendMqtt");
                
                _TaskServiceImpl.doUpdateTask(_TaskVOCallback);
                _JSONObjectResult.put("result", false);
                _JSONObjectResult.put("error", "无法向目标设备发送消息");
            } else {
                _TaskVOCallback.setTaskstatus("successSendMqtt");
                
                _TaskServiceImpl.doUpdateTask(_TaskVOCallback);
                _JSONObjectResult.put("result", true);
            }
        } else {
            _JSONObjectResult.put("result", false);
            _JSONObjectResult.put("error", "创建安装任务失败");
        }

        return _JSONObjectResult;
    }

    // 查询所有app列表
    @RequestMapping(value = "/listapps.do")
    @ResponseBody
    @CrossOrigin(origins="*")
    @RequiresPermissions({"procmodule:manage"})
    public JSONObject listapps(
            @RequestBody App _app,
            HttpServletRequest _HttpServletRequest,
            HttpServletResponse _HttpServletResponse){
        if(null == _app){
            return  null;
        }

        JSONObject _JSONObjectResult = new JSONObject();
        List<App> list_App;
    	Integer total_count;
       
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		if(company_id > 0) {
			_app.setCompanyid(company_id);
			
	        if(_app.getAppname() != null && !_app.getAppname().isEmpty()) {
	        	_app.setAppname("%" + _app.getAppname().trim() + "%");
	        }
	        
	        //总记录数
	        total_count = _AppServiceImpl.findAppCountByParameter(_app);
	        
	        //排序
	        if(_app.getSort_orderby() == null || _app.getSort_orderby().isEmpty() || _app.getSort_orderby().equalsIgnoreCase("createtimeatshow")) {
	        	_app.setSort_orderby("createtimeat");
	        }
	        if(_app.getSort_rule() == null || _app.getSort_rule().isEmpty() || _app.getSort_rule().equalsIgnoreCase("descending")) {
	        	_app.setSort_rule("desc");
	        } else {
	        	_app.setSort_rule("asc");
	        }

	        //分页
		    if(_app.getCurrent() == 0) {
		    	_app.setCurrent(1);
		    }
		    if(_app.getPagesize() == 0) {
		    	_app.setPagesize(20);
		    }
		    _app.setLimitStart((_app.getCurrent() - 1) * _app.getPagesize());
		    _app.setLimitEnd(_app.getPagesize());
	        list_App =  _AppServiceImpl.findAppByParameter(_app);
		} else {
			list_App = new ArrayList<App>();
			total_count = 0;
		}

        _JSONObjectResult.put("actionFlag", true);
        _JSONObjectResult.put("actionResult", list_App);

        _JSONObjectResult.put("pagesize", _app.getPagesize());
        _JSONObjectResult.put("current", _app.getCurrent());
        _JSONObjectResult.put("total", total_count);

        return _JSONObjectResult;

    }

    // app下发历史
    @RequestMapping(value = "/findTasklistbyparameters.do", method = {RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    @CrossOrigin(origins="*")
    @RequiresPermissions({"procmodule:manage"})
    public JSONObject findTasklistbyparameters(@RequestBody TaskVO _TaskVO, HttpServletRequest _HttpServletRequest, HttpServletResponse _HttpServletResponse){
        if(null == _TaskVO){
            return  null;
        }

        JSONObject _JSONObjectResult = new JSONObject();
        List<TaskVO> list_TaskVO;
    	Integer total_count;
      
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		if(company_id > 0) {
			_TaskVO.setCompanyid(company_id);
	        
	        //总记录数
	        total_count = _TaskServiceImpl.findTasklistcountbyparameters(_TaskVO);
	        
	        //排序
	        if(_TaskVO.getSort_orderby() == null || _TaskVO.getSort_orderby().isEmpty() || _TaskVO.getSort_orderby().equalsIgnoreCase("createtimeatshow")) {
	        	_TaskVO.setSort_orderby("createtimeat");
	        }
	        if(_TaskVO.getSort_orderby().equalsIgnoreCase("createtimeat")) {
	        	_TaskVO.setSort_orderby("t." + _TaskVO.getSort_orderby());
	        } else if(_TaskVO.getSort_orderby().equalsIgnoreCase("appname") || _TaskVO.getSort_orderby().equalsIgnoreCase("apptype") || _TaskVO.getSort_orderby().equalsIgnoreCase("appversion")) {
	        	_TaskVO.setSort_orderby("tt." + _TaskVO.getSort_orderby());
	        }
	        if(_TaskVO.getSort_rule() == null || _TaskVO.getSort_rule().isEmpty() || _TaskVO.getSort_rule().equalsIgnoreCase("descending")) {
	        	_TaskVO.setSort_rule("desc");
	        } else {
	        	_TaskVO.setSort_rule("asc");
	        }
	
	        //分页
		    if(_TaskVO.getCurrent() == 0) {
		    	_TaskVO.setCurrent(1);
		    }
		    if(_TaskVO.getPagesize() == 0) {
		    	_TaskVO.setPagesize(20);
		    }
		    _TaskVO.setLimitStart((_TaskVO.getCurrent() - 1) * _TaskVO.getPagesize());
		    _TaskVO.setLimitEnd(_TaskVO.getPagesize());
	
	        list_TaskVO = _TaskServiceImpl.findTasklistbyparameters(_TaskVO);
		} else {
			list_TaskVO = new ArrayList<TaskVO>();
			total_count = 0;
		}
		
        _JSONObjectResult.put("actionFlag", true);
        _JSONObjectResult.put("actionResult",list_TaskVO);

        _JSONObjectResult.put("pagesize", _TaskVO.getPagesize());
        _JSONObjectResult.put("current", _TaskVO.getCurrent());
        _JSONObjectResult.put("total", total_count);

        return _JSONObjectResult;

    }

    // app详情
    @RequestMapping(value = "/descApp.do")
    @ResponseBody
    @CrossOrigin(origins="*")
    @RequiresPermissions({"procmodule:manage"})
    public JSONObject descApp(@RequestBody App _App, HttpServletRequest _HttpServletRequest, HttpServletResponse _HttpServletResponse){
	
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		_App.setCompanyid(company_id);
		
        JSONObject _JSONObjectResult = new JSONObject();

        List<App> listApp = _AppServiceImpl.findAppByParameter(_App);

        App currentFixedApp = listApp.get(0);


        AppSource _AppSource = new AppSource();
        Long appId = currentFixedApp.getId();
        _AppSource.setAppid(String.valueOf(appId));
        List<AppSource> list_AppSource  = _AppSourceServiceImpl.findAppSourceByParameter(_AppSource);

        JSONObject dataJSONPackage = new JSONObject();
        dataJSONPackage.put("app",currentFixedApp);
        dataJSONPackage.put("appsourcearray",list_AppSource);

        _JSONObjectResult.put("actionResult",dataJSONPackage);
        log.info("_JSONObjectResult--->>>"+_JSONObjectResult);
        return _JSONObjectResult;
    }

    //TaskVO
    @RequestMapping(value = "/doUpdateTask.do")
    @ResponseBody
    @CrossOrigin(origins="*")
    public JSONObject doUpdateTask(@RequestBody TaskVO _TaskVO, HttpServletRequest _HttpServletRequest, HttpServletResponse _HttpServletResponse){
        if(null == _TaskVO || null == _TaskVO.getId() || _TaskVO.getId() == 0){
            return null;
        }

        JSONObject _JSONObjectResult = new JSONObject();
        _TaskVO.setUpdatetimeat(System.currentTimeMillis());
        _TaskServiceImpl.doUpdateTask(_TaskVO);

        _JSONObjectResult.put("actionResultFlag",true);

        return _JSONObjectResult;
    }


    // 启动/停止流式模块
    @RequestMapping(value = "/run_app.do")
    @ResponseBody
    @CrossOrigin(origins="*")
    //@RequiresPermissions({"datastream:manage"})
    public JSONObject run_app(@RequestBody String params, HttpServletRequest request, HttpServletResponse response){
    	boolean result;
    	String error;
    	
    	JSONObject js = JSONObject.parseObject(params);
    	
		String device_id = js.getString("device_id");
		String app_name = js.getString("app_name");
		String status = js.getString("status");
		
		if(device_id == null || device_id.isEmpty()) {
			result = false;
			error = "device id is empty";
		} else if(app_name == null || app_name.isEmpty()) {
			result = false;
			error = "app name is empty";
		} else if(status == null || status.isEmpty()) {
			result = false;
			error = "status is empty";
		} else {
			boolean result_pub = _TaskServiceImpl.run_app(device_id, app_name, Boolean.parseBoolean(status));
			if(result_pub) {
                //更新当前设备的当前应用的运行状态
                TaskVO vo = new TaskVO();
                vo.setAppstatus("successSendMessageToMqtt");
                vo.setDevice_id(device_id);
                vo.setAppname(app_name);
		        _StreamingDeviceAppServiceImpl.doUpdateStreamingDeviceAppByAppnameDeviceid(vo);
			}
			result = true;
			error = "pulishMessage:" + result_pub;
		}
		
        JSONObject object_result = new JSONObject();
        object_result.put("result", result);
        object_result.put("error", error);
        
        return object_result;
    }






    //TaskVO
    @RequestMapping(value = "/findAllTask.do", method = {RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    @CrossOrigin(origins="*")
    public JSONObject findAllTask(
            @RequestBody TaskVO _TaskVO,
            HttpServletRequest _HttpServletRequest,
            HttpServletResponse _HttpServletResponse){

        return get_findAllTask_bussiness(_TaskVO,_HttpServletRequest,_HttpServletResponse);
    }
    
    public JSONObject get_findAllTask_bussiness(@RequestBody TaskVO _TaskVO,
            HttpServletRequest _HttpServletRequest,
            HttpServletResponse _HttpServletResponse){
		log.info("request method: " + _HttpServletRequest.getMethod());
		
		if("options".equalsIgnoreCase(_HttpServletRequest.getMethod())) {
		_HttpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		try {
		_HttpServletResponse.getWriter().write("cdc");
		} catch (IOException e) {
		e.printStackTrace();
		}
		return null;
		}
		
		JSONObject _JSONObjectResult = new JSONObject();
		
		List<TaskVO> list_TaskVO = _TaskServiceImpl.findAllTask();
		
		
		_JSONObjectResult.put("actionResult",list_TaskVO);
		
		
		log.info("_JSONObjectResult-->>>"+_JSONObjectResult);
		
		return _JSONObjectResult;
	}


    /**
     * http://localhost:8080/devicemanager/TaskController/delete_TaskAndApp.do
     * Content-Type: application/json
       Host: localhost:8080
       Content-Length: 13
     {
     "id":82
     }

     * @param _TaskVO
     * @param _HttpServletRequest
     * @param _HttpServletResponse
     * @return
     */
//    @RequestMapping(value = "/delete_TaskAndApp.do", method = {RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.GET})
//    @ResponseBody
//    @CrossOrigin(origins="*")
//    public JSONObject delete_TaskAndApp(
//            @RequestBody TaskVO _TaskVO,
//            HttpServletRequest _HttpServletRequest,
//            HttpServletResponse _HttpServletResponse){
//
//        if(null == _TaskVO){
//            return  null;
//        }
//
//        JSONObject _JSONObjectResult = new JSONObject();
//
//        List<TaskVO> list_TaskVO = _TaskServiceImpl.findTasklistbyparameters(_TaskVO);
//
//        if(list_TaskVO.size()<=0){
//            _JSONObjectResult.put("actionFlag",false);
//            _JSONObjectResult.put("actionMessage","no id match in db");
//            return _JSONObjectResult;
//        }
//
//        TaskVO _TaskVOCurrent = list_TaskVO.get(0);
//
//        String appid = _TaskVOCurrent.getAppid();
//
//        if(null == appid || appid.trim().length()<=0){
//            return null;
//        }
//
//        _TaskServiceImpl.doDeleteTaskAndApp(_TaskVOCurrent);
//
//        _JSONObjectResult.put("actionResult",true);
//
//        return _JSONObjectResult;
//
//    }
}
