package com.lenovo.iot.devicemanager.dao;

import com.lenovo.iot.devicemanager.model.Account;
import com.lenovo.iot.devicemanager.model.DigitalTwin;
import com.lenovo.iot.devicemanager.model.DigitalTwinAttr;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by root on 2018/5/10.
 */
@Repository
public interface DigitalTwinMirrorDao {
    static final String TABLE = "digitaltwin";
    static final String FIELDS =
            "id,digitaltwinname,createtimestamp,updatetimestamp,profileVersion,metric,value,expectedvalue,bussinessid,tagkv";
    static final String FIELDS_INSERT = "digitaltwinname,createtimestamp,updatetimestamp,profileVersion,metric,value,expectedvalue,bussinessid";


    @Select("select " + FIELDS + " from " + TABLE + " where id = #{id}")
    public DigitalTwin select_digitaltwin_By_id(@Param("id") Long id);



    @Update("update " + TABLE + " set value=#{value} , valuetimestamp=#{valuetimestamp}  where metric=#{current_metric} and tagkv=#{current_tags}")
    public Integer update_digitaltwin(@Param("value") Double value,
                                      @Param("bussinessid") String bussinessid,
                                      @Param("current_metric") String current_metric,
                                      @Param("current_tags") String current_tags,
                                      @Param("valuetimestamp") Long valuetimestamp
                                      );


    @Update("update " + TABLE + " set expectedvalue=#{expectedvalue} where id=#{id}")
    public Integer update_digitaltwin_expectedvalue(@Param("expectedvalue") Double expectedvalue, @Param("id") Long id);




    @Delete("delete from " + TABLE + " where id=#{id}")
    public Integer delete_digitaltwin_ById(@Param("id") Long id);


    @Delete("delete from " + TABLE + " where daddyid=#{daddyid}")
    public Integer delete_digitaltwin_BydaddyId(@Param("daddyid") Long daddyid);


    static final String TABLE_digitaltwindaddy = "digitaltwindaddy";

    static final String FIELDS_digitaltwindaddy = "id,digitaltwinname,describemessage,createtimestamp,updatetimestamp";


    @Update("update " + TABLE_digitaltwindaddy + " set digitaltwinname=#{digitaltwinname} where id=#{id}")
    public Integer update_digitaltwindaddyById(@Param("digitaltwinname") String digitaltwinname, @Param("id") Long id);

    @Delete("delete from " + TABLE_digitaltwindaddy + " where id=#{id}")
    public Integer delete_digitaltwindaddy_ById(@Param("id") Long id);



}
