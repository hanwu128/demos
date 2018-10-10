package com.lenovo.iot.devicemanager.controller.front;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.devicemanager.model.DigitalTwin;
import com.lenovo.iot.devicemanager.model.DigitalTwinAttrResult;
import com.lenovo.iot.devicemanager.model.JsonResp;
import com.lenovo.iot.devicemanager.model.MqttMessage;
import com.lenovo.iot.devicemanager.mqtt.EmqttInstance;
import com.lenovo.iot.devicemanager.service.impl.DigitalTwinMirrorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import scala.xml.Null;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * fixme:反控，业务未明确 待开发
 *
 * post
 *
 *http://localhost:8080/CountercontrolController/countercontrolbyid.do
  {
  	"id": 1,
  	"attr": [
  {
  		"id": 91,
  		"value": 11
         },
     {
  		"id": 90,
  		"value": 12
     }

  ]
 }
 *
 * /set.do
 * 设置Digital Twin值，将该值更新到设备。
 * 1)	首先更新Digital Twin表的“期望值”
 * 2)	然后通过MQTT发送消息：
 * $LEAP/control/{deviceId}/digitaltwin/set
 * {
 *     "metric":"xxx.xxxx.xxxxx",
 * 	"desired": {
 * 		"sensor1":29,
 * 		"sensor2":22.12
 *        }
 * }
 */
@Controller
@RequestMapping("/CountercontrolController")
public class CountercontrolController {

    private static final Logger logger = LoggerFactory.getLogger(CountercontrolController.class);

    @Autowired
    private DigitalTwinMirrorServiceImpl _DigitalTwinMirrorServiceImpl;

    @Autowired
    private EmqttInstance emqttInstance;

    @PostMapping("/countercontrolbyid.do")
    @ResponseBody
    public Object countercontrolbyid(HttpServletRequest req,
                                     HttpServletResponse resp,
                                     @RequestBody JSONObject _JSONObjectParameter){

        if(null == _JSONObjectParameter){
            return JsonResp.getJsonRespError("_JSONObjectParameter is null");
        }

        JSONArray _JSONArray_tbl_digitaltwin = _JSONObjectParameter.getJSONArray("attr");

        if(null == _JSONArray_tbl_digitaltwin || _JSONArray_tbl_digitaltwin.size()<=0){
            return JsonResp.getJsonRespError("null == _JSONArray_tbl_digitaltwin || _JSONArray_tbl_digitaltwin.size()<=0");
        }


        JSONArray _JSONArrayResult = new JSONArray();

        for(int i = 0 ; i < _JSONArray_tbl_digitaltwin.size();i++){

            JSONObject currentJSONObject = _JSONArray_tbl_digitaltwin.getJSONObject(i);

            Long current_digitaltwin_Id = currentJSONObject.getLong("id");

            if(null == current_digitaltwin_Id || current_digitaltwin_Id <=0 ){
                logger.error("error:null == current_digitaltwin_Id || current_digitaltwin_Id <=0 ");
                continue;
            }

            Double expectedvalue = currentJSONObject.getDouble("value");

            if(null == expectedvalue){
                logger.error("error:null == expectedvalue");
                continue;
            }

            //update digitaltwin

            JSONObject _JSONObjectResult = _DigitalTwinMirrorServiceImpl.update_digitaltwin_expectedvalue(expectedvalue,current_digitaltwin_Id);

            _JSONObjectResult.put("id",current_digitaltwin_Id);

            //_JSONArrayResult.add(_JSONObjectResult);

            JSONObject _JSONObject__DigitalTwin= _DigitalTwinMirrorServiceImpl.select_digitaltwin_By_id(current_digitaltwin_Id);

            if(null == _JSONObject__DigitalTwin){
                logger.error("null == _JSONObject__DigitalTwinAttr");
                continue;
            }

            DigitalTwin _DigitalTwin = _JSONObject__DigitalTwin.getObject("result",DigitalTwin.class);//_JSONObject__DigitalTwinAttr.getObject("result");

            if(null == _DigitalTwin){
                logger.error("null == _DigitalTwin");
                continue;
            }

            String currentTagKVJSONStr = _DigitalTwin.getTagkv();

            if(null == currentTagKVJSONStr || currentTagKVJSONStr.trim().length()<=0){
                logger.error("null == currentTagKVJSONStr || currentTagKVJSONStr.trim().length()<=0");
                continue;
            }


            JSONObject _JSONObject__tagkv = (JSONObject) JSONObject.parse(currentTagKVJSONStr);

            if(null == _JSONObject__tagkv){
                logger.error("null == _JSONObject__tagkv");
                continue;
            }

            String deviceIdStrFixed = _JSONObject__tagkv.getString("deviceId");//_JSONObject__tagkv.getString("deviceId");

            if(null == deviceIdStrFixed || deviceIdStrFixed.trim().length()<=0){
                logger.error("null == deviceIdStrFixed || deviceIdStrFixed.trim().length()<=0");
                continue;
            }

            String lableStrFixed = _JSONObject__tagkv.getString("lable");;//_JSONObject__tagkv.getString("lable");
            if(null == lableStrFixed || lableStrFixed.trim().length()<=0){
                logger.error("null == lableStrFixed || lableStrFixed.trim().length()<=0");
                continue;
            }

            String metric = _DigitalTwin.getMetric();

            if(null == metric){
                logger.error("null == metric");
                continue;
            }

            String topic = "$LEAP/control/{"+deviceIdStrFixed+"}/digitaltwin/set";

            JSONObject JSONObjectMessage = new JSONObject();

            JSONObjectMessage.put("metric",metric);

            JSONObject JSONObjectDesired = new JSONObject();
            JSONObjectDesired.put(lableStrFixed,expectedvalue);

            JSONObjectMessage.put("desired",JSONObjectDesired);

            //send message to mqtt
            MqttMessage map_msg = new MqttMessage();
            map_msg.setTopic(topic);
            map_msg.setMessage(JSONObjectMessage.toJSONString());
            map_msg.setQos(0);
            map_msg.setDevice_id(deviceIdStrFixed);
//            map_msg.setApp_name(_App.getAppname());
//            map_msg.setApp_type(_App.getApptype());

            Boolean flag_SendMQTT = false;
            try {
                flag_SendMQTT = emqttInstance.pulishMessage(map_msg);
            }catch (Exception e){
                e.printStackTrace();
            }
            _JSONObject__DigitalTwin.put("sendMQTTFlag",flag_SendMQTT);

            logger.info("_JSONObject__DigitalTwin--->>>>"+_JSONObject__DigitalTwin.toJSONString());

            _JSONArrayResult.add(_JSONObject__DigitalTwin);

        }

        return JsonResp.getJsonRespSuccess(_JSONArrayResult);
    }

}
