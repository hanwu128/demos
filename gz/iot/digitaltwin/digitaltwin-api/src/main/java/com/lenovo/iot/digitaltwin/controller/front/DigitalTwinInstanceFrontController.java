package com.lenovo.iot.digitaltwin.controller.front;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.digitaltwin.exception.UniqueRecordExeception;
import com.lenovo.iot.digitaltwin.model.*;
import com.lenovo.iot.digitaltwin.service.DigitalTwinInstanceFrontService;
import com.lenovo.iot.digitaltwin.service.DigitalTwinReverseControlService;
import com.lenovo.iot.digitaltwin.service.DigitalTwinTemplateFrontService;
import com.lenovo.iot.digitaltwin.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Desc: DT实例管理
 * Name: com.lenovo.iot.digitaltwin.controller.ft.DigitalTwinInstanceController
 * Author: chench9@lenovo.com
 * Date: 2018/6/8 16:51
 **/
@Controller
@RequestMapping("/dt/front/instance")
public class DigitalTwinInstanceFrontController {
    private static final Logger logger = LoggerFactory.getLogger(DigitalTwinInstanceFrontController.class);

    @Autowired
    private DigitalTwinInstanceFrontService digitalTwinInstanceFrontService;

    @Autowired
    private DigitalTwinTemplateFrontService digitalTwinTemplateFrontService;

    @Autowired
    private DigitalTwinReverseControlService digitalTwinReverseControlService;

    /**
     * 查询DT实例列表
     *
     * @param req
     * @param resp
     * @param params
     * @return
     */
    @PostMapping("/list.ft")
    @ResponseBody
    public Object list(HttpServletRequest req, HttpServletResponse resp,
                       @RequestBody JSONObject params) {
        try {
            Integer page = 0;
            Integer offset = 0;
            if (!StringUtils.isEmpty(params.getString("page").trim())) {
                page = CommonUtil.str2Integer(params.getString("page").trim());
            }
            if (!StringUtils.isEmpty(params.getString("offset").trim())) {
                offset = CommonUtil.str2Integer(params.getString("offset").trim());
            }

            if (page <= 0) page = 1;
            if (offset <= 0) offset = 10;
            int start = PageUtil.getStart(page, offset);
            String name = params.getString("name");
            int total = 0;
            int pageTotal = 1;
            if (CommonUtil.isSpecialCharOutUnderline(name)) {
                logger.warn("list dt instance name is invalid:{}", params);
                return JsonResp.getSuccessResp(new JsonList(total, pageTotal, page, offset, new ArrayList<DigitalTwinTemplate>()));
            }
            total = digitalTwinInstanceFrontService.getDigitalTwinInstanceTotal(name);
            pageTotal = PageUtil.total(total, offset);
            String uid = null;
            if (ThreadUtil.getThreadInstance().get() == null) {
                uid = "";
                logger.info("list token is invalid");
            } else {
                uid = ThreadUtil.getThreadInstance().get().getUserId();
            }
            List<DigitalTwinInstance> digitalTwinInstanceList = digitalTwinInstanceFrontService.getDigitalTwinInstanceList(start, offset, name, uid);
            return JsonResp.getSuccessResp(new JsonList(total, pageTotal, page, offset, digitalTwinInstanceList));
        } catch (Exception e) {
            logger.error("list dt instance error: {}, {}", params, e.getMessage());
            e.printStackTrace();
        }
        return JsonResp.getErrorResp();
    }

    /**
     * 查询DT实例详情
     *
     * @param req
     * @param resp
     * @param params
     * @return
     */
    @PostMapping("/detail.ft")
    @ResponseBody
    public Object detail(HttpServletRequest req, HttpServletResponse resp,
                         @RequestBody JSONObject params) {
        try {
            Long id = params.getLong("id");
            if (id == null || id <= 0) {
                logger.error("detail dt instance id is invalid: {}", params);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }
            DigitalTwinInstance digitalTwinInstance = digitalTwinInstanceFrontService.getDigitalTwinInstanceDetail(id);
            if (digitalTwinInstance == null) {
                logger.error("detail dt instance not found: {}", params);
                return JsonResp.getErrorResp(HttpServletResponse.SC_NOT_FOUND, JsonResp.MSG_NOT_FOUND);
            }
            if (digitalTwinInstance.getAttr() != null) {
                digitalTwinInstance.setAttrnum(digitalTwinInstance.getAttr().size());
            }
            return JsonResp.getSuccessResp(digitalTwinInstance);
        } catch (Exception e) {
            logger.error("detail dt instance error: {}, {}", params, e.getMessage());
            e.printStackTrace();
        }
        return JsonResp.getErrorResp();
    }

