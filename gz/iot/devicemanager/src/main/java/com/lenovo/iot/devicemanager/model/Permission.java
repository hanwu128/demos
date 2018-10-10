/**
 * 
 */
package com.lenovo.iot.devicemanager.model;

import java.util.Date;

/**
 * 权限
 * @desc com.lenovo.iot.devicemanager.model.Permission
 * @author chench9@lenovo.com
 * @date 2017年11月8日
 */
public class Permission {
	/**
	 * 数据库id
	 */
	private Long id = 0L;
	
	/**
	 * 权限名称
	 */
	private String name = "";
	
	/**
	 * 权限编码
	 */
	private String code = "";
	
	/**
	 * 父节点权限编码
	 */
	private String parent = "";
	
	/**
	 * 权限值
	 */
	private String val = "";
	
	/**
	 * 可编辑状态
	 */
	private Short editable = 0;
	
	/**
	 * 排序优先级
	 */
	private Integer priority = 0;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public Short getEditable() {
		return editable;
	}

	public void setEditable(Short editable) {
		this.editable = editable;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
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
