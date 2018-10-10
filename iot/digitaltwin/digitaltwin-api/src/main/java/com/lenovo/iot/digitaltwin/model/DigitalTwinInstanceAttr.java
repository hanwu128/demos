package com.lenovo.iot.digitaltwin.model;

import org.apache.ibatis.type.Alias;

/**
 * Desc: DT实力属性
 * Name: com.lenovo.iot.digitaltwin.model.DigitalTwinInstanceAttr
 * Author: chench9@lenovo.com
 * Date: 2018/6/7 14:48
 **/
@Alias("DigitalTwinInstanceAttr")
public class DigitalTwinInstanceAttr extends AttrBase {
    private Long instance = null;
    private String expectvalue = null;
    private String metric = null;
    private String deviceid = null;
    private Long atime = null;
    private Long stime = null;

    public Long getInstance() {
        return instance;
    }

    public void setInstance(Long instance) {
        this.instance = instance;
    }

    public String getExpectvalue() {
        return expectvalue;
    }

    public void setExpectvalue(String expectvalue) {
        this.expectvalue = expectvalue;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public Long getAtime() {
        return atime;
    }

    public void setAtime(Long atime) {
        this.atime = atime;
    }

    public Long getStime() {
        return stime;
    }

    public void setStime(Long stime) {
        this.stime = stime;
    }
}
