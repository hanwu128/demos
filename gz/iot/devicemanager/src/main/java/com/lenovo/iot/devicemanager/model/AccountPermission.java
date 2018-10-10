/**
 * 
 */
package com.lenovo.iot.devicemanager.model;

import java.util.Date;

/**
 * 账户权限
 * @desc com.lenovo.iot.devicemanager.model.AccountPermission
 * @author chench9@lenovo.com
 * @date 2017年11月9日
 */
public class AccountPermission {
	/**
	 * 数据库id
	 */
	private Long id = 0L;
	
	/**
	 * 账户登录名
	 */
	private String loginName = "";
	
	/**
	 * 权限编码
	 */
	private String permCode = "";
	
	/**
	 * 类别,0:不可编辑的默认权限,1:可编辑的权限
	 */
	private Short editable = 0; 
	
	/**
	 * 创建时间
	 */
	private Date createTime = null;
	
	/**
	 * 更新时间
	 */
	private Date updateTime = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPermCode() {
		return permCode;
	}

	public void setPermCode(String permCode) {
		this.permCode = permCode;
	}

	public Short getEditable() {
		return editable;
	}

	public void setEditable(Short editable) {
		this.editable = editable;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
