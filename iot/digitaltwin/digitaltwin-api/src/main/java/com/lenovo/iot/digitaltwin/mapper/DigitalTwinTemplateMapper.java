package com.lenovo.iot.digitaltwin.mapper;

import com.lenovo.iot.digitaltwin.model.DigitalTwinTemplate;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.LongTypeHandler;

import java.util.List;

@Mapper
public interface DigitalTwinTemplateMapper {
    String table = "dt_tpl";
    String fileds = "id,name,desp,label,ctime,mtime";

    @Select("select " + fileds + " from " + table  + " limit #{start},#{offset}")
    // 在接口映射器上配置映射规则
    @Results({
            @Result(property = "id", column = "id", typeHandler = LongTypeHandler.class)
    })
    List<DigitalTwinTemplate> getDigitalTwinTemplateList(@Param("start") int start, @Param("offset") int offset);

    @Select("select " + fileds + " from " + table  + " where id = #{id}")
    DigitalTwinTemplate getDigitalTwinTemplate(@Param("id") long id);
}
