package com.lenovo.iot.digitaltwin.controller.open;

import com.alibaba.fastjson.JSONArray;
import com.lenovo.iot.digitaltwin.model.DigitalTwinInstance;
import com.lenovo.iot.digitaltwin.model.DigitalTwinInstanceAttr;
import com.lenovo.iot.digitaltwin.service.DigitalTwinInstanceOpenService;
import com.lenovo.iot.digitaltwin.service.DigitalTwinReverseControlService;
import com.lenovo.iot.digitaltwin.service.MessagesService;
import com.lenovo.iot.digitaltwin.util.CommonUtil;
import com.lenovo.iot.digitaltwin.util.JsonList;
import com.lenovo.iot.digitaltwin.util.JsonResp;
import com.lenovo.iot.digitaltwin.util.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Desc: 访问DT实例列表
 * Name: com.lenovo.iot.digitaltwin.controller.open.DigitalTwinInstanceOpenController
 * Author: hanwu
 * Date: 2018/7/3 10:00
 **/
@RestController
@RequestMapping("/dt/open/v1")
public class DigitalTwinInstanceOpenController {
    private static final Logger logger = LoggerFactory.getLogger(DigitalTwinInstanceOpenController.class);

    @Autowired
    private MessagesService messagesService;

    @Autowired
    private DigitalTwinInstanceOpenService digitalTwinInstanceOpenService;

    @Autowired
    private DigitalTwinReverseControlService digitalTwinReverseControlService;

