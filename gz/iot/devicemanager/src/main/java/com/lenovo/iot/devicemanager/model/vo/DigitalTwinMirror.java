package com.lenovo.iot.devicemanager.model.vo;

import java.io.Serializable;

/**
 * Created by root on 2018/5/10.
 * [{
 "timestamp":1523848208232,
 "profileVersion": 1,
 "value":22.2,
 "metric":"metric name",
 "tags":{
 "deviceId":"xxxxx",
 "tagk1":"tagv1",
 …
 }
 },
 …]


 Id
 属性名称(varchar 128)
 单位(varchar 32)
 属性值（Double）
 期望值（Double）
 创建时间（long）
 更新时间（long）
 Metric(varchar 256)
 TagKV列表(varchar 1024)



 */
public class DigitalTwinMirror implements Serializable {

    private static final long serialVersionUID = 7768501636673916947L;


    private Integer id;

    //Digital Twin名称(varchar 128)
    private String digitaltwinname; //digitaltwinname

    private String createtimestamp;//创建时间

    private String updatetimestamp;//更新时间

    private int profileVersion;    //

    private Double value;          //指标值

    private String metric;         //指标

    private Double expectedvalue;//期望值

    private String bussinessid; // Metric@@@@Tagsjsonstr 唯一确定一个属性值

    public static void main(String[] args){
        String temp = "ab";
        String temp2 = "ba";
        System.out.println(temp.hashCode());
        System.out.println(temp2.hashCode());
    }


    public String getDigitaltwinname() {
        return digitaltwinname;
    }

    public void setDigitaltwinname(String digitaltwinname) {
        this.digitaltwinname = digitaltwinname;
    }

    public String getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(String createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public String getUpdatetimestamp() {
        return updatetimestamp;
    }

    public void setUpdatetimestamp(String updatetimestamp) {
        this.updatetimestamp = updatetimestamp;
    }

    public int getProfileVersion() {
        return profileVersion;
    }

    public void setProfileVersion(int profileVersion) {
        this.profileVersion = profileVersion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Double getExpectedvalue() {
        return expectedvalue;
    }

    public void setExpectedvalue(Double expectedvalue) {
        this.expectedvalue = expectedvalue;
    }

    public String getBussinessid() {
        return bussinessid;
    }

    public void setBussinessid(String bussinessid) {
        this.bussinessid = bussinessid;
    }
}
