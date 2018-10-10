package com.lenovo.iot.devicemanager.controller.front;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.devicemanager.config.TsDb;
import com.lenovo.iot.devicemanager.model.*;
import com.lenovo.iot.devicemanager.service.DigitalTwinAttrService;
import com.lenovo.iot.devicemanager.service.DigitalTwinService;
import com.lenovo.iot.devicemanager.service.impl.DigitalTwinMirrorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Desc: 前端调用的DigitalTwin操作API
 * Name: com.lenovo.iot.devicemanager.controller.front.DtFrontController
 * Author: chench9@lenovo.com
 * Date: 2018/5/10 16:06
 **/
@Controller
@RequestMapping("/dt/front")
public class DtFrontController {
    private static final Logger logger = LoggerFactory.getLogger(DtFrontController.class);

    @Autowired
    private DigitalTwinService digitalTwinService;

    @Autowired
    private DigitalTwinAttrService digitalTwinAttrService;

    @Autowired
    private DigitalTwinMirrorServiceImpl _DigitalTwinMirrorServiceImpl;

    /**
     * 添加DT
     * @param req
     * @param resp
     * @param json
     * @return
     */
    @Deprecated
    @PostMapping("/add.do")
    @ResponseBody
    @Transactional
    public Object add(HttpServletRequest req, HttpServletResponse resp,
                        @RequestBody JSONObject json) {
        if(logger.isDebugEnabled()) {
            logger.debug("add dt json: %s", json);
        }

        if(json == null || json.size() <= 0) {
            logger.error("json is empty!");
            return JsonResp.getJsonRespError(HttpServletResponse.SC_BAD_REQUEST);
        }

        DigitalTwin digitalTwin = parseDigitalTwin(json);
        if(digitalTwin.getDigitaltwinname() == null || "".equals(digitalTwin.getDigitaltwinname().trim())) {
            logger.error("dt name is empty!");
            return JsonResp.getJsonRespError(HttpServletResponse.SC_BAD_REQUEST);
        }

        try {
            // 在一个事务中完成
            long dtId = digitalTwinService.updateDigitalTwin(digitalTwin);
            List<DigitalTwinAttrParam> dtAttrList = parseDigitalTwinAttr(json, dtId, digitalTwin.getDigitaltwinname());
            if(!dtAttrList.isEmpty()) {
                digitalTwinAttrService.addDigitalTwinAttr(dtAttrList);
            }
            return JsonResp.getJsonRespSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("add dt error", e);
        }

        return JsonResp.getJsonRespError();
    }

    private DigitalTwin parseDigitalTwin(JSONObject json) {
        Long id = json.getLong("id");
        if(id == null) {
            id = 0L;
        }
        String name = json.getString("digitaltwinname");
        String desc = json.getString("describemessage");
        DigitalTwin dt = new DigitalTwin();
        dt.setId(id);
        dt.setDigitaltwinname(name);
        dt.setDescribemessage(desc);
        return dt;
    }

    private List<DigitalTwinAttrParam> parseDigitalTwinAttr(JSONObject json, long dtId, String dtName) {
        JSONArray attrArr = json.getJSONArray("attrList");
        if(attrArr == null || attrArr.size() <= 0) {
            return new ArrayList<DigitalTwinAttrParam>();
        }

        int size = attrArr.size();
        List<DigitalTwinAttrParam> dtAttrList = new ArrayList<DigitalTwinAttrParam>(size);
        for(int i = 0; i < size; i++) {
            DigitalTwinAttrParam dtAttr = new DigitalTwinAttrParam();
            JSONObject attrJson = attrArr.getJSONObject(i);
            dtAttr.setDaddyid(dtId);
            dtAttr.setDigitaltwinname(dtName);
            dtAttr.setId(attrJson.getLong("id"));
            dtAttr.setSonname(attrJson.getString("sonname"));
            dtAttr.setDescribemessage(attrJson.getString("describemessage"));
            dtAttr.setUnit(attrJson.getString("unit"));
            dtAttr.setMetric(attrJson.getString("metric"));
            dtAttr.setTagkv(attrJson.getJSONObject("tagkv").toJSONString());
            dtAttrList.add(i, dtAttr);
        }
        return dtAttrList;
    }

