package com.lenovo.iot.devicemanager.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.List;

public class DigitalTwin {
    private Long id = 0L;
    private String digitaltwinname = "";
    private String describemessage = "";
    private Date createtimestamp = null;
    private Date updatetimestamp = null;
    private List<DigitalTwinAttrResult> attrList = null;
    private String tagkv = null;
    private String metric;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDigitaltwinname() {
        return digitaltwinname;
    }

    public void setDigitaltwinname(String digitaltwinname) {
        this.digitaltwinname = digitaltwinname;
    }

    public String getDescribemessage() {
        return describemessage;
    }

    public void setDescribemessage(String describemessage) {
        this.describemessage = describemessage;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public Date getUpdatetimestamp() {
        return updatetimestamp;
    }

    public void setUpdatetimestamp(Date updatetimestamp) {
        this.updatetimestamp = updatetimestamp;
    }

    public List<DigitalTwinAttrResult> getAttrList() {
        return attrList;
    }

    public void setAttrList(List<DigitalTwinAttrResult> attrList) {
        this.attrList = attrList;
    }


    public String getTagkv() {
        return tagkv;
    }

    public void setTagkv(String tagkv) {
        this.tagkv = tagkv;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }
}
