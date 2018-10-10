package com.lenovo.iot.devicemanager.model.bo;

import com.alibaba.fastjson.JSONArray;

import java.sql.Timestamp;

/**
 * Created by root on 2017/9/8.
 */
public class SensorTemperatureMinitue {

    private int id;

    private String label_id;

    private Double label_value;

    private String what_day;

    private String what_minitue;

    private Timestamp update_stamp;

    private String[] labelidarray;

    private String label_name;

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


    public Double getLabel_value() {
        return label_value;
    }
    public Double getLabelvalue() {
        return label_value;
    }


    public void setLabel_value(Double label_value) {
        this.label_value = label_value;
    }

    public void setLabelvalue(Double label_value) {
        this.label_value = label_value;
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


    public String getWhat_minitue() {
        return what_minitue;
    }

    public String getWhatminitue() {
        return what_minitue;
    }


    public void setWhat_minitue(String what_minitue) {
        this.what_minitue = what_minitue;
    }

    public void setWhatminitue(String what_minitue) {
        this.what_minitue = what_minitue;
    }

    public Timestamp getUpdate_stamp() {
        return update_stamp;
    }

    public Timestamp getUpdatestamp() {
        return update_stamp;
    }


    public void setUpdate_stamp(Timestamp update_stamp) {
        this.update_stamp = update_stamp;
    }

    public void setUpdatestamp(Timestamp update_stamp) {
        this.update_stamp = update_stamp;
    }


    public String[] getLabelidarray() {
        return labelidarray;
    }

    public void setLabelidarray(String[] labelidarray) {
        this.labelidarray = labelidarray;
    }


    public String getLabelname() {
        return label_name;
    }

    public void setLabelname(String label_name) {
        this.label_name = label_name;
    }
}

