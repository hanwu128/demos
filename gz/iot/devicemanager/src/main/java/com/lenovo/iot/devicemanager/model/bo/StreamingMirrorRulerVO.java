package com.lenovo.iot.devicemanager.model.bo;

import java.io.Serializable;

/**
 * Created by root on 2017/8/29.
 */
public class StreamingMirrorRulerVO implements Serializable {

    private static final long serialVersionUID = -811133732792272771L;

    private Long id;

    private Long streamingdevappbriprimarykey;

    private Long streamingid;

    private Long deviceid;

//    private Long appid;

    private String rulergroupid;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStreamingdevappbriprimarykey() {
        return streamingdevappbriprimarykey;
    }

    public void setStreamingdevappbriprimarykey(Long streamingdevappbriprimarykey) {
        this.streamingdevappbriprimarykey = streamingdevappbriprimarykey;
    }

    public Long getStreamingid() {
        return streamingid;
    }

    public void setStreamingid(Long streamingid) {
        this.streamingid = streamingid;
    }

    public Long getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Long deviceid) {
        this.deviceid = deviceid;
    }

//    public Long getAppid() {
//        return appid;
//    }
//
//    public void setAppid(Long appid) {
//        this.appid = appid;
//    }

    public String getRulergroupid() {
        return rulergroupid;
    }

    public void setRulergroupid(String rulergroupid) {
        this.rulergroupid = rulergroupid;
    }
}
