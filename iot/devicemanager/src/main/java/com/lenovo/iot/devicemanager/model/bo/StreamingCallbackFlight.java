package com.lenovo.iot.devicemanager.model.bo;

import java.io.Serializable;

public class StreamingCallbackFlight  implements Serializable {

    private static final long serialVersionUID = -8117113734796272771L;

    private Long id;

    private Long topid;

    private Long companyid;
    private Long streamingid;

    private String streamingname;

    private String streamingdesc;

    private Long createtimeat;

    private Long deviceid;

    private String policyname;

    private String accesskey;

    private String secretkey;

    private String devicedesc;

    private String deviceidcode;

    private String islive;

    private String createtimeatshow;

    private String appname;

    private String appversion;

    private String appdesc;

//    private Long appid;

    private String status;

    //private String device_id;

   // private String leap_id;

   // private String policy_name;

    //private String access_key;

    //private String secret_key;

    //private String device_desc;
    
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

    public String getStreamingname() {
        return streamingname;
    }

    public void setStreamingname(String streamingname) {
        this.streamingname = streamingname;
    }

    public String getStreamingdesc() {
        return streamingdesc;
    }

    public void setStreamingdesc(String streamingdesc) {
        this.streamingdesc = streamingdesc;
    }

    public Long getCreatetimeat() {
        return createtimeat;
    }

    public void setCreatetimeat(Long createtimeat) {
        this.createtimeat = createtimeat;
    }

    public Long getTopid() {
        return topid;
    }

    public void setTopid(Long topid) {
        this.topid = topid;
    }


    public String getPolicyname() {
        return policyname;
    }

    public void setPolicyname(String policyname) {
        this.policyname = policyname;
    }

    public String getAccesskey() {
        return accesskey;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public String getDevicedesc() {
        return devicedesc;
    }

    public void setDevicedesc(String devicedesc) {
        this.devicedesc = devicedesc;
    }

    public String getDeviceidcode() {
        return deviceidcode;
    }

    public void setDeviceidcode(String deviceidcode) {
        this.deviceidcode = deviceidcode;
    }

    public String getIslive() {
        return islive;
    }

    public void setIslive(String islive) {
        this.islive = islive;
    }

    public String getCreatetimeatshow() {
        return createtimeatshow;
    }

    public void setCreatetimeatshow(String createtimeatshow) {
        this.createtimeatshow = createtimeatshow;
    }

//    public Long getAppid() {
//        return appid;
//    }
//
//    public void setAppid(Long appid) {
//        this.appid = appid;
//    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public String getDevice_id() {
//        return device_id;
//    }
//
//    public void setDevice_id(String device_id) {
//        this.device_id = device_id;
//    }
//
//    public String getLeap_id() {
//        return leap_id;
//    }
//
//    public void setLeap_id(String leap_id) {
//        this.leap_id = leap_id;
//    }
//
//    public String getPolicy_name() {
//        return policy_name;
//    }
//
//    public void setPolicy_name(String policy_name) {
//        this.policy_name = policy_name;
//    }
//
//    public String getAccess_key() {
//        return access_key;
//    }
//
//    public void setAccess_key(String access_key) {
//        this.access_key = access_key;
//    }
//
//    public String getSecret_key() {
//        return secret_key;
//    }
//
//    public void setSecret_key(String secret_key) {
//        this.secret_key = secret_key;
//    }
//
//    public String getDevice_desc() {
//        return device_desc;
//    }
//
//    public void setDevice_desc(String device_desc) {
//        this.device_desc = device_desc;
//    }

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
