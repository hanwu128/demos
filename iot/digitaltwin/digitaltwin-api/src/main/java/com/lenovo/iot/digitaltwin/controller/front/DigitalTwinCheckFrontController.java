package com.lenovo.iot.digitaltwin.controller.front;

import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.digitaltwin.model.*;
import com.lenovo.iot.digitaltwin.service.DigitalTwinInstanceFrontService;
import com.lenovo.iot.digitaltwin.service.DigitalTwinTemplateFrontService;
import com.lenovo.iot.digitaltwin.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Desc: DT实例检查
 * Name: com.lenovo.iot.digitaltwin.controller.front.DigitalTwinCheckFrontController
 * Author: hanwu
 * Date: 2018/9/21 14:30
 **/
@Controller
@RequestMapping("/dt/front/check")
public class DigitalTwinCheckFrontController {
    private static final Logger logger = LoggerFactory.getLogger(DigitalTwinCheckFrontController.class);

    @Autowired
    private DigitalTwinInstanceFrontService digitalTwinInstanceFrontService;

    @Autowired
    private DigitalTwinTemplateFrontService digitalTwinTemplateFrontService;

    /**
     * 检查物模板名称是否相同
     *
     * @param req
     * @param resp
     * @param params
     * @return
     */
    @PostMapping("/template.ft")
    @ResponseBody
    public Object template(HttpServletRequest req, HttpServletResponse resp,
                           @RequestBody JSONObject params) {
        try {
            String name = params.getString("name");
            if (StringUtils.isEmpty(name)) {
                logger.error("check dt template name parameter is invalid: {}", params);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }
            DigitalTwinTemplate dtTemplate = digitalTwinTemplateFrontService.getDigitalTwinTemplateByName(name);
            if (dtTemplate != null) {
                logger.error("check dt template exists with name: {}", params);
                return JsonResp.getErrorResp(HttpServletResponse.SC_CONFLICT, "dt template exists with name: " + name);
            }
            return JsonResp.getSuccessResp();
        } catch (Exception e) {
            logger.error("check template tpl name error: {}, {}", params, e.getMessage());
            e.printStackTrace();
        }
        return JsonResp.getErrorResp();
    }

    /**
     * 检查物实例名称是否相同
     *
     * @param req
     * @param resp
     * @param params
     * @return
     */
    @PostMapping("/instance.ft")
    @ResponseBody
    public Object instance(HttpServletRequest req, HttpServletResponse resp,
                           @RequestBody JSONObject params) {
        try {
            String name = params.getString("name");
            if (StringUtils.isEmpty(name)) {
                logger.error("check dt instance name parameter is invalid: {}", params);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }
            DigitalTwinInstance dtInstance = digitalTwinInstanceFrontService.getDigitalTwinInstanceByName(name);
            if (dtInstance != null) {
                logger.error("check dt instance exists with name: {}", params);
                return JsonResp.getErrorResp(HttpServletResponse.SC_CONFLICT, "dt instance exists with name: " + name);
            }
            return JsonResp.getSuccessResp();
        } catch (Exception e) {
            logger.error("check dt instance name error: {}, {}", params, e.getMessage());
            e.printStackTrace();
        }
        return JsonResp.getErrorResp();
    }
}
