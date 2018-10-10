package com.lenovo.iot.devicemanager.service;

import com.lenovo.iot.devicemanager.model.bo.StreamingCallbackFlight;
import com.lenovo.iot.devicemanager.model.bo.StreamingDeviceAppVO;
import com.lenovo.iot.devicemanager.model.bo.TaskVO;

import java.util.List;

public interface IStreamingDeviceAppService {

    public StreamingDeviceAppVO doInsertStreamingDeviceApp(StreamingDeviceAppVO _StreamingDeviceAppVO);

    public List<StreamingDeviceAppVO> findStreamingDeviceAppVOByParameter(StreamingDeviceAppVO _StreamingDeviceAppVO);

    public void doUpdateStreamingDeviceAppVO(StreamingDeviceAppVO _StreamingDeviceAppVO);
    
    public void deleteStreamingDeviceApp(long company_id, long id);

    public List<StreamingCallbackFlight> findStreamingDeviceAppVOBaseById(StreamingCallbackFlight _StreamingCallbackFlight);

//    public List<StreamingCallbackFlight> findAllStreamingDeviceAppVOBaseBy(StreamingCallbackFlight _StreamingCallbackFlight);

    public List<StreamingCallbackFlight> findStreamingDeviceAppVOBaseByParameters(StreamingCallbackFlight _StreamingCallbackFlight);
    public Integer findStreamingDeviceAppVOBaseCountByParameters(StreamingCallbackFlight _StreamingCallbackFlight);

    public void doUpdateStreamingDeviceAppByAppnameDeviceid(TaskVO taskVO);

    public List<StreamingCallbackFlight> getDeviceStreamings(StreamingCallbackFlight streamingCallbackFlight);
    }
