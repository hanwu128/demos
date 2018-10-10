package com.lenovo.iot.devicemanager.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.devicemanager.dao.DeviceDao;
import com.lenovo.iot.devicemanager.dao.DeviceShadowDao;
import com.lenovo.iot.devicemanager.model.Device;
import com.lenovo.iot.devicemanager.model.DeviceShadow;
import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;
import com.lenovo.iot.devicemanager.model.bo.App;
import com.lenovo.iot.devicemanager.model.bo.StreamingCallbackFlight;
import com.lenovo.iot.devicemanager.model.bo.TaskVO;
import com.lenovo.iot.devicemanager.model.vo.Messenger;
import com.lenovo.iot.devicemanager.service.DeviceService;
import com.lenovo.iot.devicemanager.service.IApacheAdgentMetaDataService;
import com.lenovo.iot.devicemanager.service.IAppService;
import com.lenovo.iot.devicemanager.service.IStreamingDeviceAppService;
import com.lenovo.iot.devicemanager.service.ITaskService;

@RequestMapping("/ApachEdgentVersionController")
@Controller
public class ApachEdgentVersionController {

    protected static final Log LOG = LogFactory.getLog(ApachEdgentVersionController.class);

    @Autowired
    private IApacheAdgentMetaDataService _ApacheAdgentMetaDataServiceImpl;

    @Autowired
    private IAppService _AppServiceImpl;

    @Autowired
    private ITaskService _TaskServiceImpl;
    //@Resource
    //private MqttPahoMessageHandler mqtt;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private IStreamingDeviceAppService _StreamingDeviceAppServiceImpl;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private DeviceShadowDao deviceShadowDao;

    /**
     *   http://localhost:8080/cdms/ApachEdgentVersionController/wakeUpAdgentServer
     *{
     "ipAgentServer": "localhost",
     "portAgentServer": "8080",
     "controllerAlias": "/cdms/ApachEdgentVersionController/apacheAdgentBroker",
     "taskId": "taskId_appId_versionCode_Born",
     "httpClinetGetOrPostFlag": "post",
     "jarFileName": "appid_version_packageTime",
     "jarWholeFilePath": "D:\\\\access5.log",
     "callbackRestfulURL": "/cdms/ApachEdgentVersionController/downloadJarVersion"
     }
     */
//    @RequestMapping(value = "/wakeUpAdgentServer.do")
//    @ResponseBody
//    @CrossOrigin(origins="*")
//    public JSONObject wakeUpAdgentServer(
//            @RequestBody Messenger _Messenger,
//            HttpServletRequest _HttpServletRequest,
//            HttpServletResponse _HttpServletResponse){
//
//        JSONObject _JSONObjectResult = new JSONObject();
//
//        try {
//            String topicName = _Messenger.getTopicName();
//            if(null == topicName || topicName.trim().length()<=0){
//                return _JSONObjectResult;
//            }
//
//            Integer qos = _Messenger.getQos();
//            if(null == qos){
//                qos = 0;
//            }
//
//            JSONObject _JSONObjectDesc = new JSONObject();
//            _JSONObjectDesc.put("url",_Messenger.getJarWholeFilePath());
//            _JSONObjectDesc.put("taskId",_Messenger.getTaskId());
//            
//            JSONObject _JSONObjectPackage = new JSONObject();
//            _JSONObjectPackage.put("task_type","app_update");
//            _JSONObjectPackage.put("desc",_JSONObjectDesc);
//
//            String message = _JSONObjectPackage.toJSONString();
//    		MqttMessage msgObj = new MqttMessage();
//    		msgObj.setTopic(topicName);
//    		msgObj.setMessage(message);
//    		msgObj.setQos(qos);
//    		msgObj.setDevice_id();
//    		msgObj.setApp_name();
//    		msgObj.setApp_type();
//    		boolean result = EmqttInstance.pulishMessage(msgObj);
//            if(!result){
//                _JSONObjectResult.put("actionResultFlag",false);
//            }else {
//                _JSONObjectResult.put("actionResultFlag",true);
//            }
//        } catch (Exception e) {
//
//            _JSONObjectResult.put("actionResultFlag",false);
//            _JSONObjectResult.put("actionResultDesc",e.getMessage());
//            e.printStackTrace();
//        }
//
//        return _JSONObjectResult;
//    }

//    @RequestMapping(value = "/checkFromApacheAdgent.do")
//    @ResponseBody
//    @CrossOrigin(origins="*")
//    public JSONObject checkFromApacheAdgent(
//            @RequestBody Messenger _Messenger,
//            HttpServletRequest _HttpServletRequest,
//            HttpServletResponse _HttpServletResponse){
//
//
//        JSONObject _JSONObjectResult = new JSONObject();
//
//        if(null == _Messenger){
//            String message = "error:null == _Messenger";
//            LOG.error(message);
//            _JSONObjectResult.put("actionFlag",false);
//            _JSONObjectResult.put("message",message);
//            return _JSONObjectResult;
//        }
//
//        String taskId = _Messenger.getTaskId();
//
//        if(null == taskId || taskId.trim().length()<=0){
//            String message = "null == taskId || taskId.trim().length()<=0";
//            LOG.error(message);
//            _JSONObjectResult.put("actionFlag",false);
//            _JSONObjectResult.put("message",message);
//            return _JSONObjectResult;
//        }
//
//        boolean _StatusCallbackFromApacheAdgetn = _Messenger.getStatusCallbackFromApacheAdgetn();
//        _JSONObjectResult.put("actionFlag",true);
//        _JSONObjectResult.put("message","taskid:"+taskId+" status is :"+_StatusCallbackFromApacheAdgetn +" already inserver db");
//
//        return _JSONObjectResult;
//    }