    /**
     * 根据实例名称，分页参数，查询数查询DT实例列表
     *
     * @param req
     * @param resp
     * @param name
     * @param pageStr
     * @param offsetStr
     * @return
     */
    @GetMapping("/instances")
    public Object list(HttpServletRequest req, HttpServletResponse resp,
                       @RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "page", required = false) String pageStr,
                       @RequestParam(value = "offset", required = false) String offsetStr) {
        Integer page = 0;
        Integer offset = 0;
        if (pageStr != null && !"".equals(pageStr.trim())) {
            page = CommonUtil.str2Integer(pageStr);
            if (page < 0) {
                logger.error("page parameter error: {}", pageStr);
                return JsonResp.httpCode(resp, HttpServletResponse.SC_BAD_REQUEST).errorResp(messagesService.getMessage("parameter.error.name", new Object[]{"page"}));
            }
        }

        if (offsetStr != null && !"".equals(offsetStr.trim())) {
            offset = CommonUtil.str2Integer(offsetStr);
            if (offset < 0) {
                logger.error("offset parameter error: {}", offsetStr);
                return JsonResp.httpCode(resp, HttpServletResponse.SC_BAD_REQUEST).errorResp(messagesService.getMessage("parameter.error.name", new Object[]{"offset"}));
            }
        }

        if (CommonUtil.isSpecialCharOutUnderline(name)) {
            logger.error("name parameter error: {}", name);
            return JsonResp.httpCode(resp, HttpServletResponse.SC_BAD_REQUEST).errorResp(messagesService.getMessage("parameter.error.name", new Object[]{"name"}));
        }

        try {
            if (page <= 0) page = 1;
            if (offset <= 0) offset = 10;
            int start = PageUtil.getStart(page, offset);
            int total = digitalTwinInstanceOpenService.getDigitalTwinInstanceTotal(name);
            int pageTotal = PageUtil.total(total, offset);
            List<DigitalTwinInstance> digitalTwinInstanceList = digitalTwinInstanceOpenService.getDigitalTwinInstanceList(start, offset, name);
            if (digitalTwinInstanceList == null) {
                digitalTwinInstanceList = new ArrayList<DigitalTwinInstance>();
                logger.warn("list dt open instance not found: {},{},{}", name, page, offset);
            }
            return JsonResp.getSuccessResp(new JsonList(total, pageTotal, page, offset, digitalTwinInstanceList), messagesService.getMessage("success"));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("list dt open instance error: {},{},{},{}", name, pageStr, offsetStr, e.getMessage());
            e.printStackTrace();
        }
        return JsonResp.getErrorResp(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, messagesService.getMessage("error"));
    }

    /**
     * 根据实例ID查询DT实例
     *
     * @param req
     * @param resp
     * @param idsStr
     * @return
     */
    @GetMapping("/instance")
    public Object detail(HttpServletRequest req, HttpServletResponse resp,
                         @RequestParam("ids") String idsStr) {
        try {
            if (StringUtils.isEmpty(idsStr)) {
                logger.error("detail dt open instance error, id is empty!");
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, messagesService.getMessage("parameter.empty.name", new Object[]{"ids"}));
            }
            if (CommonUtil.isSpecialCharOutComma(idsStr) || CommonUtil.isContainsLetter(idsStr) || CommonUtil.isContainChinese(idsStr)) {
                logger.error("detail dt open instance error, id is invalid!");
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, messagesService.getMessage("parameter.error.name", new Object[]{"ids"}));
            }
            String[] idStrArr = idsStr.split(",");
            if (idStrArr == null || idStrArr.length <= 0) {
                logger.error("detail dt open instance error, id is empty!");
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, messagesService.getMessage("parameter.error"));
            }
            List<String> idsList = Arrays.asList(idStrArr);
            if (idsList == null || idsList.isEmpty()) {
                logger.error("detail dt instance error, id is empty!");
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, messagesService.getMessage("parameter.error"));
            }
            List<DigitalTwinInstance> digitalTwinInstanceList = digitalTwinInstanceOpenService.getDigitalTwinInstanceDetail(idsList);
            if (digitalTwinInstanceList == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                logger.error("detail dt open instance not found: {}", idsStr);
                return JsonResp.getErrorResp(HttpServletResponse.SC_NOT_FOUND, messagesService.getMessage("not.found", new Object[]{idsStr}));
            }
            for (DigitalTwinInstance instance : digitalTwinInstanceList) {
                if (instance.getAttr() != null) {
                    instance.setAttrnum(instance.getAttr().size());
                }
            }
            return JsonResp.getSuccessResp(digitalTwinInstanceList);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("detail dt open instance error: {}, {}", idsStr, e.getMessage());
            e.printStackTrace();
        }
        return JsonResp.getErrorResp(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, messagesService.getMessage("error"));
    }

    /**
     * 反向控制指定设备
     *
     * @param req
     * @param resp
     * @param id
     * @param arr
     * @return
     */
    @PatchMapping("/instance/{id}")
    public Object reverseControl(HttpServletRequest req, HttpServletResponse resp,
                                 @PathVariable("id") Long id,
                                 @RequestBody JSONArray arr) {
        DigitalTwinInstance ist = digitalTwinReverseControlService.getDigitalTwinInstanceDetailById(id);
        if (ist == null) {
            logger.error("reverse control dt instance not found: {}", id);
            return JsonResp.httpCode(resp, HttpServletResponse.SC_NOT_FOUND).errorResp(messagesService.getMessage("not.found", new Object[]{id}));
        }

        List<DigitalTwinInstanceAttr> attrList = CommonUtil.parseDigitalTwinAttrList(arr);
        if (attrList == null) {
            logger.error("reverse control dt instance parameter error: {}", arr);
            return JsonResp.httpCode(resp, HttpServletResponse.SC_BAD_REQUEST).errorResp(messagesService.getMessage("parameter.error"));
        }

        try {
            //List<Long> idList = digitalTwinReverseControlService.updateDigitalTwinInstanceAttrExpectValue(attrList);
            //digitalTwinReverseControlService.sendControlDeviceMessage(idList);
            digitalTwinReverseControlService.reverseDevice(attrList);
            return JsonResp.httpCode(resp, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            logger.error("reverse control dt instance error: {}, {}", arr, e.getMessage());
            e.printStackTrace();
        }

        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return JsonResp.httpCode(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR).errorResp(messagesService.getMessage("error"));
    }

}