    /**
     * 添加DT实例
     *
     * @param req
     * @param resp
     * @param params
     * @return
     */
    @PostMapping("/add.ft")
    @ResponseBody
    public Object add(HttpServletRequest req, HttpServletResponse resp,
                      @RequestBody JSONObject params) {
        DigitalTwinInstance dtInstance = null;
        try {
            dtInstance = parseDigitalTwinInstance(params);
            if (dtInstance.getUid() == null || dtInstance.getGid() == null) {
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }
            if (StringUtils.isEmpty(dtInstance.getName()) || CommonUtil.isSpecialCharOutUnderline(dtInstance.getName())) {
                logger.error("add dt instance name is invalid: {}", params);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }

            DigitalTwinTemplate dtTpl = digitalTwinTemplateFrontService.getDigitalTwinTemplateById(dtInstance.getTpl());
            if (dtTpl == null) {
                // DT模板不存在
                logger.error("add dt instance template not found: {}", params);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }

            List<DigitalTwinTemplateAttr> dtTplAttrList = dtTpl.getAttr();
            List<DigitalTwinInstanceAttr> dtInstanceAttrList = newList(dtTplAttrList, dtInstance);
            for (DigitalTwinInstanceAttr dtInstanceAttr : dtInstanceAttrList) {
                if (StringUtils.isEmpty(dtInstanceAttr.getName()) || CommonUtil.isSpecialCharOutUnderline(dtInstanceAttr.getName())) {
                    logger.error("add dt instance attr name is invalid: {}", params);
                    return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
                }
            }
            digitalTwinInstanceFrontService.addDigitalTwinInstance(dtInstance, dtInstanceAttrList);
            return JsonResp.getSuccessResp();
        } catch (UniqueRecordExeception e) {
            logger.error("add dt instance error: {}, {}", params, e.getMessage());
            return JsonResp.getErrorResp(HttpServletResponse.SC_CONFLICT, "add dt instance exists with name: " + dtInstance.getName());
        } catch (Exception e) {
            logger.error("add dt instance error: {}, {}", params, e.getMessage());
            e.printStackTrace();
        }
        return JsonResp.getErrorResp();
    }

    // 解析DT实例
    private DigitalTwinInstance parseDigitalTwinInstance(JSONObject params) {
        Long id = params.getLong("id");
        String name = params.getString("name");
        String desp = params.getString("desp");
        Long tpl = params.getLong("tpl");

        DigitalTwinInstance digitalTwinInstance = new DigitalTwinInstance();
        digitalTwinInstance.setId(id);
        digitalTwinInstance.setName(name);
        digitalTwinInstance.setDesp(desp);
        digitalTwinInstance.setTpl(tpl);
        ThreadUser tuser = ThreadUtil.getThreadInstance().get();
        if (tuser == null) {
            digitalTwinInstance.setUid("");
            digitalTwinInstance.setGid("");
            logger.info("user infor id null");
        } else {
            digitalTwinInstance.setUid(tuser.getUserId());
            digitalTwinInstance.setGid(tuser.getGroupId());
        }
        return digitalTwinInstance;
    }

    /**
     * 返回一个新的模板List
     */
    public List<DigitalTwinInstanceAttr> newList(List<DigitalTwinTemplateAttr> dtTplAttrList, DigitalTwinInstance paramInstance) {
        List<DigitalTwinInstanceAttr> dtInstanceAttrList = new ArrayList<DigitalTwinInstanceAttr>();
        for (DigitalTwinTemplateAttr templateAttr : dtTplAttrList) {
            DigitalTwinInstanceAttr instanceAttr = new DigitalTwinInstanceAttr();
            instanceAttr.setInstance(paramInstance.getId());
            instanceAttr.setName(templateAttr.getName());
            instanceAttr.setDisplayname(templateAttr.getDisplayname());
            instanceAttr.setDatatype(templateAttr.getDatatype());
            instanceAttr.setAttrtype(templateAttr.getAttrtype());
            instanceAttr.setValue(templateAttr.getValue());
            instanceAttr.setExpectvalue("");
            instanceAttr.setUnit(templateAttr.getUnit());
            instanceAttr.setMetric("");
            instanceAttr.setDeviceid("");
            dtInstanceAttrList.add(instanceAttr);
        }
        return dtInstanceAttrList;
    }

