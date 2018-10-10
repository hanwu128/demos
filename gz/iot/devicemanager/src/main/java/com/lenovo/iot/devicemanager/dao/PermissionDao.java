/**
 * 
 */
package com.lenovo.iot.devicemanager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.lenovo.iot.devicemanager.model.Permission;

/**
 * @desc com.lenovo.iot.devicemanager.dao.PermissionDao
 * @author chench9@lenovo.com
 * @date 2017年11月8日
 */
@Repository
public interface PermissionDao {
	static final String TABLE = "permission";
	static final String FIELDS = "id,name,code,parent,val,editable,initial,priority,create_time,update_time";
	
	/**
	 * 查询二级账户的默认权限列表
	 * @return
	 */
	@Select("select " + FIELDS + " from " + TABLE + " where initial=0")
	public List<Permission> getAdminDefaultPermissionList();
	
}
