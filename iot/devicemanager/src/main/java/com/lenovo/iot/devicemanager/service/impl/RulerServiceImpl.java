package com.lenovo.iot.devicemanager.service.impl;

import com.lenovo.iot.devicemanager.dao.IApacheAdgentMetaDataDao;
import com.lenovo.iot.devicemanager.dao.IRulerDao;
import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;
import com.lenovo.iot.devicemanager.model.bo.RulerVO;
import com.lenovo.iot.devicemanager.service.IRulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by root on 2017/8/29.
 */
@Service
public class RulerServiceImpl implements IRulerService{

    private static Logger logger = LoggerFactory.getLogger(RulerServiceImpl.class);    //日志记录

    @Autowired
    private IRulerDao _IRulerDao;



    public RulerVO doInsertruler(RulerVO _RulerVO) {

        if(null == _RulerVO){
            logger.error("null == _RulerVO");
            return null;
        }

        _IRulerDao.doInsertruler(_RulerVO);

        return _RulerVO;
    }

    public List<RulerVO> findRulerByParameter(RulerVO _RulerVO) {

        if(null == _RulerVO){
            return null;
        }
        List<RulerVO> list_ApacheAdgentMetaData = _IRulerDao.findRulerByParameter(_RulerVO);

        return list_ApacheAdgentMetaData;
    }
}
