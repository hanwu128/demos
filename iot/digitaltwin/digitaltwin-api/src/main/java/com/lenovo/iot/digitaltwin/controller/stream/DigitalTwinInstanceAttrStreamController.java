package com.lenovo.iot.digitaltwin.controller.stream;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.digitaltwin.model.DigitalTwinInstanceAttr;
import com.lenovo.iot.digitaltwin.service.DigitalTwinInstanceStreamService;
import com.lenovo.iot.digitaltwin.util.JsonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: DT实例属性接口
 * @Name: com.lenovo.iot.digitaltwin.controller.stream.DigitalTwinInstanceAttrStreamController
 * @Author: chench9@lenovo.com
 * @Date: 2018/7/9
 */
@RestController
@RequestMapping("/dt/stream")
public class DigitalTwinInstanceAttrStreamController {
    private static final Logger logger = LoggerFactory.getLogger(DigitalTwinInstanceAttrStreamController.class);

    @Autowired
    private DigitalTwinInstanceStreamService digitalTwinInstanceStreamService;

    /**
     * 批量更新DT实例属性
     * @param req
     * @param resp
     * @param attrArr
     * @return
     */
    @PostMapping("/instance/attributes")
    public Object updateAttr(HttpServletRequest req, HttpServletResponse resp,
                             @RequestBody JSONArray attrArr) {
        if(attrArr == null) {
            logger.error("update dt instance attribute body is empty");
            return JsonResp.httpCode(resp, HttpServletResponse.SC_BAD_REQUEST).errorResp(JsonResp.MSG_PARAM_ERROR);
        }

        try {
            int size = attrArr.size();
            List<DigitalTwinInstanceAttr> attrList = new ArrayList<DigitalTwinInstanceAttr>(size);
            for(int i = 0; i < size; i++) {
                JSONObject attrJson = attrArr.getJSONObject(i);
                DigitalTwinInstanceAttr attr = new DigitalTwinInstanceAttr();
                attr.setId(attrJson.getLong("id"));
                attr.setMetric(attrJson.getString("metric"));
                attr.setDeviceid(attrJson.getString("device_id"));
                attr.setValue(attrJson.getString("value"));
                attrList.add(attr);
            }

            // 批量更新DT实例属性
            digitalTwinInstanceStreamService.updateDigitalTwinInstanceAttrList(attrList);
            return JsonResp.httpCode(resp, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            logger.error("update dt instance attribute error: {}, {}", attrArr, e.getMessage());
            e.printStackTrace();
        }

        return JsonResp.httpCode(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR).errorResp(JsonResp.MSG_ERROR);
    }
}