    /**
     * 更新DT实例
     *
     * @param req
     * @param resp
     * @param params
     * @return
     */
    @PostMapping("/update.ft")
    @ResponseBody
    public Object update(HttpServletRequest req, HttpServletResponse resp,
                         @RequestBody JSONObject params) {
        DigitalTwinInstance paramInstance = null;
        try {
            paramInstance = parseDigitalTwinInstance(params);
            if (paramInstance.getId() <= 0) {
                logger.error("update dt instance error, id is empty!");
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }
            if (StringUtils.isEmpty(paramInstance.getName()) || CommonUtil.isSpecialCharOutUnderline(paramInstance.getName())) {
                logger.error("update dt instance name is invalid: {}", params);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }
            DigitalTwinInstance dbInstance = digitalTwinInstanceFrontService.getDigitalTwinInstance(paramInstance.getId());
            List<DigitalTwinInstanceAttr> dtInstanceAttrList = new ArrayList<>();
            if (paramInstance.getTpl().longValue() != dbInstance.getTpl().longValue()) {
                DigitalTwinTemplate dtTpl = digitalTwinTemplateFrontService.getDigitalTwinTemplateById(paramInstance.getTpl());
                if (dtTpl == null) {
                    // DT模板不存在
                    logger.error("update dt template not found: {}", paramInstance);
                    return JsonResp.getErrorResp(HttpServletResponse.SC_NOT_FOUND, JsonResp.MSG_NOT_FOUND);
                }

                List<DigitalTwinTemplateAttr> dtTplAttrList = dtTpl.getAttr();
                dtInstanceAttrList = newList(dtTplAttrList, paramInstance);
            }
            for (DigitalTwinInstanceAttr dtInstanceAttr : dtInstanceAttrList) {
                if (StringUtils.isEmpty(dtInstanceAttr.getName()) || CommonUtil.isSpecialCharOutUnderline(dtInstanceAttr.getName())) {
                    logger.error("update dt instance attr name is invalid: {}", params);
                    return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
                }
            }

            if (digitalTwinInstanceFrontService.updateDigitalTwinInstance(paramInstance, dtInstanceAttrList, dbInstance) <= 0) {
                logger.error("update dt instance error: {}", params);
                return JsonResp.getErrorResp();
            }
            return JsonResp.getSuccessResp();
        } catch (UniqueRecordExeception e) {
            logger.error("update dt instance error: {}, {}", params, e.getMessage());
            return JsonResp.getErrorResp(HttpServletResponse.SC_CONFLICT, "update dt instance exists with name: " + paramInstance.getName());
        } catch (Exception e) {
            logger.error("update dt instance exception: {}, {}", params, e.getMessage());
            e.printStackTrace();
        }

        return JsonResp.getErrorResp();
    }

    /**
     * 更新DT实例期望值
     *
     * @param req
     * @param resp
     * @param params
     * @return
     */
    @PostMapping("/update/expectvalue.ft")
    @ResponseBody
    public Object updateExpectvalue(HttpServletRequest req, HttpServletResponse resp,
                                    @RequestBody JSONObject params) {
        try {
            Long dtId = params.getLong("id");
            if (dtId == null) {
                logger.error("update dt instance expect value error, id is empty!");
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }

            List<DigitalTwinInstanceAttr> attrList = parseDigitalTwinInstanceAttr(params);
            if (attrList == null || attrList.isEmpty()) {
                logger.error("update dt instance expect value error, attr list is empty!");
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }
            //List<Long> idList = digitalTwinInstanceFrontService.updateDigitalTwinInstanceExpectValue(attrList);
            //List<Long> idList = digitalTwinReverseControlService.updateDigitalTwinInstanceAttrExpectValue(attrList);
            //digitalTwinReverseControlService.sendControlDeviceMessage(idList);
            digitalTwinReverseControlService.reverseDevice(attrList);
            return JsonResp.getSuccessResp();
        } catch (Exception e) {
            logger.error("update dt instance expect value error: {}, {}", params, e.getMessage());
            e.printStackTrace();
        }
        return JsonResp.getErrorResp();
    }

    // 解析DT实例属性
    private List<DigitalTwinInstanceAttr> parseDigitalTwinInstanceAttr(JSONObject params) {
        JSONArray attrArr = params.getJSONArray("attr");
        if (attrArr == null) {
            return null;
        }
        return CommonUtil.parseDigitalTwinAttrList(attrArr);
    }

