package com.lenovo.iot.devicemanager.dao;

import com.lenovo.iot.devicemanager.model.bo.StreamingVO;
import com.lenovo.iot.devicemanager.model.bo.TaskVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by root on 2017/7/19.
 */
@Repository
public interface IStreamingDao {

    //doInsertTasktoNodeBo
    public void doInsertStreaming(StreamingVO _StreamingVO);

    //doUpdateTask
    //public void doUpdateTask(TaskVO _TaskVO);

    //findAllTask
    public List<StreamingVO> findAllStreaming();

    //findStreamingByParameter

    public List<StreamingVO> findStreamingByParameter(StreamingVO _StreamingVO);

}
