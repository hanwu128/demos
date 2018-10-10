package com.lenovo.iot.devicemanager.service;


import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;
import com.lenovo.iot.devicemanager.model.bo.App;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * Created by root on 2017/7/19.
 */

public interface IAppService {

    public String doInsertApp(App _App);

    public List<App> findAppByParameter(App _App);
    public Integer findAppCountByParameter(App _App);


}
