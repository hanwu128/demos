package com.lenovo.iot.devicemanager.dao;

import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;
import com.lenovo.iot.devicemanager.model.bo.StreamingMirrorAppsourceVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by root on 2017/7/19.
 */
@Repository
public interface IStreamingMirrorAppsourceVODao {

    public void doInsertStreamingMirrorAppsourceVO(StreamingMirrorAppsourceVO _StreamingMirrorAppsourceVO);

    public List<StreamingMirrorAppsourceVO> findStreamingMirrorAppsourceVOByParameter(StreamingMirrorAppsourceVO _StreamingMirrorAppsourceVO);

    // public List<StreamingMirrorAppsourceVO> findAllStreamingMirrorAppsourceVO();
   // public void doUpdateStreamingMirrorAppsourceVO(StreamingMirrorAppsourceVO _StreamingMirrorAppsourceVO);

}
