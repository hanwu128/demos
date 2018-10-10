package com.lenovo.iot.devicemanager.service.impl;


import com.lenovo.iot.devicemanager.dao.IApacheAdgentMetaDataDao;
import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;
import com.lenovo.iot.devicemanager.service.IApacheAdgentMetaDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2017/7/19.
 */
@Service
public class ApacheAdgentMetaDataServiceImpl implements IApacheAdgentMetaDataService {

    private static Logger logger = LoggerFactory.getLogger(ApacheAdgentMetaDataServiceImpl.class);    //日志记录


    @Autowired
    private IApacheAdgentMetaDataDao _IApacheAdgentMetaDataDao;


    public void doInsertApacheAdgentMetaData(ApacheAdgentMetaData _ApacheAdgentMetaData) {
    	if(null != _ApacheAdgentMetaData) {
    		//deviceId
	        String _Hardware_device_id = _ApacheAdgentMetaData.getHardware_device_id();
	        //厂家
	        String _Hardware_manufactor = _ApacheAdgentMetaData.getHardware_manufactor();
	
	        String flag = "insert";
	        List<ApacheAdgentMetaData> list4ApacheAdgentMetaData = null;
	        if(null != _Hardware_device_id && _Hardware_device_id.trim().length() > 0 && null != _Hardware_manufactor && _Hardware_manufactor.trim().length() > 0){
	            ApacheAdgentMetaData _ApacheAdgentMetaDataParameter = new ApacheAdgentMetaData();
	            _ApacheAdgentMetaDataParameter.setHardware_device_id(_Hardware_device_id);
	            _ApacheAdgentMetaDataParameter.setHardware_manufactor(_Hardware_manufactor);
	            list4ApacheAdgentMetaData =  _IApacheAdgentMetaDataDao.findApacheAdgentMetaDataByParameter(_ApacheAdgentMetaDataParameter);
	        }
	
	        if(null != list4ApacheAdgentMetaData && list4ApacheAdgentMetaData.size()==1){
	            flag = "update";
	        }else if(null != list4ApacheAdgentMetaData && list4ApacheAdgentMetaData.size()>1){
	            flag = "update";
	            System.out.println("-----------------");
	        }
	
	        if(null != _Hardware_device_id && _Hardware_device_id.trim().length() > 0){
	            if(flag.trim().equals("insert")){
	                _IApacheAdgentMetaDataDao.doInsertApacheAdgentMetaData(_ApacheAdgentMetaData);
	            } else if(flag.trim().equals("update")){
	            	if(null != list4ApacheAdgentMetaData && list4ApacheAdgentMetaData.size() > 0){
		                ApacheAdgentMetaData _ApacheAdgentMetaDataFixed = list4ApacheAdgentMetaData.get(0);
		                Integer id = _ApacheAdgentMetaDataFixed.getId();
		                _ApacheAdgentMetaData.setId(id);
		                _IApacheAdgentMetaDataDao.doUpdateApacheAdgentMetaData(_ApacheAdgentMetaData);
	            	}
	            }
	        }
    	}
    }

    public List<ApacheAdgentMetaData> findAllApacheAdgentMetaData() {

        List<ApacheAdgentMetaData> list4ApacheAdgentMetaData = _IApacheAdgentMetaDataDao.findAllApacheAdgentMetaData();

        System.out.println("list4ApacheAdgentMetaData.size()--->>>>"+list4ApacheAdgentMetaData.size());


        return list4ApacheAdgentMetaData;
    }

    public List<ApacheAdgentMetaData> findApacheAdgentMetaDataByParameter_deviceid(ApacheAdgentMetaData _ApacheAdgentMetaData) {

        List<ApacheAdgentMetaData> listApacheAdgentMetaData = null;

        String hardware_device_id = _ApacheAdgentMetaData.getHardware_device_id();

        if(null == hardware_device_id || hardware_device_id.trim().length()<=0){
            logger.error("error:parameter hardware_device_id is null , will return null");
            return listApacheAdgentMetaData;
        }

        listApacheAdgentMetaData = _IApacheAdgentMetaDataDao.findApacheAdgentMetaDataByParameterdeviceid(_ApacheAdgentMetaData);

        return listApacheAdgentMetaData;
    }

    public void doUpdateApacheAdgentMetaData(ApacheAdgentMetaData _ApacheAdgentMetaData) {
        _IApacheAdgentMetaDataDao.doUpdateApacheAdgentMetaData(_ApacheAdgentMetaData);
    }


}
