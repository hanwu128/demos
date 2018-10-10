package com.lenovo.iot.devicemanager.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lenovo.iot.devicemanager.dao.IStreamingDeviceAppDao;
import com.lenovo.iot.devicemanager.model.Device;
import com.lenovo.iot.devicemanager.model.MqttMessage;
import com.lenovo.iot.devicemanager.model.bo.App;
import com.lenovo.iot.devicemanager.model.bo.StreamingCallbackFlight;
import com.lenovo.iot.devicemanager.model.bo.StreamingDeviceAppVO;
import com.lenovo.iot.devicemanager.model.bo.TaskVO;
import com.lenovo.iot.devicemanager.mqtt.EmqttInstance;
import com.lenovo.iot.devicemanager.service.DeviceService;
import com.lenovo.iot.devicemanager.service.IAppService;
import com.lenovo.iot.devicemanager.service.IStreamingDeviceAppService;
import com.lenovo.iot.devicemanager.service.ITaskService;

/**
 * Created by root on 2017/8/24.
 */
@Service
public class StreamingDeviceAppServiceImpl implements IStreamingDeviceAppService {

    private static Logger log = LoggerFactory.getLogger(StreamingDeviceAppServiceImpl.class);    //日志记录

    @Autowired
    private IStreamingDeviceAppDao _IStreamingDeviceAppDao;

    @Autowired
    private IAppService _AppServiceImpl;

    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private ITaskService _TaskServiceImpl;
    
    @Autowired
    private EmqttInstance emqttInstance;

    public StreamingDeviceAppVO doInsertStreamingDeviceApp(StreamingDeviceAppVO _StreamingDeviceAppVO) {

        if(null == _StreamingDeviceAppVO){
            return null;
        }

        _IStreamingDeviceAppDao.doInsertStreamingDeviceApp(_StreamingDeviceAppVO);

        return _StreamingDeviceAppVO;
    }

    public List<StreamingDeviceAppVO> findStreamingDeviceAppVOByParameter(StreamingDeviceAppVO _StreamingDeviceAppVO) {

        if(null == _StreamingDeviceAppVO){
            return null;
        }

        List<StreamingDeviceAppVO> List_ApacheAdgentMetaData = _IStreamingDeviceAppDao.findStreamingDeviceAppVOByParameter(_StreamingDeviceAppVO);

        return List_ApacheAdgentMetaData;
    }

    public void doUpdateStreamingDeviceAppVO(StreamingDeviceAppVO _StreamingDeviceAppVO) {
        _IStreamingDeviceAppDao.doUpdateStreamingDeviceAppVO(_StreamingDeviceAppVO);

    }
    
    public void deleteStreamingDeviceApp(long company_id, long id) {
    	StreamingDeviceAppVO _StreamingDeviceAppVO = new StreamingDeviceAppVO();
		_StreamingDeviceAppVO.setCompanyid(company_id);
		_StreamingDeviceAppVO.setId(id);
        _StreamingDeviceAppVO.setIslive("false");

        this.doUpdateStreamingDeviceAppVO(_StreamingDeviceAppVO);
        
        //删除流时向设备发送消息清除MAP、清除FILTER、停止应用
        String deviceId = "", appName = "";
        List<StreamingDeviceAppVO> list = this.findStreamingDeviceAppVOByParameter(_StreamingDeviceAppVO);
        if(list.size() > 0) {
        	StreamingDeviceAppVO streamingDeviceApp = list.get(0);
        	if(streamingDeviceApp != null) {
        		Device device = deviceService.getDeviceByPrimaryKey(streamingDeviceApp.getDeviceid());
        		if(device != null) {
	            	deviceId = device.getDevice_id();
	            	appName = streamingDeviceApp.getAppname();
	            	if(!deviceId.isEmpty() && appName != null && !appName.isEmpty()) {
	            		String map_topic = "$MOC/control/" + deviceId + "/map/update";
	        			String map_message = EmqttInstance.buildMap(appName, null);
	        			
	        			MqttMessage map_msg = new MqttMessage();
	        			map_msg.setTopic(map_topic);
	        			map_msg.setMessage(map_message);
	        			map_msg.setQos(0);
	        			map_msg.setDevice_id(deviceId);
	        			map_msg.setApp_name(appName);
	        			map_msg.setApp_type("stream");
	        			emqttInstance.pulishMessage(map_msg);
	        			
	        			String filter_topic = "$MOC/control/" + deviceId + "/filter/update";
	        			String filter_message = EmqttInstance.buildFilter(appName, null);
	        			MqttMessage filter_msg = new MqttMessage();
	        			filter_msg.setTopic(filter_topic);
	        			filter_msg.setMessage(filter_message);
	        			filter_msg.setQos(0);
	        			filter_msg.setDevice_id(deviceId);
	        			filter_msg.setApp_name(appName);
	        			filter_msg.setApp_type("stream");
	        			emqttInstance.pulishMessage(filter_msg);
	        			
	            		_TaskServiceImpl.run_app(deviceId, appName, false);
	            	}
        		}
        	}
        }
    }

//    public List<ApacheAdgentMetaData> findAllStreamingDeviceAppVO() {
//        List<ApacheAdgentMetaData> list_ApacheAdgentMetaData = _IStreamingDeviceAppDao.findAllStreamingDeviceAppVO();
//        return list_ApacheAdgentMetaData;
//    }

    public List<StreamingCallbackFlight> findStreamingDeviceAppVOBaseById(StreamingCallbackFlight _StreamingCallbackFlight) {

        if(null == _StreamingCallbackFlight){
            return null;
        }

        List<StreamingCallbackFlight> listStreamingCallbackFlight = _IStreamingDeviceAppDao.findStreamingDeviceAppVOBaseById(_StreamingCallbackFlight);

        return listStreamingCallbackFlight;
    }

    public List<StreamingCallbackFlight> findStreamingDeviceAppVOBaseByParameters(StreamingCallbackFlight _StreamingCallbackFlight) {

        if(null == _StreamingCallbackFlight){
            return null;
        }

        List<StreamingCallbackFlight> listStreamingCallbackFlight = _IStreamingDeviceAppDao.findStreamingDeviceAppVOBaseByParameters(_StreamingCallbackFlight);

        return listStreamingCallbackFlight;
    }
    public Integer findStreamingDeviceAppVOBaseCountByParameters(StreamingCallbackFlight _StreamingCallbackFlight) {
    	return _IStreamingDeviceAppDao.findStreamingDeviceAppVOBaseCountByParameters(_StreamingCallbackFlight);
    }








//    public List<StreamingCallbackFlight> findAllStreamingDeviceAppVOBaseBy(StreamingCallbackFlight _StreamingCallbackFlight) {
//
//        List<StreamingCallbackFlight> listForStreamingCallbackFlight = _IStreamingDeviceAppDao.findAllStreamingDeviceAppVOBaseBy(new StreamingCallbackFlight());
//
//        return listForStreamingCallbackFlight;
//    }

    public void doUpdateStreamingDeviceAppByAppnameDeviceid(TaskVO taskVO) {
    	_IStreamingDeviceAppDao.doUpdateStreamingDeviceAppByAppnameDeviceid(taskVO);
    }
    
    public List<StreamingCallbackFlight> getDeviceStreamings(StreamingCallbackFlight streamingCallbackFlight) {
    	List<StreamingCallbackFlight> listForStreamingCallbackFlight = _IStreamingDeviceAppDao.getDeviceStreamings(streamingCallbackFlight);
    	return listForStreamingCallbackFlight;
    }
}