    // app更新后的设备回调
    @RequestMapping(value = "/checkApacheAdgentBroker.url")
    @ResponseBody
    @CrossOrigin(origins="*")
    public JSONObject checkApacheAdgentBroker(@RequestBody Messenger _Messenger, HttpServletRequest _HttpServletRequest, HttpServletResponse _HttpServletResponse){
		// {"taskId":task_id, "statusCallbackFromApacheAdgetn":true}
        String taskId = _Messenger.getTaskId();
        boolean taskStatus = _Messenger.getStatusCallbackFromApacheAdgetn();
        
        TaskVO _TaskVO = new TaskVO();
        _TaskVO.setId(Long.valueOf(taskId));
        _TaskVO.setTaskstatus(Boolean.toString(taskStatus));
        _TaskVO.setAppstatus("true");
        _TaskServiceImpl.doUpdateTask(_TaskVO);

        //App在更新后，如果该设备有对应的流和镜像在运行，应该立即运行该app
        TaskVO task = new TaskVO();
        task.setId(Long.valueOf(taskId));
        List<TaskVO> taskList = _TaskServiceImpl.findTasklistbyparameters(task);
        if(taskList != null && taskList.size() == 1) {
	        task = taskList.get(0);
	        
	        Device device = deviceDao.getDevice(task.getDeviceid());
			//根据流状态启动app - 首先获取当前设备的指定appname的流
        	App app = new App();
        	app.setId(task.getAppid());
        	List<App> list_AppFixed = _AppServiceImpl.findAppByParameter(app);
			if(list_AppFixed != null && list_AppFixed.size() > 0) {
				if(app.getApptype().equals("stream")) {
					StreamingCallbackFlight streaming = new StreamingCallbackFlight();
					streaming.setIslive("false");
					streaming.setDeviceid(device.getId());
					streaming.setAppname(app.getAppname());
					List<StreamingCallbackFlight> listForStreamingCallbackFlight = _StreamingDeviceAppServiceImpl.findStreamingDeviceAppVOBaseByParameters(streaming);
					if(listForStreamingCallbackFlight != null && listForStreamingCallbackFlight.size() > 0) {
						for(StreamingCallbackFlight s : listForStreamingCallbackFlight) {
							if(s.getStatus().equalsIgnoreCase("true")) {
								_TaskServiceImpl.run_app(device.getDevice_id(), s.getAppname(), true);
							}
						}
					}
				} else if(app.getApptype().equals("shadow")) {
					//根据镜像状态启动app
					List<DeviceShadow> deviceShadowList = deviceShadowDao.getDeviceShadowListByDeviceId(0, device.getDevice_id());
					if(deviceShadowList != null && deviceShadowList.size() > 0) {
						for(DeviceShadow shadow : deviceShadowList) {
							if(shadow.getApp_name().equalsIgnoreCase(app.getAppname())) {
								_TaskServiceImpl.run_app(device.getDevice_id(), shadow.getApp_name(), true);
							}
						}
					}
				}
			}
        }

        JSONObject _JSONObjectResult = new JSONObject();
        _JSONObjectResult.put("actionResultFlag",true);

        return _JSONObjectResult;
    }

//    @RequestMapping(value = "/showAllApacheAdgentMetaData.do")
//    @ResponseBody
//    @CrossOrigin(origins="*")
//    public JSONObject showAllApacheAdgentMetaData(HttpServletRequest _HttpServletRequest, HttpServletResponse _HttpServletResponse){
//
//        JSONObject _JSONObject4Result = new JSONObject();
//        List<ApacheAdgentMetaData> list4ApacheAdgentMetaData =  _ApacheAdgentMetaDataServiceImpl.findAllApacheAdgentMetaData();
//
//        _JSONObject4Result.put("actionFlag",true);
//        _JSONObject4Result.put("resultSize",list4ApacheAdgentMetaData.size());
//        _JSONObject4Result.put("result",list4ApacheAdgentMetaData);
//
//        return _JSONObject4Result;
//    }
//
//    @RequestMapping(value = "/findApacheAdgentMetaDataByParameter_deviceid.do")
//    @ResponseBody
//    @CrossOrigin(origins="*")
//    public JSONObject findApacheAdgentMetaDataByParameter_deviceid(
//            @RequestBody ApacheAdgentMetaData _ApacheAdgentMetaData,
//            HttpServletRequest _HttpServletRequest,
//            HttpServletResponse _HttpServletResponse){
//
//        JSONObject _JSONObject4Result = new JSONObject();
//
//        Boolean actionResultFlag = true;
//
//        if(null == _ApacheAdgentMetaData ||
//                _ApacheAdgentMetaData.getHardware_device_id() == null ||
//                _ApacheAdgentMetaData.getHardware_device_id().trim().length()<=0){
//            actionResultFlag =false;
//            _JSONObject4Result.put("actionResultFlag",actionResultFlag);
//            _JSONObject4Result.put("actionResultDesc","null is hardware_device_id");
//            return _JSONObject4Result;
//        }
//
//        List<ApacheAdgentMetaData> list4ApacheAdgentMetaData = _ApacheAdgentMetaDataServiceImpl.findApacheAdgentMetaDataByParameter_deviceid(_ApacheAdgentMetaData);
//
//        _JSONObject4Result.put("actionResultFlag",actionResultFlag);
//        _JSONObject4Result.put("actionResult",list4ApacheAdgentMetaData);
//
//        return _JSONObject4Result;
//
//    }


