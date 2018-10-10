package com.lenovo.iot.devicemanager.dao;

import com.lenovo.iot.devicemanager.model.DigitalTwin;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DigitalTwinDao {
    static final String TABLE = "digitaltwindaddy";
    static final String FIELDS = "id,digitaltwinname,describemessage,createtimestamp,updatetimestamp";
    static final String FIELDS_INSERT = "digitaltwinname,describemessage,createtimestamp,updatetimestamp";

    @Insert("insert into " + TABLE + "(" + FIELDS_INSERT + ") values(#{digitaltwinname},#{describemessage},now(),now())")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    public Integer addDigitalTwin(DigitalTwin digitalTwin);

    @Select("select count(1) as c from " + TABLE + " where digitaltwinname like #{name}")
    public Integer countByName(@Param("name") String name);

    @Select("select " + FIELDS + " from " + TABLE + " where digitaltwinname like #{name} limit #{start}, #{limit}")
    public List<DigitalTwin> getDigitalTwinList(@Param("name") String name,
                                                @Param("start") int start,
                                                @Param("limit") int limit);

    @Update("update " + TABLE + " set digitaltwinname=#{digitaltwinname},describemessage=#{describemessage},updatetimestamp=now() where id=#{id}")
    public Integer updateDigitalTwin(DigitalTwin digitalTwin);

    @Select("select " + FIELDS + " from " + TABLE + " where id=#{id}")
    public DigitalTwin getDigitalTwin(@Param("id") long id);
}
