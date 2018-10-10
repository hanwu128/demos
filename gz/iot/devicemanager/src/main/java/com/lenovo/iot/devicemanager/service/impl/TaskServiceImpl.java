package com.lenovo.iot.devicemanager.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.devicemanager.dao.ITaskDao;
import com.lenovo.iot.devicemanager.model.MqttMessage;
import com.lenovo.iot.devicemanager.model.bo.TaskVO;
import com.lenovo.iot.devicemanager.mqtt.EmqttInstance;
import com.lenovo.iot.devicemanager.service.ITaskService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by root on 2017/8/21.
 */


@Service
public class TaskServiceImpl implements ITaskService{

    private static Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);    //日志记录

    @Autowired
    private ITaskDao _ITaskDao;

    @Autowired
    private EmqttInstance emqttInstance;

    public TaskVO doInsertTask(TaskVO _TaskVO) {

        if(null == _TaskVO){
            log.error("error:null == _TaskVO");
            return null;
        }

        _ITaskDao.doInsertTask(_TaskVO);

        return  _TaskVO;
    }

    public void doUpdateTask(TaskVO _TaskVO) {
        if(null == _TaskVO){
            log.error("error:null == _TaskVO");
            return;
        }
        _ITaskDao.doUpdateTask(_TaskVO);
    }

    public List<TaskVO> findAllTask() {
        List<TaskVO> listTaskVO= _ITaskDao.findAllTask();
        return listTaskVO;
    }

    public List<TaskVO> findTasklistbyparameters(TaskVO _TaskVO){
        List<TaskVO> listTaskVO= _ITaskDao.findTasklistbyparameters(_TaskVO);
        return listTaskVO;
    }
    public Integer findTasklistcountbyparameters(TaskVO _TaskVO) {
    	return _ITaskDao.findTasklistcountbyparameters(_TaskVO);
    }

    public void doDeleteTaskAndApp(TaskVO _TaskVO) {

        _ITaskDao.doDeleteTask(_TaskVO);

        _ITaskDao.doDeleteApp(_TaskVO);

    }



    public void doUpdateTaskByAppname(TaskVO _TaskVO) {
    	_ITaskDao.doUpdateTaskByAppname(_TaskVO);
    }

    public boolean run_app(String device_id, String app_name, boolean status) {
    	String topic = "$MOC/control/" + device_id + "/app/run";
    	
		JSONObject object = new JSONObject();
		object.put("app_name", app_name);
		object.put("status", status);
		
		String message = object.toString();

		MqttMessage msgObj = new MqttMessage();
		msgObj.setTopic(topic);
		msgObj.setMessage(message);
		msgObj.setQos(0);
		msgObj.setDevice_id(device_id);
		msgObj.setApp_name(app_name);
		//msgObj.setApp_type("stream");
		boolean result = emqttInstance.pulishMessage(msgObj);
		
		return result;
    }
}
