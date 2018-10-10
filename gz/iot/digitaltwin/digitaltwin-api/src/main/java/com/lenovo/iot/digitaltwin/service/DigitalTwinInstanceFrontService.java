package com.lenovo.iot.digitaltwin.service;

import com.lenovo.iot.digitaltwin.exception.UniqueRecordExeception;
import com.lenovo.iot.digitaltwin.model.DigitalTwinInstance;
import com.lenovo.iot.digitaltwin.model.DigitalTwinInstanceAttr;
import com.lenovo.iot.digitaltwin.model.DigitalTwinTemplate;
import com.lenovo.iot.digitaltwin.util.AttrTypeEnum;
import com.lenovo.iot.digitaltwin.util.DBUtil;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DigitalTwinInstanceFrontService extends DBServiceBase {
    private static final Logger logger = LoggerFactory.getLogger(DigitalTwinInstanceFrontService.class);
    private static final String STATEMENT_INSTANCE = "com.lenovo.iot.digitaltwin.ist.front";
    private static final String STATEMENT_INSTANCE_ATTR = "com.lenovo.iot.digitaltwin.istattr.front";

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
     * 查询DT实例列表
     *
     * @param start
     * @param offset
     * @param name
     * @return
     */
    public List<DigitalTwinInstance> getDigitalTwinInstanceList(int start, int offset, String name, String uid) {
        name = DBUtil.like(name);
        Map<String, Object> params = DBUtil.buildNamePageParam(name, start, offset, uid);
        return sqlSession.selectList(getStatement("getList"), params);
    }

    /**
     * 查询DT实例
     *
     * @param id
     * @return
     */
    public DigitalTwinInstance getDigitalTwinInstance(long id) {
        return sqlSession.selectOne(getStatement("getById"), id);
    }

    /**
     * 查询DT实例详情
     *
     * @param id
     * @return
     */
    public DigitalTwinInstance getDigitalTwinInstanceDetail(long id) {
        return sqlSession.selectOne(getStatement("getDetailById"), id);
    }

    /**
     * 根据实例名称查询DT实例
     *
     * @param name
     * @return
     */
    public DigitalTwinInstance getDigitalTwinInstanceByName(String name) {
        return sqlSession.selectOne(getStatement("getByName"), name);
    }

    /**
     * 添加DT实例
     *
     * @param dtInstance
     * @return
     */
    @Transactional
    public Integer addDigitalTwinInstance(DigitalTwinInstance dtInstance, List<DigitalTwinInstanceAttr> attrList) throws SQLException {
        // 检查是否存在同名物实例
        DigitalTwinInstance digitalTwinInstance = sqlSession.selectOne(getStatement("getByName"), dtInstance.getName());
        if (digitalTwinInstance != null) {
            logger.error("add dt instance exists with name: {}", dtInstance.getName());
            throw new UniqueRecordExeception("add dt instance exists with name: " + dtInstance.getName());
        }
        int rows = sqlSession.insert(getStatement("add"), dtInstance);
        if (rows <= 0) {
            logger.error("add dt instance error: {}", dtInstance);
            throw new SQLException("add dt instance error");
        }

        for (DigitalTwinInstanceAttr attr : attrList) {
            attr.setInstance(dtInstance.getId());
        }
        rows = sqlSession.insert(getStatement(STATEMENT_INSTANCE_ATTR, "add"), attrList);
        if (rows <= 0) {
            logger.error("add dt instance attr error: {}", attrList);
            throw new SQLException("add dt instance attr error");
        }

        return rows;
    }

    /**
     * 更新DT实例
     *
     * @param paramInstance
     * @return
     */
    @Transactional
    public Integer updateDigitalTwinInstance(DigitalTwinInstance paramInstance, List<DigitalTwinInstanceAttr> attrList, DigitalTwinInstance dbInstance) throws SQLException {
        // 检查是否存在同名物实例
        DigitalTwinInstance digitalTwinInstance = sqlSession.selectOne(getStatement("getByName"), paramInstance.getName());
        if (digitalTwinInstance != null && paramInstance.getId().longValue() != digitalTwinInstance.getId().longValue()) {
            logger.error("update dt instance exists with name: {}", paramInstance.getName());
            throw new UniqueRecordExeception("update dt instance exists with name: " + paramInstance.getName());
        }
        if (!(paramInstance.getName().equals(dbInstance.getName())) || !(paramInstance.getDesp().equals(dbInstance.getDesp()))) {
            // 更新DT实例名称，描述
            int rows = sqlSession.update(getStatement(STATEMENT_INSTANCE, "update"), paramInstance);
            if (rows <= 0) {
                logger.error("update dt instance error: {}", paramInstance);
                throw new SQLException("update dt instance error !");
            }
        }
        if (paramInstance.getTpl().longValue() != dbInstance.getTpl().longValue()) {
            // 先删除之前的DT属性，再添加新的模板属性
            int rows = sqlSession.delete(getStatement(STATEMENT_INSTANCE_ATTR, "delete"), paramInstance.getId());
            if (rows <= 0) {
                logger.error("delete dt instance attr error: {}", paramInstance);
                throw new SQLException("delete dt instance attr error !");
            }

            rows = sqlSession.insert(getStatement(STATEMENT_INSTANCE_ATTR, "add"), attrList);
            if (rows <= 0) {
                logger.error("add dt instance attr error: {}", attrList);
                throw new SQLException("add dt instance attr error !");
            }
        }

        return 1;
    }

    /**
     * 更新DT实例期望值
     *
     * @param attrList
     * @return
     * @throws SQLException
     */
    @Transactional
    public List<Long> updateDigitalTwinInstanceExpectValue(List<DigitalTwinInstanceAttr> attrList) throws SQLException {
        List<Long> idList = new ArrayList<Long>();
        for (DigitalTwinInstanceAttr attr : attrList) {
            DigitalTwinInstanceAttr digitalTwinInstanceAttr = getDigitalTwinInstanceAttr(attr.getId());
            if (!AttrTypeEnum.TYPE_STATE.toString().equalsIgnoreCase(digitalTwinInstanceAttr.getAttrtype())) {
                logger.error("dt instance attribute type error is not state: {}", attr);
                throw new SQLException("dt instance attribute type error is not state");
            }
            if (sqlSession.update(getStatement(STATEMENT_INSTANCE_ATTR, "update"), attr) > 0) {
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
     * 删除DT实例
     *
     * @param istIdList
     * @return
     */
    @Transactional
    public Integer deleteDigitalTwinInstance(List<Long> istIdList) throws SQLException {
        int rows = sqlSession.delete(getStatement("delete"), istIdList);
        if (rows <= 0) {
            logger.error("delete dt instance error");
            throw new SQLException("delete dt instance error");
        }
        return rows;
    }

    /**
     * 查询DT实例属性详情列表
     *
     * @param id
     * @return
     */
    public DigitalTwinInstanceAttr getDigitalTwinInstanceAttr(long id) {
        List<Long> list = new ArrayList<Long>();
        list.add(id);
        return sqlSession.selectOne(getStatement(STATEMENT_INSTANCE_ATTR, "getListById"), list);
    }

    public List<DigitalTwinInstance> getDigitalTwinInstanceByListId(List<Long> ids) {
        return sqlSession.selectList(getStatement("getByListId"), ids);
    }

}
