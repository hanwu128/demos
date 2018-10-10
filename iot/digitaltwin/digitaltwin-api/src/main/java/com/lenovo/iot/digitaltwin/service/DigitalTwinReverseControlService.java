package com.lenovo.iot.digitaltwin.service;

import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.digitaltwin.model.DigitalTwinInstance;
import com.lenovo.iot.digitaltwin.model.DigitalTwinInstanceAttr;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/********************************************************************
 反控设备消息格式：

 Topic:
 $LEAP/set/{deviceId}/stream

 Message:
 {
    "desired": {
    "light": "on"
        ...
    }
 }
********************************************************************/

/**
 * @Desc: 设备反控
 * @Name: com.lenovo.iot.digitaltwin.service.DigitalTwinCommonService
 * @Author: chench9@lenovo.com
 * @Date: 2018/7/12
 */
@Service
public class DigitalTwinReverseControlService extends DBServiceBase {
    private static final Logger logger = LoggerFactory.getLogger(DigitalTwinReverseControlService.class);
    private static final String STATEMENT_INSTANCE_ATTR = "com.lenovo.iot.digitaltwin.rc";
    private static final String TOPIC ="$LEAP/set/{deviceId}/stream";

    @Autowired
    private SqlSession sqlSession;

    @Override
    public String getStatement(String id) {
        return STATEMENT_INSTANCE_ATTR + "." + id;
    }

    @Autowired
    private MQTTService mqttService;

    /**
     * 查询DT实例
     * @param id
     * @return
     */
    public DigitalTwinInstance getDigitalTwinInstanceDetailById(long id) {
        return sqlSession.selectOne(getStatement("getIstDetailById"), id);
    }

    /**
     * 查询DT实例属性列表
     * @param idList
     * @return
     */
    public List<DigitalTwinInstanceAttr> getDigitalTwinInstanceAttrList(List<Long> idList) {
        return sqlSession.selectList(getStatement("getIstAttrListById"), idList);
    }

    /**
     * 反控设备
     * @param attrList
     * @throws Exception
     */
    @Transactional
    public void reverseDevice(List<DigitalTwinInstanceAttr> attrList) throws Exception {
        List<Long> idList = updateDigitalTwinInstanceAttrExpectValue(attrList);
        sendControlDeviceMessage(idList);
    }

    /**
     * 更新DT实例属性期望值
     * @param attrList
     * @return
     * @throws SQLException
     */
    public List<Long> updateDigitalTwinInstanceAttrExpectValue(List<DigitalTwinInstanceAttr> attrList) throws SQLException {
        List<Long> idList = new ArrayList<Long>();
        for (DigitalTwinInstanceAttr attr : attrList) {
            if(sqlSession.update(getStatement(STATEMENT_INSTANCE_ATTR, "updateIstAttrExpectValue"), attr) > 0) {
                idList.add(attr.getId());
            }
        }
        if (idList.size() != attrList.size()) {
            throw new SQLException("update dt instance attr error");
        }

        // 将更新期望值成功的属性ID列表返回
        return idList;
    }

    /**
     * 发送反控设备消息
     * @param attrIdList
     */
    public void sendControlDeviceMessage(List<Long> attrIdList) throws IOException {
        if(attrIdList == null || attrIdList.isEmpty()) {
            return;
        }
        List<DigitalTwinInstanceAttr> attrList = getDigitalTwinInstanceAttrList(attrIdList);
        if(attrList == null || attrList.isEmpty()) {
            return;
        }

        for(DigitalTwinInstanceAttr attr : attrList) {
            if(attr.getDeviceid() == null || "".equals(attr.getDeviceid().trim())) {
                logger.error("dt instance attribute device id is empty: {}", attr.getId());
                continue;
            }
            if(attr.getMetric() == null || "".equals(attr.getMetric().trim())) {
                logger.error("dt instance attribute metric is empty: {}", attr.getId());
                continue;
            }
            if(attr.getExpectvalue() == null || "".equals(attr.getExpectvalue().trim())) {
                logger.error("dt instance attribute expect value is empty: {}", attr.getId());
                continue;
            }

            String tp = TOPIC.replace("{deviceId}", attr.getDeviceid());
            JSONObject desired = new JSONObject();
            desired.put(attr.getMetric(), attr.getExpectvalue());
            JSONObject message = new JSONObject();
            message.put("desired", desired);
            if(logger.isDebugEnabled()) {
                logger.debug("reverse control topic: {},  message: {}", tp, message.toJSONString());
            }
            if(!mqttService.publish(tp, message.toJSONString())) {
                logger.error("reverse control device failed: {}, {}, {}, {}", attr.getId(), attr.getDeviceid(), attr.getMetric(), attr.getExpectvalue());
                throw new IOException(String.format("mqtt publish message failed: %s, %s, %s, %s", attr.getId(), attr.getDeviceid(), attr.getMetric(), attr.getExpectvalue()));
            }
        }
    }

}
