package com.lenovo.iot.digitaltwin.controller.front;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.digitaltwin.exception.NullRecordExeception;
import com.lenovo.iot.digitaltwin.exception.UniqueAttrRecordExeception;
import com.lenovo.iot.digitaltwin.exception.UniqueRecordExeception;
import com.lenovo.iot.digitaltwin.model.DigitalTwinTemplate;
import com.lenovo.iot.digitaltwin.model.DigitalTwinTemplateAttr;
import com.lenovo.iot.digitaltwin.service.DigitalTwinTemplateFrontService;
import com.lenovo.iot.digitaltwin.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Desc: DT模板管理
 * Name: com.lenovo.iot.digitaltwin.controller.ft.DigitalTwinTemplateController
 * Author: chench9@lenovo.com
 * Date: 2018/6/8 16:51
 **/
@Controller
@RequestMapping("/dt/front/tpl")
public class DigitalTwinTemplateFrontController {
    private static final Logger logger = LoggerFactory.getLogger(DigitalTwinTemplateFrontController.class);

    @Autowired
    private DigitalTwinTemplateFrontService digitalTwinTemplateFrontService;

    /**
     * 查询DT模板列表
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
            int page = params.getInteger("page");
            if (page <= 0) {
                page = 1;
            }
            int offset = params.getInteger("offset");
            if (offset <= 0) {
                offset = 10;
            }
            int start = PageUtil.getStart(page, offset);
            String name = params.getString("name");
            int total = 0;
            int pageTotal = 1;
            if (CommonUtil.isSpecialCharOutUnderline(name)) {
                logger.warn("list dt tpl name is invalid:{}", params);
                return JsonResp.getSuccessResp(new JsonList(total, pageTotal, page, offset, new ArrayList<DigitalTwinTemplate>()));
            }
            total = digitalTwinTemplateFrontService.getDigitalTwinTemplateTotal(name);
            pageTotal = PageUtil.total(total, offset);
            List<DigitalTwinTemplate> digitalTwinTemplateList = digitalTwinTemplateFrontService.getDigitalTwinTemplateList(start, offset, name);
            return JsonResp.getSuccessResp(new JsonList(total, pageTotal, page, offset, digitalTwinTemplateList));
        } catch (Exception e) {
            logger.error("list dt tpl error: {}, {}", params, e.getMessage());
            e.printStackTrace();
        }
        return JsonResp.getErrorResp();
    }

    /**
     * 查询DT详情
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
                logger.error("detail dt tpl id is invalid: {}", params);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }
            DigitalTwinTemplate digitalTwinTemplate = digitalTwinTemplateFrontService.getDigitalTwinTemplateById(id);
            if (digitalTwinTemplate == null) {
                logger.error("detail dt tpl not found: {}", params);
                return JsonResp.getErrorResp(HttpServletResponse.SC_NOT_FOUND, JsonResp.MSG_NOT_FOUND);
            }
            if (digitalTwinTemplate.getAttr() != null) {
                digitalTwinTemplate.setAttrnum(digitalTwinTemplate.getAttr().size());
            }
            return JsonResp.getSuccessResp(digitalTwinTemplate);
        } catch (Exception e) {
            logger.error("detail dt tpl error: {}, {}", params, e.getMessage());
            e.printStackTrace();
        }
        return JsonResp.getErrorResp();
    }

    /**
     * 添加DT模板
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
        DigitalTwinTemplate dtTpl = null;
        try {
            dtTpl = parseDigitalTwinTemplate(params);
            if (StringUtils.isEmpty(dtTpl.getName()) || CommonUtil.isSpecialCharOutUnderline(dtTpl.getName())) {
                logger.error("add dt tpl name is invalid: {}", params);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }
            List<DigitalTwinTemplateAttr> attrList = parseDigitalTwinTemplateAttr(dtTpl.getId(), params);
            Map<String, Object> map = new HashMap<String, Object>();
            for (DigitalTwinTemplateAttr dtAttr : attrList) {
                if (StringUtils.isEmpty(dtAttr.getName()) || CommonUtil.isSpecialCharOutUnderline(dtAttr.getName())) {
                    logger.error("add dt tpl attr name is invalid: {}", params);
                    return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
                }
                if (!StringUtils.isEmpty(dtAttr.getName())) {
                    if (map.containsKey(dtAttr.getName())) {
                        logger.error("add dt tpl attr error: {}", params);
                        return JsonResp.getErrorResp(JsonResp.CODE_ATTR_NAME_CONFLICT, "add dt tpl attr exists with name: " + dtAttr.getName());
                    }
                    map.put(dtAttr.getName(), dtAttr.getName());
                }
            }
            if (!checkDataType(attrList, params)) {
                logger.error("add dt tpl attr value format invalid: {}", params);
                return JsonResp.getErrorResp(JsonResp.CODE_ATTR_VALUE_FORMAT_INVALID, "add dt tpl attr don't agree value with data type");
            }
            digitalTwinTemplateFrontService.addDigitalTwinTemplate(dtTpl, attrList);
            return JsonResp.getSuccessResp();
        } catch (UniqueRecordExeception e) {
            logger.error("add dt tpl error: {}, {}", params, e.getMessage());
            return JsonResp.getErrorResp(HttpServletResponse.SC_CONFLICT, "add dt tpl exists with name: " + dtTpl.getName());
        } catch (Exception e) {
            logger.error("add dt tpl error: {}, {}", params, e.getMessage());
            e.printStackTrace();
        }
        return JsonResp.getErrorResp();
    }

    // 解析DT模板参数
    private DigitalTwinTemplate parseDigitalTwinTemplate(JSONObject params) {
        Long id = params.getLong("id");
        if (id == null) {
            id = 0L;
        }
        String name = params.getString("name");
        if (name == null) {
            name = "";
        }
        String desp = params.getString("desp");
        if (desp == null) {
            desp = "";
        }
        DigitalTwinTemplate dtTpl = new DigitalTwinTemplate(name, desp);
        dtTpl.setId(id);
        return dtTpl;
    }

    // 解析DT模板属性参数
    private List<DigitalTwinTemplateAttr> parseDigitalTwinTemplateAttr(long tpl, JSONObject params) {
        JSONArray attrArr = params.getJSONArray("attr");
        if (attrArr == null) {
            return null;
        }

        List<DigitalTwinTemplateAttr> attrList = new ArrayList<DigitalTwinTemplateAttr>();
        int size = attrArr.size();
        for (int i = 0; i < size; i++) {
            JSONObject attr = attrArr.getJSONObject(i);
            Long id = attr.getLong("id");
            String name = attr.getString("name");
            if (name == null) {
                name = "";
            }
            String displayname = attr.getString("displayname");
            if (displayname == null) {
                displayname = "";
            }
            String attrtype = attr.getString("attrtype");
            if (attrtype == null) {
                attrtype = "";
            }
            String datatype = attr.getString("datatype");
            if (datatype == null) {
                datatype = "";
            }
            String value = attr.getString("value");
            if (value == null) {
                value = "";
            }
            String unit = attr.getString("unit");
            if (unit == null) {
                unit = "";
            }

            DigitalTwinTemplateAttr dtAttr = new DigitalTwinTemplateAttr();
            dtAttr.setId(id);
            dtAttr.setTpl(tpl);
            dtAttr.setName(name);
            dtAttr.setDisplayname(displayname);
            dtAttr.setDatatype(datatype);
            dtAttr.setAttrtype(attrtype);
            dtAttr.setValue(value);
            dtAttr.setUnit(unit);
            attrList.add(dtAttr);
        }
        return attrList;
    }

    /**
     * 校验DT模板属性数据类型与值是否匹配
     *
     * @param dtTemplateAttrList
     * @param params
     * @return
     */
    private boolean checkDataType(List<DigitalTwinTemplateAttr> dtTemplateAttrList, JSONObject params) {
        for (DigitalTwinTemplateAttr dtAttr : dtTemplateAttrList) {
            if (!StringUtils.isEmpty(dtAttr.getDatatype()) && DataTypeEnum.TYPE_STRING.toString().equalsIgnoreCase(dtAttr.getDatatype())) {
                if (dtAttr.getValue() == null || "".equals(dtAttr.getValue())) {
                    dtAttr.setValue("");
                }
            }
            if (!StringUtils.isEmpty(dtAttr.getDatatype()) && DataTypeEnum.TYPE_ARRAY.toString().equalsIgnoreCase(dtAttr.getDatatype())) {
                if (dtAttr.getValue() == null || "".equals(dtAttr.getValue())) {
                    dtAttr.setValue(new JSONArray().toJSONString());
                } else if (!CommonUtil.isArray(dtAttr.getValue())) {
                    return false;
                }
            }
            if (!StringUtils.isEmpty(dtAttr.getDatatype()) && DataTypeEnum.TYPE_BOOLEAN.toString().equalsIgnoreCase(dtAttr.getDatatype())) {
                if (dtAttr.getValue() == null || "".equals(dtAttr.getValue())) {
                    dtAttr.setValue(false);
                } else if (!CommonUtil.isBoolean(dtAttr.getValue())) {
                    return false;
                }
            }
            if (!StringUtils.isEmpty(dtAttr.getDatatype()) && DataTypeEnum.TYPE_NUMBER.toString().equalsIgnoreCase(dtAttr.getDatatype())) {
                if (dtAttr.getValue() == null || "".equals(dtAttr.getValue())) {
                    dtAttr.setValue(0);
                } else if (!CommonUtil.isNumeric(CommonUtil.obj2String(dtAttr.getValue()))) {
                    return false;
                }
            }
            if (!StringUtils.isEmpty(dtAttr.getDatatype()) && DataTypeEnum.TYPE_OBJECT.toString().equalsIgnoreCase(dtAttr.getDatatype())) {
                if (dtAttr.getValue() == null || "".equals(dtAttr.getValue())) {
                    dtAttr.setValue(new JSONObject().toJSONString());
                } else if (!CommonUtil.isObject(dtAttr.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 编辑DT模板
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
        try {
            Object object = updateInternal(params);
            if (object != null) {
                return object;
            }
            return JsonResp.getSuccessResp();
        } catch (UniqueRecordExeception e) {
            logger.error("update dt tpl error: {}, {}", params, e.getMessage());
            return JsonResp.getErrorResp(HttpServletResponse.SC_CONFLICT, "update dt tpl exists with name: " + params.get("name"));
        } catch (UniqueAttrRecordExeception e) {
            logger.error("update dt tpl attr error: {}, {}", params, e.getMessage());
            return JsonResp.getErrorResp(JsonResp.CODE_ATTR_NAME_CONFLICT, e.getMessage());
        } catch (NullRecordExeception e) {
            logger.error("update dt tpl is null: {}, {}", params, e.getMessage());
            return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("update dt tpl error: {}, {}", params, e.getMessage());
            e.printStackTrace();
        }
        return JsonResp.getErrorResp();
    }

    // 涉及批量更新，添加或删除模板属性，在一个事务中实现。
    protected Object updateInternal(JSONObject params) throws SQLException {
        DigitalTwinTemplate dtTpl = parseDigitalTwinTemplate(params);
        if (StringUtils.isEmpty(dtTpl.getName()) || CommonUtil.isSpecialCharOutUnderline(dtTpl.getName())) {
            logger.error("detail dt tpl name is invalid: {}", params);
            return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
        }
        List<DigitalTwinTemplateAttr> dtAttrList = parseDigitalTwinTemplateAttr(dtTpl.getId(), params);
        // 需要更新的属性
        List<DigitalTwinTemplateAttr> updateAttrList = new ArrayList<DigitalTwinTemplateAttr>();
        // 新添加的属性
        List<DigitalTwinTemplateAttr> addAttrList = new ArrayList<DigitalTwinTemplateAttr>();
        for (DigitalTwinTemplateAttr attr : dtAttrList) {
            if (attr.getId() == null || attr.getId() <= 0) {
                addAttrList.add(attr);
            } else {
                updateAttrList.add(attr);
            }
        }
        for (DigitalTwinTemplateAttr updateAttr : updateAttrList) {
            if (StringUtils.isEmpty(updateAttr.getName()) || CommonUtil.isSpecialCharOutUnderline(updateAttr.getName())) {
                logger.error("detail dt tpl attr name is invalid: {}", params);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }
        }
        for (DigitalTwinTemplateAttr addAttr : addAttrList) {
            if (StringUtils.isEmpty(addAttr.getName()) || CommonUtil.isSpecialCharOutUnderline(addAttr.getName())) {
                logger.error("detail dt tpl attr name is invalid: {}", params);
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }
        }

        // 需要删除的属性
        List<Long> deleteAttrIdList = new ArrayList<Long>();
        String deleteAttrIdStr = params.getString("delete");
        if (deleteAttrIdStr != null && !"".equals(deleteAttrIdStr.trim())) {
            String[] attrIdArr = deleteAttrIdStr.split(",");
            for (String attrId : attrIdArr) {
                Long id = NumberUtils.parseNumber(attrId, Long.class);
                deleteAttrIdList.add(id);
            }
        }
        if (!checkDataType(addAttrList, params) || !checkDataType(updateAttrList, params)) {
            logger.error("update dt tpl attr value format invalid: {}", params);
            return JsonResp.getErrorResp(JsonResp.CODE_ATTR_VALUE_FORMAT_INVALID, "update dt tpl attr don't agree value with data type");
        }
        digitalTwinTemplateFrontService.updateDigitalTwinTemplate(dtTpl, updateAttrList, addAttrList, deleteAttrIdList);
        return null;
    }

    /**
     * 删除DT模板
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
            String ids = params.getString("ids");
            if (ids == null || "".equals(ids.trim())) {
                logger.error("delete dt ids is empty!");
                return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
            }

            List<Long> tplIdList = new ArrayList<Long>();
            String[] idArr = ids.split(",");
            for (String idStr : idArr) {
                Long id = NumberUtils.parseNumber(idStr, Long.class);
                if (id > 0) {
                    tplIdList.add(id);
                }
            }

            if (digitalTwinTemplateFrontService.deleteDigitalTwinTemplate(tplIdList) > 0) {
                return JsonResp.getSuccessResp();
            }
        } catch (Exception e) {
            logger.error("delete dt tpl error: {}, {}", params, e.getMessage());
            e.printStackTrace();
        }
        return JsonResp.getErrorResp();
    }
}
