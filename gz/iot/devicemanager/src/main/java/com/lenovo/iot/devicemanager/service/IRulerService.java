package com.lenovo.iot.devicemanager.service;

import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;
import com.lenovo.iot.devicemanager.model.bo.RulerVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by root on 2017/8/29.
 */
public interface IRulerService {

    public  RulerVO  doInsertruler(RulerVO _RulerVO);

    public List<RulerVO> findRulerByParameter(RulerVO _RulerVO);

}
