package com.lenovo.iot.devicemanager.service;


import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;

import java.util.List;

/**
 * Created by root on 2017/7/19.
 */
public interface IApacheAdgentMetaDataService {

    //doInsertTasktoNodeBo
    public void doInsertApacheAdgentMetaData(ApacheAdgentMetaData _ApacheAdgentMetaData);

    public List<ApacheAdgentMetaData> findAllApacheAdgentMetaData();

    public List<ApacheAdgentMetaData> findApacheAdgentMetaDataByParameter_deviceid(ApacheAdgentMetaData _ApacheAdgentMetaData);

    public void  doUpdateApacheAdgentMetaData(ApacheAdgentMetaData _ApacheAdgentMetaData);

}