    @RequestMapping(value = "/insertApacheAdgentMetaData.url")
    @ResponseBody
    @CrossOrigin(origins="*")
    public JSONObject insertApacheAdgentMetaData(@RequestBody ApacheAdgentMetaData _ApacheAdgentMetaData, HttpServletRequest _HttpServletRequest, HttpServletResponse _HttpServletResponse){

        JSONObject _JSONObjectResult = new JSONObject();
        String __Hardware_device_id = _ApacheAdgentMetaData.getHardware_device_id();
        if(null == __Hardware_device_id || __Hardware_device_id.trim().length()<=0){
            _JSONObjectResult.put("actionResultFlag",false);
            _JSONObjectResult.put("actionResultDesc","null is hardware_device_id");
            return _JSONObjectResult;
        }

        Device device = deviceDao.getDevice(__Hardware_device_id);
        if(null == device){
            _JSONObjectResult.put("actionResultFlag",false);
            _JSONObjectResult.put("actionResultDesc","no device was found:" + __Hardware_device_id);
            return _JSONObjectResult;
        }
        
        String device_id = device.getDevice_id();
        String edgent_agent_app_list = _ApacheAdgentMetaData.getEdgent_agent_app_list();
        if(null != edgent_agent_app_list && edgent_agent_app_list.trim().length()>0){
            JSONArray _JSONArray__edgent_agent_app_list = JSONArray.parseArray(edgent_agent_app_list);
            if(_JSONArray__edgent_agent_app_list != null) {
	            for(int i = 0 ; i < _JSONArray__edgent_agent_app_list.size(); i++){
	                JSONObject currentJSONObjectTemp = _JSONArray__edgent_agent_app_list.getJSONObject(i);
	                String current__app_name = currentJSONObjectTemp.getString("app_name");
	                String current__is_running = currentJSONObjectTemp.getString("is_running");
	                
	                //更新当前设备的当前应用的运行状态
	                TaskVO vo = new TaskVO();
	                vo.setDevice_id(device_id);
	                vo.setAppname(current__app_name);
	                vo.setAppstatus(current__is_running);
	                _StreamingDeviceAppServiceImpl.doUpdateStreamingDeviceAppByAppnameDeviceid(vo);
	            }
            }
        }

        ApacheAdgentMetaData _ApacheAdgentMetaDataParameters = new ApacheAdgentMetaData();
        _ApacheAdgentMetaDataParameters.setHardware_device_id(__Hardware_device_id);
        List<ApacheAdgentMetaData> listApacheAdgentMetaDataCheck = _ApacheAdgentMetaDataServiceImpl.findApacheAdgentMetaDataByParameter_deviceid(_ApacheAdgentMetaDataParameters);

        //更新元数据
        if(null != listApacheAdgentMetaDataCheck && listApacheAdgentMetaDataCheck.size()>0){
        	_ApacheAdgentMetaData.setId(listApacheAdgentMetaDataCheck.get(0).getId());
            _ApacheAdgentMetaDataServiceImpl.doUpdateApacheAdgentMetaData(_ApacheAdgentMetaData);
            _JSONObjectResult.put("actionResultFlag",true);
            return _JSONObjectResult;
        } else {
	        String _Hardware_manufactor = _ApacheAdgentMetaData.getHardware_manufactor();
	        if(null == _Hardware_manufactor || _Hardware_manufactor.trim().length()<=0){
	            _JSONObjectResult.put("actionResultFlag",false);
	            _JSONObjectResult.put("actionResultDesc","null is _Hardware_manufactor");
	            //return _JSONObjectResult;
	            _Hardware_manufactor = "";
	        }
	
	        _ApacheAdgentMetaDataServiceImpl.doInsertApacheAdgentMetaData(_ApacheAdgentMetaData);
	        _JSONObjectResult.put("actionResultFlag",true);
	        return _JSONObjectResult;
        }
    }
}
