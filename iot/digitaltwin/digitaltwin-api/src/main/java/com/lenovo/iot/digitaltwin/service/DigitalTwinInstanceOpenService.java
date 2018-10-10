package com.lenovo.iot.digitaltwin.service;

import com.lenovo.iot.digitaltwin.model.DigitalTwinInstance;
import com.lenovo.iot.digitaltwin.model.DigitalTwinInstanceAttr;
import com.lenovo.iot.digitaltwin.util.DBUtil;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DigitalTwinInstanceOpenService extends DBServiceBase {
    private static final Logger logger = LoggerFactory.getLogger(DigitalTwinInstanceOpenService.class);
    private static final String STATEMENT_INSTANCE = "com.lenovo.iot.digitaltwin.ist.open";
    private static final String STATEMENT_INSTANCE_ATTR = "com.lenovo.iot.digitaltwin.istattr.open";

    @Autowired
    private SqlSession sqlSession;

    @Override
    public String getStatement(String id) {
        return STATEMENT_INSTANCE + "." + id;
    }

    /**
     * 查询DT实例总数
     *
     * @param name
     * @return
     */
    public Integer getDigitalTwinInstanceTotal(String name) {
        name = DBUtil.like(name);
        Map<String, Object> params = DBUtil.buildNameParam(name);
        return sqlSession.selectOne(getStatement("getTotal"), params);
    }

    /**
     * 查询DT实例分页列表
     *
     * @param start
     * @param offset
     * @param name
     * @return
     */
    public List<DigitalTwinInstance> getDigitalTwinInstanceList(int start, int offset, String name) {
        name = DBUtil.like(name);
        Map<String, Object> params = DBUtil.buildNamePageParam(name, start, offset,null);
        return sqlSession.selectList(getStatement("getList"), params);
    }

    /**
     * 查询DT实例详情
     *
     * @param idsList
     * @return
     */
    public List<DigitalTwinInstance> getDigitalTwinInstanceDetail(List<String> idsList) {
        return sqlSession.selectList(getStatement("getDetailById"), idsList);
    }

    /**
     * 根据ID、属性类型查询DT实例属性
     * @param id
     * @return
     */
    public DigitalTwinInstanceAttr getAttrById(long id) {
        return sqlSession.selectOne(getStatement(STATEMENT_INSTANCE_ATTR, "getById"), id);
    }

}
