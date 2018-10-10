package com.lenovo.iot.digitaltwin.model;

import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * Desc: DT实例
 * Name: com.lenovo.iot.digitaltwin.model.DigitalTwinInstance
 * Author: chench9@lenovo.com
 * Date: 2018/6/7 14:47
 **/
@Alias("DigitalTwinInstance")
public class DigitalTwinInstance {
    private Long id = null;
    private Long tpl = null;
    private String name = null;
    private String desp = null;
    private Short state = null;
    private Short lockup = null;
    private String uid = null;
    private String gid = null;
    private Long ctime = null;
    private Long mtime = null;
    private Long atime = null;
    private String tplname = null;
    private Integer attrnum = null;
    private List<DigitalTwinInstanceAttr> attr = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTpl() {
        return tpl;
    }

    public void setTpl(Long tpl) {
        this.tpl = tpl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Short getLockup() {
        return lockup;
    }

    public void setLockup(Short lockup) {
        this.lockup = lockup;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Long getMtime() {
        return mtime;
    }

    public void setMtime(Long mtime) {
        this.mtime = mtime;
    }

    public Long getAtime() {
        return atime;
    }

    public void setAtime(Long atime) {
        this.atime = atime;
    }

    public String getTplname() {
        return tplname;
    }

    public void setTplname(String tplname) {
        this.tplname = tplname;
    }

    public Integer getAttrnum() {
        return attrnum;
    }

    public void setAttrnum(Integer attrnum) {
        this.attrnum = attrnum;
    }

    public List<DigitalTwinInstanceAttr> getAttr() {
        return attr;
    }

    public void setAttr(List<DigitalTwinInstanceAttr> attr) {
        this.attr = attr;
    }
}
