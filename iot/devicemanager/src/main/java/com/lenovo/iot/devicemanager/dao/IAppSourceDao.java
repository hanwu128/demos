package com.lenovo.iot.devicemanager.dao;

import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;
import com.lenovo.iot.devicemanager.model.bo.AppSource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by root on 2017/7/19.
 */
@Repository
public interface IAppSourceDao {


    //doInsertApp
    public void doInsertAppSource(AppSource _AppSource);

    //findAppSourceByParameter
    public List<AppSource> findAppSourceByParameter(AppSource _AppSource);


}
