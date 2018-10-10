package com.lenovo.iot.devicemanager.model.bo;

import java.io.Serializable;

/**
 * Created by root on 2017/8/24.
 */
public class StreamingMirrorAppsourceVO implements Serializable {

    private static final long serialVersionUID = -811133735796272771L;

    private Long id;

    private Long streamingdevappbriprimarykey;

//    private Long streamingid;

//    private Long deviceid;

//    private Long appid;

//    private Long appsourceid;

    private String mapdisplayname;

    private String mapname;

//    private String appname;
//
    private String sourcedisplayname;

    private String sourcedefault;

    private String sourcedatatype;

    private String sourceunit;

    private String sourcename;

    private String mapnameshow;

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

//    public Long getStreamingid() {
//        return streamingid;
//    }
//
//    public void setStreamingid(Long streamingid) {
//        this.streamingid = streamingid;
//    }
//
//    public Long getDeviceid() {
//        return deviceid;
//    }
//
//    public void setDeviceid(Long deviceid) {
//        this.deviceid = deviceid;
//    }

//    public Long getAppid() {
//        return appid;
//    }
//
//    public void setAppid(Long appid) {
//        this.appid = appid;
//    }

//    public Long getAppsourceid() {
//        return appsourceid;
//    }
//
//    public void setAppsourceid(Long appsourceid) {
//        this.appsourceid = appsourceid;
//    }

    public String getMapdisplayname() {
        return mapdisplayname;
    }

    public void setMapdisplayname(String mapdisplayname) {
        this.mapdisplayname = mapdisplayname;
    }

    public String getMapname() {
        return mapname;
    }

    public void setMapname(String mapname) {
        this.mapname = mapname;
    }
//
//    public String getAppname() {
//        return appname;
//    }
//
//    public void setAppname(String appname) {
//        this.appname = appname;
//    }
//
    public String getSourcedisplayname() {
        return sourcedisplayname;
    }

    public void setSourcedisplayname(String sourcedisplayname) {
        this.sourcedisplayname = sourcedisplayname;
    }

    public String getSourcedefault() {
        return sourcedefault;
    }

    public void setSourcedefault(String sourcedefault) {
        this.sourcedefault = sourcedefault;
    }

    public String getSourcedatatype() {
        return sourcedatatype;
    }

    public void setSourcedatatype(String sourcedatatype) {
        this.sourcedatatype = sourcedatatype;
    }

    public String getSourceunit() {
        return sourceunit;
    }

    public void setSourceunit(String sourceunit) {
        this.sourceunit = sourceunit;
    }

    public String getSourcename() {
        return sourcename;
    }

    public void setSourcename(String sourcename) {
        this.sourcename = sourcename;
    }

    public String getMapnameshow() {
        return mapnameshow;
    }

    public void setMapnameshow(String mapnameshow) {
        this.mapnameshow = mapnameshow;
    }
}
