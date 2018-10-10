package com.lenovo.iot.devicemanager.service;

import com.lenovo.iot.devicemanager.model.bo.StreamingVO;
import com.lenovo.iot.devicemanager.model.bo.TaskVO;

import java.util.List;

/**
 * Created by root on 2017/8/21.
 */
public interface IStreamingService {

    //doInsertTasktoNodeBo
    public StreamingVO doInsertStreaming(StreamingVO _StreamingVO);

    //doUpdateTask
    //public void doUpdateTask(TaskVO _TaskVO);

    //findAllTask
    public List<StreamingVO> findAllStreaming();

    //findStreamingByParameter

    public List<StreamingVO> findStreamingByParameter(StreamingVO _StreamingVO);


}
