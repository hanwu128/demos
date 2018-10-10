package com.lenovo.iot.devicemanager.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import scala.collection.immutable.Range;
import scala.collection.parallel.immutable.ParRange;

/**
 * Created by root on 2018/5/10.
 */
@Service
public interface DigitalTwinMirrorService {

    //DigitalTwinMirrorDao

    public JSONObject update_digitaltwin(Double value,String bussinessid,String current_metric,String current_tags,Long _Long_Timestamp);

    //    public Integer update_digitaltwindaddyById(@Param("digitaltwinname") String digitaltwinname, @Param("id") Long id);
    public JSONObject update_digitaltwindaddyById(Long id,String digitaltwinname);

    //delete_All_digitaltwindaddy_digitaltwin
    public JSONObject delete_All_digitaltwindaddy_digitaltwin(Long id);
    //修改期望值 by id
    public JSONObject update_digitaltwin_expectedvalue(Double expectedvalue,Long id_digitaltwin);

    public JSONObject select_digitaltwin_By_id(Long id);

}
