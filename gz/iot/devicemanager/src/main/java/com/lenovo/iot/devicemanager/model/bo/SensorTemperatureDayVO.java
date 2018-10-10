package com.lenovo.iot.devicemanager.model.bo;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by root on 2017/9/7.
 */
public class SensorTemperatureDayVO {

    private int id;

    private String label_id;

    private double current_value;

    private double max_value;

    private double min_value;

    private String what_day;

    private Timestamp update_stamp;

    private String updatestampstrshow;

    private String label_name;

    private String label_group;

    private String label_index;

    private String device_id;

    private Timestamp create_stamp;

    private Timestamp max_stamp;

    private String maxstampshow;

    private Timestamp min_stamp;

    private String minstampshow;

    private Double difference;
    
    private int status;
    
    private int last_status;
    
    private int limitStart;

    private int limitEnd;

    private int totalnumcount;//共多少页

    private int rowcount;    //每页多少条

    private int currentpage; //当前第几页

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

    public void setLabelid(String label_id) {
        this.label_id = label_id;
    }


    public Timestamp getUpdatestamp() {
        return update_stamp;
    }


    public void setUpdatestamp(Timestamp update_stamp) {
        this.update_stamp = update_stamp;
    }


    public String getLabelname() {
        return label_name;
    }


    public void setLabelname(String label_name) {
        this.label_name = label_name;
    }


    public String getLabelgroup() {
        return label_group;
    }

    public void setLabelgroup(String label_group) {
        this.label_group = label_group;
    }



    public String getLabelindex() {
        return label_index;
    }


    public void setLabelindex(String label_index) {
        this.label_index = label_index;
    }


    public Timestamp getCreatestamp() {
        return create_stamp;
    }


    public void setCreatestamp(Timestamp create_stamp) {
        this.create_stamp = create_stamp;
    }



    public double getCurrentvalue() {
        return current_value;
    }




    public void setCurrentvalue(double current_value) {
        this.current_value = current_value;
    }




    public double getMaxvalue() {
        return max_value;
    }





    public void setMaxvalue(double max_value) {
        this.max_value = max_value;
    }




    public double getMinvalue() {
        return min_value;
    }




    public void setMinvalue(double min_value) {
        this.min_value = min_value;
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

    public String getUpdatestampstrshow() {
        return updatestampstrshow;
    }

    public void setUpdatestampstrshow(String updatestampstrshow) {
        this.updatestampstrshow = updatestampstrshow;
    }

    public Timestamp getMaxstamp() {
        return max_stamp;
    }

    public void setMaxstamp(Timestamp max_stamp) {
        this.max_stamp = max_stamp;
    }

    public Timestamp getMinstamp() {
        return min_stamp;
    }

    public void setMinstamp(Timestamp min_stamp) {
        this.min_stamp = min_stamp;
    }

    public String getMaxstampshow() {
        return maxstampshow;
    }

    public void setMaxstampshow(String maxstampshow) {
        this.maxstampshow = maxstampshow;
    }

    public String getMinstampshow() {
        return minstampshow;
    }

    public void setMinstampshow(String minstampshow) {
        this.minstampshow = minstampshow;
    }

    public Double getDifference() {
        return difference;
    }

    public void setDifference(Double difference) {
        this.difference = difference;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLaststatus() {
        return last_status;
    }

    public void setLaststatus(int last_status) {
        this.last_status = last_status;
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
