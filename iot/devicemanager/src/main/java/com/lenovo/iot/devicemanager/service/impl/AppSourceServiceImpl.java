package com.lenovo.iot.devicemanager.service.impl;

import com.lenovo.iot.devicemanager.dao.IApacheAdgentMetaDataDao;
import com.lenovo.iot.devicemanager.dao.IAppSourceDao;
import com.lenovo.iot.devicemanager.model.bo.AppSource;
import com.lenovo.iot.devicemanager.service.IAppSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by root on 2017/8/18.
 */
@Service
public class AppSourceServiceImpl implements IAppSourceService{

    private static Logger logger = LoggerFactory.getLogger(ApacheAdgentMetaDataServiceImpl.class);    //日志记录

    @Autowired
    private IAppSourceDao _IAppSourceDao;


    public String doInsertAppSource(AppSource _AppSource) {

        if(null == _AppSource){
            return null;
        }

        _IAppSourceDao.doInsertAppSource(_AppSource);

        Integer id = _AppSource.getId();

        return id+"";
    }

    public List<AppSource> findAppSourceByParameter(AppSource _AppSource) {

        if(null == _AppSource || _AppSource.getAppid() == null || _AppSource.getAppid().trim().length()<=0){
            return null;
        }

        List<AppSource> list4AppSource = _IAppSourceDao.findAppSourceByParameter(_AppSource);

        return list4AppSource;
    }
}
