package com.lenovo.iot.devicemanager.service.impl;

import com.lenovo.iot.devicemanager.dao.IApacheAdgentMetaDataDao;
import com.lenovo.iot.devicemanager.dao.IStreamingDao;
import com.lenovo.iot.devicemanager.model.bo.StreamingVO;
import com.lenovo.iot.devicemanager.service.IStreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by root on 2017/8/22.
 */

@Service
public class StreamingServiceImpl implements IStreamingService {

    @Autowired
    private IStreamingDao _IStreamingDao;

    public StreamingVO doInsertStreaming(StreamingVO _StreamingVO) {
        if(null ==_StreamingVO){
            return null;
        }
        _IStreamingDao.doInsertStreaming(_StreamingVO);
        return _StreamingVO;
    }

    public List<StreamingVO> findAllStreaming() {
        List<StreamingVO> list4StreamingVO = _IStreamingDao.findAllStreaming();
        return list4StreamingVO;
    }

    public List<StreamingVO> findStreamingByParameter(StreamingVO _StreamingVO) {

        if(null ==_StreamingVO){
            return null;
        }

        List<StreamingVO> list4StreamingVO = _IStreamingDao.findStreamingByParameter(_StreamingVO);

        return list4StreamingVO;
    }
}
