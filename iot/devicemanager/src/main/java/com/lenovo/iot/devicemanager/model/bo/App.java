package com.lenovo.iot.devicemanager.model.bo;


import java.sql.Timestamp;

/**
 * Created by root on 2017/8/18.
 */
public class App {

    private Long id;
    
    private Long companyid;

    private String appname;//1

    private String apptype;//2

    private String appversion;//3

    private String appdesc;//4

    private String appfilename;//5

    private Timestamp createstamp;//6

    private Timestamp updatestamp;//7

    private Long createtimeat;//8
    private String createtimeatshow;

	private Long updatetimeat;//9

    private String classname;

    private String mdfive;

    private String descstr;
    
    private int limitStart;

    private int limitEnd;

    //分页
    private int current;
	private int pagesize;
    private String sort_orderby;
    private String sort_rule;

	public Long getCompanyid() {
		return companyid;
	}

	public void setCompanyid(Long companyid) {
		this.companyid = companyid;
	}

    public String getApptype() {
        return apptype;
    }

    public void setApptype(String apptype) {
        this.apptype = apptype;
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

    public String getAppfilename() {
        return appfilename;
    }

    public void setAppfilename(String appfilename) {
        this.appfilename = appfilename;
    }

    public Timestamp getCreatestamp() {
        return createstamp;
    }

    public void setCreatestamp(Timestamp createstamp) {
        this.createstamp = createstamp;
    }

    public Timestamp getUpdatestamp() {
        return updatestamp;
    }

    public void setUpdatestamp(Timestamp updatestamp) {
        this.updatestamp = updatestamp;
    }

    public Long getCreatetimeat() {
        return createtimeat;
    }

    public void setCreatetimeat(Long createtimeat) {
        this.createtimeat = createtimeat;
    }

    public String getCreatetimeatshow() {
		return createtimeatshow;
	}

	public void setCreatetimeatshow(String createtimeatshow) {
		this.createtimeatshow = createtimeatshow;
	}

    public Long getUpdatetimeat() {
        return updatetimeat;
    }

    public void setUpdatetimeat(Long updatetimeat) {
        this.updatetimeat = updatetimeat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getMdfive() {
        return mdfive;
    }

    public void setMdfive(String mdfive) {
        this.mdfive = mdfive;
    }

    public String getDescstr() {
        return descstr;
    }

    public void setDescstr(String descstr) {
        this.descstr = descstr;
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
