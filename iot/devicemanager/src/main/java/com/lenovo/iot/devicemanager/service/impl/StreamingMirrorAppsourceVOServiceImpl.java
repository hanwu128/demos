package com.lenovo.iot.devicemanager.service.impl;

import com.lenovo.iot.devicemanager.dao.IApacheAdgentMetaDataDao;
import com.lenovo.iot.devicemanager.dao.IStreamingMirrorAppsourceVODao;
import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;
import com.lenovo.iot.devicemanager.model.bo.StreamingMirrorAppsourceVO;
import com.lenovo.iot.devicemanager.service.IStreamingMirrorAppsourceVOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by root on 2017/8/24.
 */
@Service
public class StreamingMirrorAppsourceVOServiceImpl implements IStreamingMirrorAppsourceVOService {

    private static Logger logger = LoggerFactory.getLogger(StreamingMirrorAppsourceVOServiceImpl.class);    //日志记录

    @Autowired
    private IStreamingMirrorAppsourceVODao _IStreamingMirrorAppsourceVODao;

    public StreamingMirrorAppsourceVO doInsertStreamingMirrorAppsourceVO(StreamingMirrorAppsourceVO _StreamingMirrorAppsourceVO) {
        if(null == _StreamingMirrorAppsourceVO){
            return null;
        }
        _IStreamingMirrorAppsourceVODao.doInsertStreamingMirrorAppsourceVO(_StreamingMirrorAppsourceVO);

        return _StreamingMirrorAppsourceVO;
    }

    public List<StreamingMirrorAppsourceVO> findStreamingMirrorAppsourceVOByParameter(StreamingMirrorAppsourceVO _StreamingMirrorAppsourceVO) {

        if(null == _StreamingMirrorAppsourceVO){
            return null;
        }

        List<StreamingMirrorAppsourceVO> listStreamingMirrorAppsourceVO  =  _IStreamingMirrorAppsourceVODao.findStreamingMirrorAppsourceVOByParameter(_StreamingMirrorAppsourceVO);

        return listStreamingMirrorAppsourceVO;
    }


}
