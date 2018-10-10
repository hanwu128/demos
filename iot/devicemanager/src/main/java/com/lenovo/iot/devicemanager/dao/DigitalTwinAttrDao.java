package com.lenovo.iot.devicemanager.dao;

import com.lenovo.iot.devicemanager.model.DigitalTwinAttrParam;
import com.lenovo.iot.devicemanager.model.DigitalTwinAttrValueMeta;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DigitalTwinAttrDao {
    public Integer addDigitalTwinAttr(List<DigitalTwinAttrParam> list);
    public Integer updateDigitalTwinAttr(DigitalTwinAttrParam digitalTwinAttr);
    public Integer updateDigitalTwinAttrValue(DigitalTwinAttrValueMeta digitalTwinAttrValueMeta);
    public List<DigitalTwinAttrParam> getDigitalTwinAttrList(long daddyid);
}
