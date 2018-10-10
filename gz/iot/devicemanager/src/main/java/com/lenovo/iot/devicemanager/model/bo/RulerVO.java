package com.lenovo.iot.devicemanager.model.bo;

import java.io.Serializable;

/**
 * Created by root on 2017/8/29.
 */
public class RulerVO implements Serializable {

    private static final long serialVersionUID = -8117113735796272771L;

    private int id;

    private int appid;

    private int appsourceid;

    private String attributename;

    private String mathematicalsymbol;

    private String rightvalue;

    private String expressionstatement;

    private String logicalsymbol;

    private String groupid;

    private int groupinnerindex;

    private String attributevaluetype;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public int getAppsourceid() {
        return appsourceid;
    }

    public void setAppsourceid(int appsourceid) {
        this.appsourceid = appsourceid;
    }

    public String getAttributename() {
        return attributename;
    }

    public void setAttributename(String attributename) {
        this.attributename = attributename;
    }

    public String getMathematicalsymbol() {
        return mathematicalsymbol;
    }

    public void setMathematicalsymbol(String mathematicalsymbol) {
        this.mathematicalsymbol = mathematicalsymbol;
    }

    public String getRightvalue() {
        return rightvalue;
    }

    public void setRightvalue(String rightvalue) {
        this.rightvalue = rightvalue;
    }

    public String getExpressionstatement() {
        return expressionstatement;
    }

    public void setExpressionstatement(String expressionstatement) {
        this.expressionstatement = expressionstatement;
    }

    public String getLogicalsymbol() {
        return logicalsymbol;
    }

    public void setLogicalsymbol(String logicalsymbol) {
        this.logicalsymbol = logicalsymbol;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public int getGroupinnerindex() {
        return groupinnerindex;
    }

    public void setGroupinnerindex(int groupinnerindex) {
        this.groupinnerindex = groupinnerindex;
    }

    public String getAttributevaluetype() {
        return attributevaluetype;
    }

    public void setAttributevaluetype(String attributevaluetype) {
        this.attributevaluetype = attributevaluetype;
    }
}
