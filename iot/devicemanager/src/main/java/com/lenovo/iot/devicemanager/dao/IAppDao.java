package com.lenovo.iot.devicemanager.dao;

import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;
import com.lenovo.iot.devicemanager.model.bo.App;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by root on 2017/7/19.
 */
@Repository
public interface IAppDao {

    //doInsertApp
    public void doInsertApp(App _App);

    //findAppByParameter

    public List<App> findAppByParameter(App _App);
    public Integer findAppCountByParameter(App _App);


}