    /**
     * 根据名称查询DT列表
     * @param req
     * @param resp
     * @param json
     * @return
     */
    @PostMapping("/list.do")
    @ResponseBody
    public Object list(HttpServletRequest req, HttpServletResponse resp,
                       @RequestBody JSONObject json) {
        // DT名称,查询全部时为空
        String name = json.getString("name");
        // 当前页
        int current = json.getInteger("current");
        // 当前页数量
        int pagesize = json.getInteger("pagesize");
        if(current <= 0) {
            current = 1;
        }
        if(pagesize <= 0) {
            pagesize = 10;
        }
        int start = (current - 1) * pagesize;
        int total = 0;
        List<DigitalTwin> digitalTwinList = null;
        try {
            total = digitalTwinService.count(name);
            digitalTwinList = digitalTwinService.getDigitalTwinList(name, start, pagesize);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("get dt list error, dt name: %s, start: %s, offset: %s", name, start, pagesize, e);
        }

        JSONObject data = new JSONObject();
        data.put("pagesize", pagesize);
        data.put("current", current);
        data.put("total", total);
        data.put("rows", digitalTwinList);
        return JsonResp.getJsonRespSuccess(data);
    }

    /**
     * 编辑DT
     * @param req
     * @param resp
     * @param json
     * @return
     */
    @PostMapping("/update.do")
    @ResponseBody
    @Transactional
    public Object update(HttpServletRequest req, HttpServletResponse resp,
                        @RequestBody JSONObject json) {
        if(logger.isDebugEnabled()) {
            logger.debug("update dt json: %s", json);
        }

        if(json == null || json.size() <= 0) {
            logger.error("json is empty!");
            return JsonResp.getJsonRespError(HttpServletResponse.SC_BAD_REQUEST);
        }

        DigitalTwin digitalTwin = parseDigitalTwin(json);
        if(digitalTwin.getDigitaltwinname() == null || "".equals(digitalTwin.getDigitaltwinname().trim())) {
            logger.error("dt name is empty!");
            return JsonResp.getJsonRespError(HttpServletResponse.SC_BAD_REQUEST);
        }

        try {
            // 在一个事务中完成
            long dtId = digitalTwin.getId();
            if(dtId <= 0) {
                dtId = digitalTwinService.addDigitalTwin(digitalTwin);
            }else {
                digitalTwinService.updateDigitalTwin(digitalTwin);
            }
            List<DigitalTwinAttrParam> dtAttrList = parseDigitalTwinAttr(json, dtId, digitalTwin.getDigitaltwinname());
            if(!dtAttrList.isEmpty()) {
                digitalTwinAttrService.updateDigitalTwinAttr(dtAttrList);
            }
            return JsonResp.getJsonRespSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("update dt error", e);
        }

        return JsonResp.getJsonRespError();
    }

    /**
     * 查询DT详情
     * @param req
     * @param resp
     * @param json
     * @return
     */
    @PostMapping("/detail.do")
    @ResponseBody
    public Object detail(HttpServletRequest req, HttpServletResponse resp,
                        @RequestBody JSONObject json) {
        long id = json.getLong("id");
        try {
            DigitalTwin digitalTwin = digitalTwinService.getDigitalTwin(id);
            if(digitalTwin != null) {
                List<DigitalTwinAttrParam> digitalTwinAttrList = digitalTwinAttrService.getDigitalTwinAttrList(id);
                if(digitalTwinAttrList != null) {
                    // 由于当前使用的MySQL版本太低(5.1.73),不支持直接存储JSON类型，为了适配前后端的参数名一致性，查询DT详情的时候进行适配
                    List<DigitalTwinAttrResult> digitalTwinAttrResultList = new ArrayList<DigitalTwinAttrResult>(digitalTwinAttrList.size());
                    for(DigitalTwinAttrParam dtp : digitalTwinAttrList) {
                        DigitalTwinAttrResult dtr = new DigitalTwinAttrResult();
                        dtr.setId(dtp.getId());
                        dtr.setDaddyid(dtp.getDaddyid());
                        dtr.setDigitaltwinname(dtp.getDigitaltwinname());
                        dtr.setDescribemessage(dtp.getDescribemessage());
                        dtr.setUnit(dtp.getUnit());
                        dtr.setMetric(dtp.getMetric());
                        dtr.setCreatetimestamp(dtp.getCreatetimestamp());
                        dtr.setUpdatetimestamp(dtp.getUpdatetimestamp());
                        dtr.setValue(dtp.getValue());
                        dtr.setExpectedvalue(dtp.getExpectedvalue());
                        dtr.setSonname(dtp.getSonname());
                        dtr.setTagkv(JSONObject.parseObject(dtp.getTagkv()));
                        Long valueTimestamp = dtp.getValuetimestamp();
                        if(valueTimestamp == null || valueTimestamp <= 0) {
                            logger.warn(String.format("dt attr value stamp is 0! id: %s", dtp.getId()));
                            valueTimestamp = System.currentTimeMillis();
                        }
                        dtr.setValuetimestamp(valueTimestamp);
                        digitalTwinAttrResultList.add(dtr);
                    }
                    digitalTwin.setAttrList(digitalTwinAttrResultList);
                }
            }
            if(digitalTwin == null) {
                return JsonResp.getJsonRespError(HttpServletResponse.SC_NOT_FOUND);
            }
            return JsonResp.getJsonRespSuccess(digitalTwin);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("get dt error, id: %s", id, e);
        }

        return JsonResp.getJsonRespError();
    }