    /**
     * 删除DT实例
     *
     * @param req
     * @param resp
     * @param params
     * @return
     */
    @PostMapping("/delete.ft")
    @ResponseBody
    public Object delete(HttpServletRequest req, HttpServletResponse resp,
                         @RequestBody JSONObject params) {
        try {
            String idsStr = params.getString("ids");
            if (idsStr == null || "".equals(idsStr.trim())) {
                logger.error("delete dt instance error, id is empty!");
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }

            String[] idStrArr = idsStr.split(",");
            if (idStrArr == null || idStrArr.length <= 0) {
                logger.error("delete dt instance error, id is empty!");
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }
            List<Long> idList = new ArrayList<Long>();
            for (String idStr : idStrArr) {
                Long id = NumberUtils.parseNumber(idStr, Long.class);
                idList.add(id);
            }
            //获取被锁定的实例
            List<DigitalTwinInstance> instanceList = digitalTwinInstanceFrontService.getDigitalTwinInstanceByListId(idList);
            if (instanceList != null && !instanceList.isEmpty()) {
                List<String> names = new ArrayList<String>();
                for (DigitalTwinInstance dtInstance : instanceList) {
                    names.add(dtInstance.getName());
                }
                return JsonResp.getErrorResp(JsonResp.CODE_NOT_DELETE, "delete dt instance error, instance is using : " + names);
            }
            if (digitalTwinInstanceFrontService.deleteDigitalTwinInstance(idList) > 0) {
                return JsonResp.getSuccessResp();
            }
        } catch (Exception e) {
            logger.error("delete dt instance error: {}, {}", params, e.getMessage());
            e.printStackTrace();
        }
        return JsonResp.getErrorResp();
    }

    /**
     * 查询DT实例属性stream类型tsdb数据
     *
     * @param req
     * @param resp
     * @param params
     * @return
     */
    @PostMapping("/tsdb.ft")
    @ResponseBody
    public Object tsdb(HttpServletRequest req, HttpServletResponse resp, @RequestBody JSONObject params) {
        Long id = params.getLong("id");
        try {
            if (id == null || id <= 0) {
                logger.error("tsdb parameter dt front instance attribute id is invalid: {}", id);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }
            DigitalTwinInstanceAttr digitalTwinInstanceAttr = digitalTwinInstanceFrontService.getDigitalTwinInstanceAttr(id);
            if (digitalTwinInstanceAttr == null) {
                logger.error("tsdb dt front instance attribute not found: {}", id);
                return JsonResp.getErrorResp(HttpServletResponse.SC_NOT_FOUND, JsonResp.MSG_NOT_FOUND);
            }
            JSONObject resultJson = new JSONObject();
            resultJson.put("id", id);
            if (StringUtils.isEmpty(digitalTwinInstanceAttr.getMetric())) {
                logger.error("tsdb dt front instance attribute metric is null: {}", id);
                return JsonResp.getSuccessResp(resultJson);
            }
            DigitalTwinInstance digitalTwinInstance = digitalTwinInstanceFrontService.getDigitalTwinInstanceDetail(digitalTwinInstanceAttr.getInstance());
            if (digitalTwinInstance == null) {
                logger.error("tsdb dt front instance not found: {}", id);
                return JsonResp.getSuccessResp(resultJson);
            }
            //获取当前时间戳
            Long nowDate = new Date().getTime();
            //获取OpenTsDb中的数据
            Object results = TsDbUtil.getQueryFromTsDb(id, digitalTwinInstance.getName(), digitalTwinInstanceAttr.getDeviceid(),
                    digitalTwinInstanceAttr.getMetric(), nowDate - (60 * 60 * 1000), nowDate, null, null);
            if (results == null) {
                logger.warn("tsdb dt front instance results is null : {}", params);
                return JsonResp.getSuccessResp(resultJson);
            }
            // fast json的parseArray方法会导致map键值变为无序
            //JSONArray tsdbArr = JSONArray.parseArray(results.toString());
            JSONArray tsdbArr = (JSONArray) results;
            List<Object> timeList = new ArrayList<Object>();
            List<Object> valueList = new ArrayList<Object>();
            if(!tsdbArr.isEmpty()) {
                JSONObject object = tsdbArr.getJSONObject(0);
                /*//获取dps值
                String dpsString = object.getString("dps");
                if (StringUtils.isEmpty(dpsString)) {
                    logger.error("list dt front instance attribute tsdb dps is null: {}", id);
                    return JsonResp.getSuccessResp();
                }
                Map<String, Object> dpsMapOld = JSONObject.parseObject(dpsString);
                //按照Map的key排序
                dpsMapOld = CommonUtil.sortMapByKey(dpsMapOld);*/
                JSONObject dpsJson = object.getJSONObject("dps");
                if(dpsJson != null) {
                    Map<String, Object> dpsMapOld = dpsJson.getInnerMap();
                    for (String key : dpsMapOld.keySet()) {
                        String date = CommonUtil.timeStamp2Date(key, "HH:mm:ss");
                        timeList.add(date);
                        valueList.add(dpsMapOld.get(key));
                    }
                }
            }

            resultJson.put("time", timeList);
            resultJson.put("value", valueList);
            return JsonResp.getSuccessResp(resultJson);
        } catch (Exception e) {
            logger.error("tsdb dt front instance attribute error: {}, {}", id, e.getMessage());
            e.printStackTrace();
        }
        return JsonResp.getErrorResp();
    }
}
