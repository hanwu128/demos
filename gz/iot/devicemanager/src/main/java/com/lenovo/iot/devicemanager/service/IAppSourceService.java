package com.lenovo.iot.devicemanager.service;

import com.lenovo.iot.devicemanager.model.bo.App;
import com.lenovo.iot.devicemanager.model.bo.AppSource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by root on 2017/8/18.
 */

public interface IAppSourceService {

    public String doInsertAppSource(AppSource _AppSource);

    public List<AppSource> findAppSourceByParameter(AppSource _AppSource);

}
