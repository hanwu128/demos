package com.lenovo.iot.digitaltwin.controller.open;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.digitaltwin.model.DigitalTwinInstance;
import com.lenovo.iot.digitaltwin.model.DigitalTwinInstanceAttr;
import com.lenovo.iot.digitaltwin.service.DigitalTwinInstanceOpenService;
import com.lenovo.iot.digitaltwin.service.MessagesService;
import com.lenovo.iot.digitaltwin.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Desc: 访问DT实例属性列表
 * Name: com.lenovo.iot.digitaltwin.controller.open.DigitalTwinInstanceAttrOpenController
 * Author: hanwu
 * Date: 2018/7/3 11:00
 **/
@RestController
@RequestMapping("/dt/open/v1/instance/attribute")
public class DigitalTwinInstanceAttrOpenController {
    private static final Logger logger = LoggerFactory.getLogger(DigitalTwinInstanceAttrOpenController.class);

    @Autowired
    private MessagesService messagesService;

    @Autowired
    private DigitalTwinInstanceOpenService digitalTwinInstanceOpenService;

    /**
     * 查询State、Description、Stream类型数据
     *
     * @param req
     * @param resp
     * @param id
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/{id}")
    public Object state(HttpServletRequest req, HttpServletResponse resp,
                        @PathVariable("id") Long id,
                        @RequestParam(value = "start", required = false) Object start,
                        @RequestParam(value = "end", required = false) Object end,
                        @RequestParam(value = "downsample", required = false) String downsample,
                        @RequestParam(value = "aggregator", required = false) String aggregator) {
        try {
            if (id == null || id <= 0) {
                logger.error("detail parameter dt open instance attribute id is invalid: {}", id);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, messagesService.getMessage("parameter.error.name", new Object[]{"id"}));
            }
            if (CommonUtil.isSpecialCharOutRung(String.valueOf(start)) || CommonUtil.isContainChinese(String.valueOf(start))) {
                logger.error("detail parameter dt open instance attribute start is invalid: {},{},{},{},{}",
                        id, start, end, downsample, aggregator);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, messagesService.getMessage("parameter.error.name", new Object[]{"start"}));
            }
            if (CommonUtil.isSpecialCharOutRung(String.valueOf(end)) || CommonUtil.isContainChinese(String.valueOf(end))) {
                logger.error("detail parameter dt open instance attribute end is invalid: {},{},{},{},{}",
                        id, start, end, downsample, aggregator);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, messagesService.getMessage("parameter.error.name", new Object[]{"end"}));
            }
            if (CommonUtil.isSpecialCharOutRung(downsample) || CommonUtil.isContainChinese(downsample)) {
                logger.error("detail parameter dt open instance attribute downsample is invalid: {},{},{},{},{}",
                        id, start, end, downsample, aggregator);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, messagesService.getMessage("parameter.error.name", new Object[]{"downsample"}));
            }
            if (CommonUtil.isSpecialChar(aggregator) || CommonUtil.isContainChinese(aggregator)) {
                logger.error("detail parameter dt open instance attribute aggregator is invalid: {},{},{},{},{}",
                        id, start, end, downsample, aggregator);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, messagesService.getMessage("parameter.error.name", new Object[]{"aggregator"}));
            }
            DigitalTwinInstanceAttr digitalTwinInstanceAttr = digitalTwinInstanceOpenService.getAttrById(id);
            if (digitalTwinInstanceAttr == null) {
                logger.error("detail dt open instance attribute not found: {}", id);
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return JsonResp.getErrorResp(HttpServletResponse.SC_NOT_FOUND, messagesService.getMessage("not.found", new Object[]{id}));
            }
            if (!StringUtils.isEmpty(digitalTwinInstanceAttr.getAttrtype()) &&
                    digitalTwinInstanceAttr.getAttrtype().equalsIgnoreCase(AttrTypeEnum.TYPE_STREAM.toString())) {
                if (start == null || "".equals(start)) {
                    // 查询Stream类型的物实例属性时必须带有start参数
                    logger.error("detail parameter dt open instance attribute start is invalid: {},{},{},{},{}",
                            id, start, end, downsample, aggregator);
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, messagesService.getMessage("parameter.empty.name", new Object[]{"start"}));
                }
                if (end != null && !"".equals(end)) {
                    //start与end时间格式保持一致，当start为时间戳end为时间戳，start为字符串end为字符串
                    if ((CommonUtil.isNumeric(start.toString()) && !CommonUtil.isNumeric(end.toString())) ||
                            (!CommonUtil.isNumeric(start.toString()) && CommonUtil.isNumeric(end.toString()))) {
                        logger.error("detail parameter dt open instance attribute end is invalid: {},{},{},{},{}",
                                id, start, end, downsample, aggregator);
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, messagesService.getMessage("parameter.error"));
                    }
                }

                if (!StringUtils.isEmpty(digitalTwinInstanceAttr.getMetric())) {
                    List<String> idsList = new ArrayList<String>();
                    idsList.add(String.valueOf(digitalTwinInstanceAttr.getInstance()));
                    DigitalTwinInstance digitalTwinInstance = digitalTwinInstanceOpenService.getDigitalTwinInstanceDetail(idsList).get(0);
                    if (digitalTwinInstance == null) {
                        logger.error("detail dt open instance not found: {}", id);
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        return JsonResp.getErrorResp(HttpServletResponse.SC_NOT_FOUND, messagesService.getMessage("not.found", new Object[]{digitalTwinInstanceAttr.getInstance()}));
                    }
                    //获取OpenTsDb中的数据
                    Object results = TsDbUtil.postQueryFromTsDb(id, digitalTwinInstance.getName(), digitalTwinInstanceAttr.getDeviceid(), digitalTwinInstanceAttr.getMetric(), start, end, downsample, aggregator);
                    digitalTwinInstanceAttr.setValue(results);
                } else {
                    logger.info("detail dt open instance attr metric attribute is null:{}", id);
                    digitalTwinInstanceAttr.setValue(new JSONArray());
                }
            } else {
                if (!StringUtils.isEmpty(digitalTwinInstanceAttr.getDatatype())) {
                    if (digitalTwinInstanceAttr.getDatatype().equalsIgnoreCase(DataTypeEnum.TYPE_BOOLEAN.toString())) {
                        if (CommonUtil.isBoolean(digitalTwinInstanceAttr.getValue())) {
                            digitalTwinInstanceAttr.setValue(CommonUtil.str2Boolean(digitalTwinInstanceAttr.getValue().toString()));
                        } else {
                            digitalTwinInstanceAttr.setValue(false);
                        }
                    }
                    if (digitalTwinInstanceAttr.getDatatype().equalsIgnoreCase(DataTypeEnum.TYPE_NUMBER.toString())) {
                        if (CommonUtil.isNumeric(CommonUtil.obj2String(digitalTwinInstanceAttr.getValue()))) {
                            digitalTwinInstanceAttr.setValue(CommonUtil.str2Double(digitalTwinInstanceAttr.getValue().toString()));
                        } else {
                            digitalTwinInstanceAttr.setValue(0);
                        }
                    }
                    if (digitalTwinInstanceAttr.getDatatype().equalsIgnoreCase(DataTypeEnum.TYPE_OBJECT.toString())) {
                        if (CommonUtil.isObject(digitalTwinInstanceAttr.getValue())) {
                            digitalTwinInstanceAttr.setValue(CommonUtil.str2JSONObject(digitalTwinInstanceAttr.getValue().toString()));
                        } else {
                            digitalTwinInstanceAttr.setValue(new JSONObject());
                        }
                    }
                    if (digitalTwinInstanceAttr.getDatatype().equalsIgnoreCase(DataTypeEnum.TYPE_ARRAY.toString())) {
                        if (CommonUtil.isArray(digitalTwinInstanceAttr.getValue())) {
                            digitalTwinInstanceAttr.setValue(CommonUtil.str2JSONArray(digitalTwinInstanceAttr.getValue().toString()));
                        } else {
                            digitalTwinInstanceAttr.setValue(new JSONArray());
                        }
                    }
                }
            }
            return JsonResp.getSuccessResp(digitalTwinInstanceAttr);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                logger.error("detail parameter dt open instance attr is invalid: {},{},{},{},{}",
                        id, start, end, downsample, aggregator);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, messagesService.getMessage("parameter.error"));
            }
            logger.error("detail dt open instance attribute error: {},{},{},{},{},{}", id, start, end, downsample, aggregator, e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
        return JsonResp.getErrorResp(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, messagesService.getMessage("parameter.error"));
    }
}
