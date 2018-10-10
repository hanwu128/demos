package com.lenovo.iot.digitaltwin.service;

import com.lenovo.iot.digitaltwin.model.DigitalTwinInstance;
import com.lenovo.iot.digitaltwin.model.DigitalTwinInstanceAttr;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DigitalTwinInstanceStreamService extends DBServiceBase {
    private static final Logger logger = LoggerFactory.getLogger(DigitalTwinInstanceStreamService.class);
    private static final String STATEMENT_INSTANCE = "com.lenovo.iot.digitaltwin.ist.stream";
    private static final String STATEMENT_INSTANCE_ATTR = "com.lenovo.iot.digitaltwin.istattr.stream";

    @Autowired
    private SqlSession sqlSession;

    @Override
    public String getStatement(String id) {
        return STATEMENT_INSTANCE + "." + id;
    }

    /**
     * 查询DT实例概要列表
     * @return
     */
    public List<DigitalTwinInstance> getDigitalTwinInstanceListSummary() {
        return sqlSession.selectList(getStatement("getDtIstListSummary"));
    }

    /**
     * 查询DT实例详情列表
     * @return
     */
    public List<DigitalTwinInstance> getDigitalTwinInstanceListDetail() {
        return sqlSession.selectList(getStatement("getDtIstListDetail"));
    }

    /**
     * 查询DT实例详情
     * @param id
     * @return
     */
    public DigitalTwinInstance getDigitalTwinInstanceDetail(long id) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        return sqlSession.selectOne("getDtIstDetail", param);
    }


    /**
     * 批量更新DT实例属性
     * @param attrList
     * @return
     */
    @Transactional
    public int updateDigitalTwinInstanceAttrList(List<DigitalTwinInstanceAttr> attrList) {
        int rows = 0;
        for(DigitalTwinInstanceAttr attr : attrList) {
            rows += sqlSession.update(getStatement(STATEMENT_INSTANCE_ATTR, "updateDtIstAttr"), attr);
        }
        return rows;
    }

    /**
     * 更新物实例锁状态
     * @param instance
     * @return
     */
    public int updateDigitalTwinInstanceLock(DigitalTwinInstance instance) {
        return sqlSession.update(getStatement("updateLock"), instance);
    }

}
