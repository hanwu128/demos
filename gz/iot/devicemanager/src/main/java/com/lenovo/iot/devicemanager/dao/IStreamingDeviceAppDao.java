package com.lenovo.iot.devicemanager.dao;

import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;
import com.lenovo.iot.devicemanager.model.bo.StreamingCallbackFlight;
import com.lenovo.iot.devicemanager.model.bo.StreamingDeviceAppVO;
import com.lenovo.iot.devicemanager.model.bo.TaskVO;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by root on 2017/7/19.
 */
@Repository
public interface IStreamingDeviceAppDao {

    public void doInsertStreamingDeviceApp(StreamingDeviceAppVO _StreamingDeviceAppVO);

    public List<StreamingDeviceAppVO> findStreamingDeviceAppVOByParameter(StreamingDeviceAppVO _StreamingDeviceAppVO);

    public void doUpdateStreamingDeviceAppVO(StreamingDeviceAppVO _StreamingDeviceAppVO);

//    public List<ApacheAdgentMetaData> findAllStreamingDeviceAppVO();

    public List<StreamingCallbackFlight> findStreamingDeviceAppVOBaseById(StreamingCallbackFlight _StreamingCallbackFlight);
    
    public List<StreamingCallbackFlight> findStreamingDeviceAppVOBaseByParameters(StreamingCallbackFlight _StreamingCallbackFlight);
    public Integer findStreamingDeviceAppVOBaseCountByParameters(StreamingCallbackFlight _StreamingCallbackFlight);
   
    public void doUpdateStreamingDeviceAppByAppnameDeviceid(TaskVO taskVO);

    public List<StreamingCallbackFlight> getDeviceStreamings(StreamingCallbackFlight streamingCallbackFlight);
}
