package com.lenovo.iot.devicemanager.service.impl;

import com.lenovo.iot.devicemanager.dao.IApacheAdgentMetaDataDao;
import com.lenovo.iot.devicemanager.dao.IStreamingMirrorRulerDao;
import com.lenovo.iot.devicemanager.model.bo.StreamingMirrorAppsourceVO;
import com.lenovo.iot.devicemanager.model.bo.StreamingMirrorRulerVO;
import com.lenovo.iot.devicemanager.service.IStreamingMirrorRulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by root on 2017/8/29.
 */
@Service
public class StreamingMirrorRulerServiceImpl implements IStreamingMirrorRulerService {

    private static Logger logger = LoggerFactory.getLogger(StreamingMirrorRulerServiceImpl.class);    //日志记录

    @Autowired
    private IStreamingMirrorRulerDao _IStreamingMirrorRulerDao;

    public StreamingMirrorRulerVO doInsertStreamingMirrorRuler(StreamingMirrorRulerVO _StreamingMirrorRulerVO) {

        if(null == _StreamingMirrorRulerVO){
            logger.error("null == _StreamingMirrorRulerVO");
            return null;
        }

        _IStreamingMirrorRulerDao.doInsertStreamingMirrorRuler(_StreamingMirrorRulerVO);

        return _StreamingMirrorRulerVO;
    }

    public List<StreamingMirrorRulerVO> findStreamingMirrorRulerVOByParameter(StreamingMirrorRulerVO _StreamingMirrorRulerVO) {

        List<StreamingMirrorRulerVO> list4StreamingMirrorRulerVO = _IStreamingMirrorRulerDao.findStreamingMirrorRulerVOByParameter(_StreamingMirrorRulerVO);

        return list4StreamingMirrorRulerVO;
    }










}
