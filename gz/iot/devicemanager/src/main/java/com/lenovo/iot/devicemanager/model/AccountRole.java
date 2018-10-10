/**
 * 
 */
package com.lenovo.iot.devicemanager.model;

import java.util.Date;

/**
 * 账户角色
 * @desc com.lenovo.iot.devicemanager.model.AccountRole
 * @author chench9@lenovo.com
 * @date 2017年11月9日
 */
public class AccountRole {
	/**
	 * 数据库id
	 */
	private Long id = 0L;
	
	/**
	 * 账户登录名
	 */
	private String loginName = "";
	
	/**
	 * 账户角色名
	 */
	private String roleName = "";
	
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

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
