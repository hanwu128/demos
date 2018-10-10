package com.lenovo.iot.devicemanager.model;

import java.util.Date;

public abstract class DigitalTwinAttr {
    protected Long id =0L;
    protected Long daddyid = 0L;
    protected String digitaltwinname = "";
    protected String describemessage = "";
    protected String unit = "";
    protected String metric = "";
    protected Date createtimestamp = null;
    protected Date updatetimestamp = null;
    protected Double value = 0.0d;
    protected Double expectedvalue = 0.0d;
    protected String sonname = "";
    protected Long valuetimestamp = 0L;     // 从OpenSDB中查询时获取到的时间戳

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDaddyid() {
        return daddyid;
    }

    public void setDaddyid(Long daddyid) {
        this.daddyid = daddyid;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getExpectedvalue() {
        return expectedvalue;
    }

    public void setExpectedvalue(Double expectedvalue) {
        this.expectedvalue = expectedvalue;
    }

    public String getSonname() {
        return sonname;
    }

    public void setSonname(String sonname) {
        this.sonname = sonname;
    }

    public Long getValuetimestamp() {
        return valuetimestamp;
    }

    public void setValuetimestamp(Long valuetimestamp) {
        this.valuetimestamp = valuetimestamp;
    }
}
