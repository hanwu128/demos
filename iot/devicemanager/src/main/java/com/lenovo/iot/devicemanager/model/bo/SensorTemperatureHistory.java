package com.lenovo.iot.devicemanager.model.bo;

import java.sql.Timestamp;

/**
 * Created by root on 2017/9/7.
 */
public class SensorTemperatureHistory {

    private int id;

    private String label_id;

    private double value;

    private Timestamp create_stamp;

    private String createstampshow;

    private String what_day;

    private String label_name;

    private String device_id;

    private String group_name;

    private String startTimeAt;

    private String endTimeAt;

    private int totalnumcount;//共多少页

    private int rowcount;    //每页多少条

    private int currentpage; //当前第几页

    private int limitStart;

    private int limitEnd;

    private int intervalSeconds = -10000;//10秒

    private int flagTimeBatch = 60;

    private int total;
    private int over_high;
    private int over_low;

    private String[] labelidarray;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel_id() {
        return label_id;
    }

    public String getLabelid() {
        return label_id;
    }

    public void setLabel_id(String label_id) {
        this.label_id = label_id;
    }


    public void setLabelid(String label_id) {
        this.label_id = label_id;
    }


    public Timestamp getCreate_stamp() {
        return create_stamp;
    }

    public Timestamp getCreatestamp() {
        return create_stamp;
    }


    public void setCreate_stamp(Timestamp create_stamp) {
        this.create_stamp = create_stamp;
    }

    public void setCreatestamp(Timestamp create_stamp) {
        this.create_stamp = create_stamp;
    }


    public String getWhat_day() {
        return what_day;
    }


    public String getWhatday() {
        return what_day;
    }

    public void setWhat_day(String what_day) {
        this.what_day = what_day;
    }

    public void setWhatday(String what_day) {
        this.what_day = what_day;
    }


    public double getValue() {
        return value;
    }




    public void setValue(double value) {
        this.value = value;
    }

    public String getCreatestampshow() {
        return createstampshow;
    }

    public void setCreatestampshow(String createstampshow) {
        this.createstampshow = createstampshow;
    }

    public String getLabelname() {
        return label_name;
    }

    public void setLabelname(String label_name) {
        this.label_name = label_name;
    }



    public String getGroupname() {
        return group_name;
    }

    public void setGroupname(String group_name) {
        this.group_name = group_name;
    }

    public String getStartTimeAt() {
        return startTimeAt;
    }

    public void setStartTimeAt(String startTimeAt) {
        this.startTimeAt = startTimeAt;
    }

    public String getEndTimeAt() {
        return endTimeAt;
    }

    public void setEndTimeAt(String endTimeAt) {
        this.endTimeAt = endTimeAt;
    }

    public int getTotalnumcount() {
        return totalnumcount;
    }

    public void setTotalnumcount(int totalnumcount) {
        this.totalnumcount = totalnumcount;
    }

    public int getRowcount() {
        return rowcount;
    }

    public void setRowcount(int rowcount) {
        this.rowcount = rowcount;
    }

    public int getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart = limitStart;
    }

    public int getLimitEnd() {
        return limitEnd;
    }

    public void setLimitEnd(int limitEnd) {
        this.limitEnd = limitEnd;
    }

    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(int intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    public int getFlagTimeBatch() {
        return flagTimeBatch;
    }

    public void setFlagTimeBatch(int flagTimeBatch) {
        this.flagTimeBatch = flagTimeBatch;
    }

    public String[] getLabelidarray() {
        return labelidarray;
    }

    public void setLabelidarray(String[] labelidarray) {
        this.labelidarray = labelidarray;
    }

    public int getTotal() {
        return total;
    }

    public void Total(int total) {
        this.total = total;
    }

    public int getOver_high() {
        return over_high;
    }

    public void setOver_high(int over_high) {
        this.over_high = over_high;
    }

    public int getOver_low() {
        return over_low;
    }

    public void setOver_low(int over_low) {
        this.over_low = over_low;
    }

    public String getDevice_id() {
        return device_id;
    }

    public String getDeviceid() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public void setDeviceid(String device_id) {
        this.device_id = device_id;
    }
}
