package com.lenovo.iot.devicemanager.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.devicemanager.dao.DigitalTwinMirrorDao;
import com.lenovo.iot.devicemanager.model.DigitalTwin;
import com.lenovo.iot.devicemanager.model.DigitalTwinAttr;
import com.lenovo.iot.devicemanager.service.DigitalTwinMirrorService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by root on 2018/5/10.
 */
@Service
public class DigitalTwinMirrorServiceImpl implements DigitalTwinMirrorService {

    @Autowired
    private DigitalTwinMirrorDao _DigitalTwinMirrorDao;

    //public DigitalTwinAttr select_digitaltwin_By_id(@Param("id") Long loginName);

    public JSONObject select_digitaltwin_By_id(Long id) {

        JSONObject _JSONObject = new JSONObject();

        DigitalTwin _DigitalTwin = _DigitalTwinMirrorDao.select_digitaltwin_By_id(id);

        _JSONObject.put("result",_DigitalTwin);

        return _JSONObject;

    }


    public JSONObject update_digitaltwin(Double value,
                                         String bussinessid,
                                         String current_metric,
                                         String current_tags,
                                         Long _Long_Timestamp
                                         ) {

        JSONObject _JSONObject = new JSONObject();

        Integer flag = _DigitalTwinMirrorDao.update_digitaltwin(
                                                                value,bussinessid,
                                                                current_metric,
                                                                current_tags,
                                                                _Long_Timestamp
                                                                );

        _JSONObject.put("actionFlag",true);

        _JSONObject.put("actionResult",flag);

        return _JSONObject;
    }


    public JSONObject update_digitaltwin_expectedvalue(Double expectedvalue,Long id_digitaltwin) {

        JSONObject _JSONObject = new JSONObject();

        Integer flag = _DigitalTwinMirrorDao.update_digitaltwin_expectedvalue(expectedvalue,id_digitaltwin);

        _JSONObject.put("actionFlag",true);

        _JSONObject.put("actionResult",flag);

        return _JSONObject;
    }


    public JSONObject update_digitaltwindaddyById(Long id, String digitaltwinname) {

        JSONObject _JSONObject = new JSONObject();

        Integer flag = _DigitalTwinMirrorDao.update_digitaltwindaddyById(digitaltwinname,id);

        _JSONObject.put("actionFlag",true);

        _JSONObject.put("actionResult",flag);

        return _JSONObject;
    }

    public JSONObject delete_All_digitaltwindaddy_digitaltwin(Long id) {

        JSONObject _JSONObject = new JSONObject();

        Integer flagDelete_digitaltwindaddy = _DigitalTwinMirrorDao.delete_digitaltwindaddy_ById(id);

        Integer flagDelete_digitaltwin = _DigitalTwinMirrorDao.delete_digitaltwin_BydaddyId(id);

        _JSONObject.put("actionFlag",true);

        JSONObject _JSONObjectPackage =  new JSONObject();

        _JSONObjectPackage.put("flagDelete_digitaltwindaddy",flagDelete_digitaltwindaddy);
        _JSONObjectPackage.put("flagDelete_digitaltwin",flagDelete_digitaltwin);

        _JSONObject.put("actionResult",_JSONObjectPackage);

        return _JSONObject;
    }


}
