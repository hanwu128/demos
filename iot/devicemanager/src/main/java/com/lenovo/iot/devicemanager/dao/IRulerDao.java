package com.lenovo.iot.devicemanager.dao;

import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;
import com.lenovo.iot.devicemanager.model.bo.RulerVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by root on 2017/7/19.
 */
@Repository
public interface IRulerDao {


    //doInsertTasktoNodeBo
    public void doInsertruler(RulerVO _RulerVO);


    public List<RulerVO> findRulerByParameter(RulerVO _RulerVO);


//    public List<ApacheAdgentMetaData> findAllApacheAdgentMetaData();
//
//
//    public List<ApacheAdgentMetaData> findApacheAdgentMetaDataByParameter(ApacheAdgentMetaData _ApacheAdgentMetaData);
//
//
//    public void doUpdateApacheAdgentMetaData(ApacheAdgentMetaData _ApacheAdgentMetaData);
//
//    //findApacheAdgentMetaDataByParameterdeviceid
//    public List<ApacheAdgentMetaData> findApacheAdgentMetaDataByParameterdeviceid(ApacheAdgentMetaData _ApacheAdgentMetaData);

}
