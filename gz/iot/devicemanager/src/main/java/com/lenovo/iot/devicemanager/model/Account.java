/**
 * 
 */
package com.lenovo.iot.devicemanager.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理员账户信息
 * @desc com.lenovo.iot.devicemanager.model.Account
 * @author chench9@lenovo.com
 * @date 2017年11月6日
 */
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 数据库id
	 */
	private Long id =0L;
	
	/**
	 * 姓名
	 */
	private String name = "";
	
	/**
	 * 登录名
	 */
	private String loginName = "";
	
	/**
	 * 密码
	 */
	private String password = "";
	
	/**
	 * 邮箱
	 */
	private String email = "";
	
	/**
	 * 联系方式
	 */
	private String phone = "";
	
	/**
	 * 是否启用,1: 启用, 0: 禁用
	 */
	private Short enable = 1;
	
	/**
	 * 公司id
	 */
	private Long companyId = 0L;
	
	/**
	 * 创建时间
	 */
	private Date createTime = null;
	
	/**
	 * 更新时间
	 */
	private Date updateTime = null;
	
//	@Override
//	public Object clone() throws CloneNotSupportedException {
//		return super.clone();
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Short getEnable() {
		return enable;
	}

	public void setEnable(Short enable) {
		this.enable = enable;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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

	@Override
	public String toString() {
		return "Account [id=" + id + ", name=" + name + ", loginName=" + loginName + ", password=" + password
				+ ", email=" + email + ", phone=" + phone + ", enable=" + enable + ", companyId=" + companyId
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
	
}
