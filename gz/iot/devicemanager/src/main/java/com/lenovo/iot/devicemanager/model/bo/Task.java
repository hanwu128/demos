package com.lenovo.iot.devicemanager.model.bo;

/**
 * Created by root on 2017/8/18.
 */
public class Task {

    private static final long serialVersionUID = -8997153735796271771L;

    private Long id;    //1
    
    private Long companyid;

    private String appid;   //2

    private String deviceid; //3

    private Long createtimeat;//4

    private Long updatetimeat;//5

    private  String taskcode ;//6

    private String devicestatus;//7

    private String appstatus;   //8

    private String jarappmdfive;//9

    private String jarfilewholeforder;//10

    private String taskownerid;//11

    private String taskdeploypolicyexpression;//11


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyid() {
        return companyid;
    }

	public void setCompanyid(Long companyid) {
		this.companyid = companyid;
	}

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public Long getCreatetimeat() {
        return createtimeat;
    }

    public void setCreatetimeat(Long createtimeat) {
        this.createtimeat = createtimeat;
    }

    public Long getUpdatetimeat() {
        return updatetimeat;
    }

    public void setUpdatetimeat(Long updatetimeat) {
        this.updatetimeat = updatetimeat;
    }

    public String getTaskcode() {
        return taskcode;
    }

    public void setTaskcode(String taskcode) {
        this.taskcode = taskcode;
    }

    public String getDevicestatus() {
        return devicestatus;
    }

    public void setDevicestatus(String devicestatus) {
        this.devicestatus = devicestatus;
    }

    public String getAppstatus() {
        return appstatus;
    }

    public void setAppstatus(String appstatus) {
        this.appstatus = appstatus;
    }

    public String getJarappmdfive() {
        return jarappmdfive;
    }

    public void setJarappmdfive(String jarappmdfive) {
        this.jarappmdfive = jarappmdfive;
    }

    public String getJarfilewholeforder() {
        return jarfilewholeforder;
    }

    public void setJarfilewholeforder(String jarfilewholeforder) {
        this.jarfilewholeforder = jarfilewholeforder;
    }

    public String getTaskownerid() {
        return taskownerid;
    }

    public void setTaskownerid(String taskownerid) {
        this.taskownerid = taskownerid;
    }

    public String getTaskdeploypolicyexpression() {
        return taskdeploypolicyexpression;
    }

    public void setTaskdeploypolicyexpression(String taskdeploypolicyexpression) {
        this.taskdeploypolicyexpression = taskdeploypolicyexpression;
    }
}
