package com.lenovo.iot.devicemanager.service;

import com.lenovo.iot.devicemanager.dao.DigitalTwinDao;
import com.lenovo.iot.devicemanager.model.DigitalTwin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DigitalTwinService {

    @Autowired
    private DigitalTwinDao digitalTwinDao;

    /**
     * 添加镜像，返回数据库主键id
     * @param digitalTwin
     * @return
     */
    public long addDigitalTwin(DigitalTwin digitalTwin) {
        int rows = digitalTwinDao.addDigitalTwin(digitalTwin);
        if(rows > 0) {
            return digitalTwin.getId();
        }
        return 0;
    }

    /**
     * 查询总数
     * @return
     */
    public Integer count(String name) {
        name = nameLike(name);
        return digitalTwinDao.countByName(name);
    }

    /**
     * 根据名称查询DT列表
     * @param name
     * @param start
     * @param limit
     * @return
     */
    public List<DigitalTwin> getDigitalTwinList(String name, int start, int limit) {
        name = nameLike(name);
        return digitalTwinDao.getDigitalTwinList(name, start, limit);
    }

    private String nameLike(String name) {
        if(name == null || "".equals(name.trim())) {
            return "%";
        }
        return new StringBuilder().append("%").append(name).append("%").toString();
    }

    /**
     * 更新DT
     * @param digitalTwin
     * @return
     */
    public Integer updateDigitalTwin(DigitalTwin digitalTwin) {
        return digitalTwinDao.updateDigitalTwin(digitalTwin);
    }

    /**
     * 查询DT
     * @param id
     * @return
     */
    public DigitalTwin getDigitalTwin(long id) {
        return digitalTwinDao.getDigitalTwin(id);
    }
}