    @GetMapping("/commoncheckspringmvcparameter.do")
    @ResponseBody
    public Object commoncheckspringmvcparameter(HttpServletRequest req, HttpServletResponse resp,
                        @RequestParam("id") long id) {

        logger.info("id--->>>>>"+logger);

        return JsonResp.getJsonRespError();
    }

    // 更新DT名称
    /**
     * POST
     * http://localhost:8080/DigitalTwinCommonHandlerController/updatedigitaltwindaddynameByid.do
     * {"id":"1","digitaltwinname":"haloword"}
     *
     * {
     *     "code": 200,
     *     "data": {
     *         "actionFlag": true,
     *         "actionResult": 1
     *     },
     *     "message": "success"
     * }
     *
     * @param req
     * @param resp
     * @param _JSONObjectParameter
     * @return
     */
    @Deprecated
    @PostMapping("/updateDtName.do")
    @ResponseBody
    public Object update_digitaltwindaddy_nameBy_id(HttpServletRequest req,
                                                    HttpServletResponse resp,
                                                    @RequestBody JSONObject _JSONObjectParameter){

        if(null == _JSONObjectParameter){
            return JsonResp.getJsonRespError("_JSONObjectParameter is null");
        }

        Long primaryKey_id = _JSONObjectParameter.getLong("id");

        if(null == primaryKey_id || primaryKey_id <= 0 ){
            return JsonResp.getJsonRespError("null == primaryKey_id || primaryKey_id <= 0");
        }

        String digitaltwinname = _JSONObjectParameter.getString("digitaltwinname");

        if(null == digitaltwinname || digitaltwinname.trim().length()<=0){
            return JsonResp.getJsonRespError("null == digitaltwinname || digitaltwinname.trim().length()<=0");
        }

        JSONObject result = _DigitalTwinMirrorServiceImpl.update_digitaltwindaddyById(primaryKey_id,digitaltwinname);

        return JsonResp.getJsonRespSuccess(result);
    }

    // 删除DT
    @PostMapping("/delete.do")
    @ResponseBody
    public Object delete_All_digitaltwindaddy_digitaltwin(HttpServletRequest req,
                                                          HttpServletResponse resp,
                                                          @RequestBody JSONObject _JSONObjectParameter){
        if(null == _JSONObjectParameter){
            return JsonResp.getJsonRespError("_JSONObjectParameter is null");
        }

        Long primaryKey_digitaltwindaddy_id = _JSONObjectParameter.getLong("id");

        if(null == primaryKey_digitaltwindaddy_id || primaryKey_digitaltwindaddy_id <=0 ){
            return JsonResp.getJsonRespError("null == primaryKey_digitaltwindaddy_id || primaryKey_digitaltwindaddy_id <=0");
        }

        //delete_All_digitaltwindaddy_digitaltwin
        JSONObject _JSONObjectResult = _DigitalTwinMirrorServiceImpl.delete_All_digitaltwindaddy_digitaltwin(primaryKey_digitaltwindaddy_id);

        return JsonResp.getJsonRespSuccess(_JSONObjectResult);
    }



}
