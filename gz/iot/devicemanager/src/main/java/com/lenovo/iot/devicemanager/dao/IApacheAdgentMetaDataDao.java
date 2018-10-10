package com.lenovo.iot.devicemanager.dao;

import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by root on 2017/7/19.
 */
@Repository
public interface IApacheAdgentMetaDataDao {


    //doInsertTasktoNodeBo
    public void doInsertApacheAdgentMetaData(ApacheAdgentMetaData _ApacheAdgentMetaData);


    public List<ApacheAdgentMetaData> findAllApacheAdgentMetaData();


    public List<ApacheAdgentMetaData> findApacheAdgentMetaDataByParameter(ApacheAdgentMetaData _ApacheAdgentMetaData);


    public void doUpdateApacheAdgentMetaData(ApacheAdgentMetaData _ApacheAdgentMetaData);

    //findApacheAdgentMetaDataByParameterdeviceid
    public List<ApacheAdgentMetaData> findApacheAdgentMetaDataByParameterdeviceid(ApacheAdgentMetaData _ApacheAdgentMetaData);

}
