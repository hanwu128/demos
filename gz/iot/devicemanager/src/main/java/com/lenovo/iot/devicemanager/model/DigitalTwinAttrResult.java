package com.lenovo.iot.devicemanager.model;


import com.alibaba.fastjson.JSONObject;

public class DigitalTwinAttrResult extends DigitalTwinAttr {
    private JSONObject tagkv = null;

    public JSONObject getTagkv() {
        return tagkv;
    }

    public void setTagkv(JSONObject tagkv) {
        this.tagkv = tagkv;
    }
}
