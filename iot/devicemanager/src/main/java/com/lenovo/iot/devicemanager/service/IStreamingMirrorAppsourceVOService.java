package com.lenovo.iot.devicemanager.service;

import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;
import com.lenovo.iot.devicemanager.model.bo.StreamingMirrorAppsourceVO;

import java.util.List;

/**
 * Created by root on 2017/8/24.
 */
public interface IStreamingMirrorAppsourceVOService {

    public StreamingMirrorAppsourceVO doInsertStreamingMirrorAppsourceVO(StreamingMirrorAppsourceVO _StreamingMirrorAppsourceVO);

    public List<StreamingMirrorAppsourceVO> findStreamingMirrorAppsourceVOByParameter(StreamingMirrorAppsourceVO _StreamingMirrorAppsourceVO);
}
