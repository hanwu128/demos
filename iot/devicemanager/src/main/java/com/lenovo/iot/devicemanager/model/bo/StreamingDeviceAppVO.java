package com.lenovo.iot.devicemanager.model.bo;

import java.io.Serializable;

public class StreamingDeviceAppVO implements Serializable {

    private static final long serialVersionUID = -811153735796272771L;

    private Long id;//foreiginkey streamingdevappbriprimarykey
    
    private Long companyid;

    private Long streamingid;

    private Long deviceid;

//    private Long appid;

    private String status;

    private String islive;

    private long period;

    private String periodunit;

    private Long createtimeat;

	private String appname;
    private String appversion;
    private String appdesc;

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

    public Long getStreamingid() {
        return streamingid;
    }

    public void setStreamingid(Long streamingid) {
        this.streamingid = streamingid;
    }

    public Long getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Long deviceid) {
        this.deviceid = deviceid;
    }

//    public Long getAppid() {
//        return appid;
//    }
//
//    public void setAppid(Long appid) {
//        this.appid = appid;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIslive() {
        return islive;
    }

    public void setIslive(String islive) {
        this.islive = islive;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public String getPeriodunit() {
        return periodunit;
    }

    public void setPeriodunit(String periodunit) {
        this.periodunit = periodunit;
    }

    public Long getCreatetimeat() {
        return createtimeat;
    }

    public void setCreatetimeat(Long createtimeat) {
        this.createtimeat = createtimeat;
    }
    
    public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getAppversion() {
		return appversion;
	}

	public void setAppversion(String appversion) {
		this.appversion = appversion;
	}

	public String getAppdesc() {
		return appdesc;
	}

	public void setAppdesc(String appdesc) {
		this.appdesc = appdesc;
	}
}
