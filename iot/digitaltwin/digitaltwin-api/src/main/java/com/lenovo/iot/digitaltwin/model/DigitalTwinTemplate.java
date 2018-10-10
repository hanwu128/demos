package com.lenovo.iot.digitaltwin.model;

import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Desc: DT模板
 * Name: com.lenovo.iot.digitaltwin.model.DigitalTwinTemplate
 * Author: chench9@lenovo.com
 * Date: 2018/6/5 15:25
 **/
@Alias("DigitalTwinTemplate")
public class DigitalTwinTemplate {
    private Long id = 0L;
    private String name = "";
    private String desp = "";
    private Long ctime = 0L;
    private Long mtime = 0L;
    private Integer attrnum = 0;
    private List<DigitalTwinTemplateAttr> attr = new ArrayList<DigitalTwinTemplateAttr>();
    private Integer istnum = 0;

    public DigitalTwinTemplate() {
    }

    public DigitalTwinTemplate(String name, String desp) {
        this.name = name;
        this.desp = desp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getAttrnum() {
        return attrnum;
    }

    public void setAttrnum(Integer attrnum) {
        this.attrnum = attrnum;
    }

    public List<DigitalTwinTemplateAttr> getAttr() {
        return attr;
    }

    public void setAttr(List<DigitalTwinTemplateAttr> attr) {
        this.attr = attr;
    }

    public Integer getIstnum() {
        return istnum;
    }

    public void setIstnum(Integer istnum) {
        this.istnum = istnum;
    }
}
