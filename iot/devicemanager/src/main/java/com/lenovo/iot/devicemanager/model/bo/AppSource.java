package com.lenovo.iot.devicemanager.model.bo;

import java.sql.Timestamp;

/**
 * Created by root on 2017/8/18.
 */
public class AppSource {

    private Integer id;//primaryKey

    private String appname;

    private String sourcename;

    private String sourcedisplayname;

    private String sourcedefault;

    private String sourcedatatype;

    private String sourceunit;

    private Timestamp createstamp;

    private String appid;

    private Long createtimeat;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getSourcename() {
        return sourcename;
    }

    public void setSourcename(String sourcename) {
        this.sourcename = sourcename;
    }

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

    public Timestamp getCreatestamp() {
        return createstamp;
    }

    public void setCreatestamp(Timestamp createstamp) {
        this.createstamp = createstamp;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public Long getCreatetimeat() {
        return createtimeat;
    }

    public void setCreatetimeat(Long createtimeat) {
        this.createtimeat = createtimeat;
    }
}
