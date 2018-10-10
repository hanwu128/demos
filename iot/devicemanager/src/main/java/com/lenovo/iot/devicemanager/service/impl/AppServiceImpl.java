package com.lenovo.iot.devicemanager.service.impl;

import com.lenovo.iot.devicemanager.dao.IApacheAdgentMetaDataDao;
import com.lenovo.iot.devicemanager.dao.IAppDao;
import com.lenovo.iot.devicemanager.model.bo.App;
import com.lenovo.iot.devicemanager.service.IAppService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by root on 2017/8/18.
 */

@Service
public class AppServiceImpl implements IAppService{

    private static Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);    //日志记录

    @Autowired
    private IAppDao _IAppDao;


    public String doInsertApp(com.lenovo.iot.devicemanager.model.bo.App _App) {

        _IAppDao.doInsertApp(_App);

        Long iIAppPrimaryKey = _App.getId();

        return iIAppPrimaryKey+"";
    }

    public List<com.lenovo.iot.devicemanager.model.bo.App> findAppByParameter(App _App) {

        List<App> list4App = _IAppDao.findAppByParameter(_App);

        return list4App;
    }
    public Integer findAppCountByParameter(App _App) {
    	return _IAppDao.findAppCountByParameter(_App);
    }
}
