package com.lenovo.iot.devicemanager.controller.stream;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.devicemanager.model.JsonResp;
import com.lenovo.iot.devicemanager.service.impl.DigitalTwinMirrorServiceImpl;
import com.lenovo.iot.devicemanager.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by root on 2018/5/10.
 *
 *
 访问url：
 http://localhost:8080/DigitalTwinStreamSetHandlerController/update_value2018.do
 参数
 [{
 "timestamp":1523848208232,
 "profileVersion": 1,
 "value":22.222,
 "metric":"temperature",
 "tags":{
 "deviceId":"dvid001",
 "tagk1":"tagv1"}}

 ]
返回
 {
 "code": 200,
 "data": {
 "actionFlag": true,
 "actionResult": 1
 },
 "message": "success"
 }
 *
 *
 *
 *
 *
 *
 */
@RequestMapping("/DigitalTwinStreamSetHandlerController")
@Controller
public class DigitalTwinStreamSetHandlerController {

    private static final Logger logger = LoggerFactory.getLogger(DigitalTwinStreamSetHandlerController.class);

    @Autowired
    private DigitalTwinMirrorServiceImpl _DigitalTwinMirrorServiceImpl;



    /**
     *  *
     更新Digital Twin属性值（leap stream调用）
     /update_value.do
     前端不能调用。
     Digital Twin属性值是不能直接更新的，必须通过Leap Stream更新。
     此接口供leap stream调用，Web server接收Leap Stream转发的流数据，通过此接口自动更新Digital Twin状态。
     Metric + Tags唯一确定一个属性值。
     Leap stream转发的数据格式：
     [{
     "timestamp":1523848208232,
     "profileVersion": 1,
     "value":22.2,
     "metric":"metric name",
     "tags":{
     "deviceId":"xxxxx",
     "tagk1":"tagv1",
     …
     }
     },
     …]

     * @param _DigitalTwinMirrorJSONArray
     * @param _HttpServletRequest
     * @param _HttpServletResponse
     * @return
     */
    @PostMapping(value = "/update_value_dt.url", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    @CrossOrigin(origins="*")
    public Object updateDigitalTwinObject(@RequestBody String _text,
                                              HttpServletRequest _HttpServletRequest,
                                              HttpServletResponse _HttpServletResponse) {

    	JSONArray _DigitalTwinMirrorJSONArray = JSONArray.parseArray(_text);
        JSONObject _Restult_JSONObject = new JSONObject();

        if(null == _DigitalTwinMirrorJSONArray || _DigitalTwinMirrorJSONArray.size()<=0){
            String logMessage = "error: null or blank is (@RequestBody String _DigitalTwinMirrorJSONArrayStr)";
            logger. error(logMessage);
            _Restult_JSONObject.put("ActionFlag",false);
            _Restult_JSONObject.put("ActionDesc",logMessage);
            return _Restult_JSONObject;
        }

       for(int i = 0 ; i < _DigitalTwinMirrorJSONArray.size(); i++){

            JSONObject current_JSONObject = _DigitalTwinMirrorJSONArray.getJSONObject(i);

            System.out.println("current_JSONObject --- >>>>>>"+current_JSONObject.toJSONString());

            Double current_value = Double.valueOf(current_JSONObject.getString("value"));

            String current_metric = current_JSONObject.getString("metric");

            String current_tags = current_JSONObject.getString("tags");

            String current_bussinessid=current_metric+current_tags;

            Long _Long_Timestamp = current_JSONObject.getLong("timestamp");

           if(null == _Long_Timestamp || _Long_Timestamp <= 0){

               logger.error("!!!error:parameter timestamp is null or <=0"+"we will give currentServerTime to timestamp ");

               Date currentDate = new Date();

               _Long_Timestamp = currentDate.getTime();

           }


            _Restult_JSONObject = _DigitalTwinMirrorServiceImpl.update_digitaltwin(current_value,current_bussinessid,current_metric,current_tags,_Long_Timestamp);

        }

        return JsonResp.getJsonRespSuccess(_Restult_JSONObject);
    }






    }
