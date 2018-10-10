package com.lenovo.iot.digitaltwin.mapper;

import com.lenovo.iot.digitaltwin.model.DigitalTwinTemplateAttr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DigitalTwinTemplateAttrMapper {
    String table = "dt_tpl_attr";
    String fileds = "id,tpl,name,desp,datatype,value,ctime,mtime";

    @Select("select " + fileds + " from " + table + " where tpl = #{tpl}")
    List<DigitalTwinTemplateAttr> getDigitalTwinTemplateAttrList(@Param("tpl") long tpl);
}
