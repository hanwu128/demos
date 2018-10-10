package com.lenovo.iot.devicemanager.service;

import com.lenovo.iot.devicemanager.model.bo.StreamingMirrorRulerVO;

import java.util.List;

/**
 * Created by root on 2017/8/29.
 */
public interface IStreamingMirrorRulerService {


    public StreamingMirrorRulerVO doInsertStreamingMirrorRuler(StreamingMirrorRulerVO _StreamingMirrorRulerVO);

    public List<StreamingMirrorRulerVO> findStreamingMirrorRulerVOByParameter(StreamingMirrorRulerVO _StreamingMirrorRulerVO);

}
