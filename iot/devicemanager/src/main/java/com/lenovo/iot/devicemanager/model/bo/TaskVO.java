package com.lenovo.iot.devicemanager.model.bo;

import java.io.Serializable;

/**
 * Created by root on 2017/8/21.
 */
public class TaskVO implements Serializable {

    private static final long serialVersionUID = -1997153735796272771L;


    private Long id;

    private Long companyid;

    private Long appid;

    private String[] appidArray;

    private String deviceid;

    private String[] deviceidArray;

    private Long createtimeat;

    private Long updatetimeat;

    private String taskcode;

    private String devicestatus;

    private String appstatus;

    private String jarappmdfive;

    private String jarfilewholeforder;

    private String taskownerid;

    private String taskdeploypolicyexpression;

    private String taskstatus;

    private String apptype;

    private String appfilename;

    private String appversion;

    private String tasktype;

    private Boolean fire = true;

//    private String commonJsonstr;

    private String appname;

    private Boolean startOrStop;

    private String classname;

    private String devicecode;

    private String createtimeatshow;

    private String device_id;
    private String accesskey;

    private int limitStart;

    private int limitEnd;

    //分页
    private int current;
	private int pagesize;
    private String sort_orderby;
    private String sort_rule;

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

    public Long getAppid() {
        return appid;
    }

    public void setAppid(Long appid) {
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

    public String getTaskstatus() {
        return taskstatus;
    }

    public void setTaskstatus(String taskstatus) {
        this.taskstatus = taskstatus;
    }

    public Boolean getFire() {
        return fire;
    }

    public void setFire(Boolean fire) {
        this.fire = fire;
    }

//    public String getCommonJsonstr() {
//        return commonJsonstr;
//    }
//
//    public void setCommonJsonstr(String commonJsonstr) {
//        this.commonJsonstr = commonJsonstr;
//    }

    public String getApptype() {
        return apptype;
    }

    public void setApptype(String apptype) {
        this.apptype = apptype;
    }

    public String getAppfilename() {
        return appfilename;
    }

    public void setAppfilename(String appfilename) {
        this.appfilename = appfilename;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public Boolean getStartOrStop() {
        return startOrStop;
    }

    public void setStartOrStop(Boolean startOrStop) {
        this.startOrStop = startOrStop;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getDevicecode() {
        return devicecode;
    }

    public void setDevicecode(String devicecode) {
        this.devicecode = devicecode;
    }

    public String getCreatetimeatshow() {
        return createtimeatshow;
    }

    public void setCreatetimeatshow(String createtimeatshow) {
        this.createtimeatshow = createtimeatshow;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getAccesskey() {
        return accesskey;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String[] getAppidArray() {
        return appidArray;
    }

    public void setAppidArray(String[] appidArray) {
        this.appidArray = appidArray;
    }

    public String[] getDeviceidArray() {
        return deviceidArray;
    }

    public void setDeviceidArray(String[] deviceidArray) {
        this.deviceidArray = deviceidArray;
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

    public int getCurrent() {
		return current;
	}

	//分页
	public void setCurrent(int current) {
		if(current == 0) {
			current = 1;
		}
		this.current = current;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		if(pagesize == 0) {
			pagesize = 20;
		}
		this.pagesize = pagesize;
	}

	public String getSort_orderby() {
		return sort_orderby;
	}

	public void setSort_orderby(String sort_orderby) {
		this.sort_orderby = sort_orderby;
	}

	public String getSort_rule() {
		return sort_rule;
	}

	public void setSort_rule(String sort_rule) {
		this.sort_rule = sort_rule;
	}
}
