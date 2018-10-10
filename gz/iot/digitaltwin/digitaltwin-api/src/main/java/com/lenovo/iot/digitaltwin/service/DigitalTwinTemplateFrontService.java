package com.lenovo.iot.digitaltwin.service;

import com.lenovo.iot.digitaltwin.exception.NullRecordExeception;
import com.lenovo.iot.digitaltwin.exception.UniqueAttrRecordExeception;
import com.lenovo.iot.digitaltwin.exception.UniqueRecordExeception;
import com.lenovo.iot.digitaltwin.model.DigitalTwinInstanceAttr;
import com.lenovo.iot.digitaltwin.model.DigitalTwinTemplate;
import com.lenovo.iot.digitaltwin.model.DigitalTwinTemplateAttr;
import com.lenovo.iot.digitaltwin.util.CommonUtil;
import com.lenovo.iot.digitaltwin.util.DBUtil;
import com.lenovo.iot.digitaltwin.util.JsonResp;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DigitalTwinTemplateFrontService extends DBServiceBase {
    private static final Logger logger = LoggerFactory.getLogger(DigitalTwinTemplateFrontService.class);
    private static final String STATEMENT_TPL = "com.lenovo.iot.digitaltwin.tpl.front";
    private static final String STATEMENT_TPL_ATTR = "com.lenovo.iot.digitaltwin.tplattr.front";

    @Autowired
    private SqlSession sqlSession;

    @Override
    public String getStatement(String id) {
        return STATEMENT_TPL + "." + id;
    }

    /**
     * 查询DT模板总数
     *
     * @param name
     * @return
     */
    public Integer getDigitalTwinTemplateTotal(String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", DBUtil.like(name));
        return sqlSession.selectOne(getStatement("getTotal"), params);
    }

    /**
     * 查询DT模板列表
     *
     * @param start
     * @param offset
     * @return
     */
    public List<DigitalTwinTemplate> getDigitalTwinTemplateList(int start, int offset, String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("start", start);
        params.put("offset", offset);
        params.put("name", DBUtil.like(name));
        return sqlSession.selectList(getStatement("getList"), params);
    }

    /**
     * 查询DT模板详情
     *
     * @param id
     * @return
     */
    public DigitalTwinTemplate getDigitalTwinTemplateById(long id) {
        return sqlSession.selectOne(getStatement("getById"), id);
    }

    /**
     * 根据模板名称查询DT模板
     *
     * @param name
     * @return
     */
    public DigitalTwinTemplate getDigitalTwinTemplateByName(String name) {
        return sqlSession.selectOne(getStatement("getByName"), name);
    }

    /**
     * 添加DT模板
     *
     * @param dtTpl
     * @param attrList
     * @return
     * @throws SQLException
     */
    @Transactional
    public Integer addDigitalTwinTemplate(DigitalTwinTemplate dtTpl, List<DigitalTwinTemplateAttr> attrList) throws SQLException {
        // 检查是否存在同名物模板
        DigitalTwinTemplate dtt = sqlSession.selectOne(getStatement("getByName"), dtTpl.getName());
        if (dtt != null) {
            logger.error("dt tpl exists with name: {}", dtTpl.getName());
            throw new UniqueRecordExeception("dt tpl exists with name: " + dtTpl.getName());
        }

        Integer rows = sqlSession.insert(getStatement("add"), dtTpl);
        if (rows <= 0) {
            logger.error("add dt tpl error: {}", dtTpl);
            return rows;
        }

        if (attrList == null || attrList.isEmpty()) {
            logger.warn("add dt tpl attr is empty: {}", dtTpl);
            return rows;
        }
        for (DigitalTwinTemplateAttr attr : attrList) {
            attr.setTpl(dtTpl.getId());
        }
        rows = sqlSession.insert(getStatement(STATEMENT_TPL_ATTR, "add"), attrList);
        if (rows <= 0) {
            logger.error("add dt tpl attr error: {}, {}", dtTpl, attrList);
            throw new SQLException("add dt tpl attr error");
        }

        return rows;
    }

    /**
     * 编辑DT模板
     *
     * @param dtTpl
     * @return
     */
    @Transactional
    public Integer updateDigitalTwinTemplate(DigitalTwinTemplate dtTpl,
                                             List<DigitalTwinTemplateAttr> updateAttrList, List<DigitalTwinTemplateAttr> addAttrList,
                                             List<Long> deleteAttrIdList) throws SQLException {
        // 检查是否存在同名物模板
        DigitalTwinTemplate dtt = sqlSession.selectOne(getStatement("getByName"), dtTpl.getName());
        if (dtt != null && dtTpl.getId().longValue() != dtt.getId().longValue()) {
            logger.error("update dt tpl exists with name: {}", dtTpl.getName());
            throw new UniqueRecordExeception("update dt tpl exists with name: " + dtTpl.getName());
        }
        int rows = sqlSession.update(getStatement("update"), dtTpl);
        if (rows <= 0) {
            logger.error("update dt tpl error: {}", dtTpl);
            throw new SQLException("update dt tpl error");
        }
        //获取DT模板中所有属性
        DigitalTwinTemplate digitalTwinTemplate = sqlSession.selectOne(getStatement("getById"), dtTpl.getId());
        if (digitalTwinTemplate == null) {
            logger.error("update dt tpl is null: {}", dtTpl);
            throw new NullRecordExeception("update dt tpl is null:" + dtTpl);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        for (DigitalTwinTemplateAttr dtAttr : digitalTwinTemplate.getAttr()) {
            map.put(dtAttr.getName(), dtAttr.getId());
        }
        for (DigitalTwinTemplateAttr updateAttr : updateAttrList) {
            if (!StringUtils.isEmpty(updateAttr.getName())) {
                if (map.containsKey(updateAttr.getName()) &&
                        updateAttr.getId().longValue() != CommonUtil.str2Long(String.valueOf(map.get(updateAttr.getName()))).longValue()) {
                    logger.error("update dt tpl attr error: {}", updateAttrList);
                    throw new UniqueAttrRecordExeception("update dt tpl attr exists with name: " + updateAttr.getName());
                }
                map.put(updateAttr.getName(), updateAttr.getId());
            }
        }
        for (DigitalTwinTemplateAttr addAttr : addAttrList) {
            if (!StringUtils.isEmpty(addAttr.getName())) {
                if (map.containsKey(addAttr.getName())) {
                    logger.error("update dt tpl attr error: {}", addAttr);
                    throw new UniqueAttrRecordExeception("update dt tpl attr exists with name: " + addAttr.getName());
                }
                map.put(addAttr.getName(), addAttr.getId());
            }
        }
        int count = 0;
        // 删除属性
        if (deleteAttrIdList != null && !deleteAttrIdList.isEmpty()) {
            count += sqlSession.delete(getStatement(STATEMENT_TPL_ATTR, "delete"), deleteAttrIdList);
            if (deleteAttrIdList.size() != count) {
                logger.error("delete dt tpl attr error: {}", deleteAttrIdList);
                throw new SQLException("delete dt tpl attr error");
            }
        }
        // 添加新属性
        if (addAttrList != null && !addAttrList.isEmpty()) {
            count = 0;
            count += sqlSession.insert(getStatement(STATEMENT_TPL_ATTR, "add"), addAttrList);
            if (addAttrList.size() != count) {
                logger.error("add dt tpl attr error: {}", addAttrList);
                throw new SQLException("add dt tpl attr error");
            }
        }
        // 更新已存在属性
        if (updateAttrList != null && !updateAttrList.isEmpty()) {
            count = 0;
            for (DigitalTwinTemplateAttr attr : updateAttrList) {
                count += sqlSession.update(getStatement(STATEMENT_TPL_ATTR, "update"), attr);
            }
            if (updateAttrList.size() != count) {
                logger.error("update dt tpl attr error: {}", updateAttrList);
                throw new SQLException("update dt tpl attr error");
            }
        }
        return count;
    }

    /**
     * 删除DT模板
     *
     * @param tplIdList
     * @return
     */
    @Transactional
    public Integer deleteDigitalTwinTemplate(List<Long> tplIdList) {
        return sqlSession.delete(getStatement("delete"), tplIdList);
    }

}
