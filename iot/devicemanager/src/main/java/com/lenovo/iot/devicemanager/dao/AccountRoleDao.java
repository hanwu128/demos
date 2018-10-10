/**
 * 
 */
package com.lenovo.iot.devicemanager.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.lenovo.iot.devicemanager.model.AccountRole;

/**
 * @desc com.lenovo.iot.devicemanager.dao.AccountRoleDao
 * @author chench9@lenovo.com
 * @date 2017年11月9日
 */
@Repository
public interface AccountRoleDao {
	static final String TABLE = "account_role";
	static final String FIELDS_INSERT = "login_name,role_name,create_time,update_time";
	
	/**
	 * 添加账户权限
	 * @param accountRole
	 * @return
	 */
	@Insert("insert into " + TABLE + "(" + FIELDS_INSERT + ") values(#{loginName},#{roleName},now(),now())")
	public Integer addAccountRole(AccountRole accountRole);
	
	/**
	 * 删除账户权限
	 * @param loginName
	 * @return
	 */
	@Delete("delete from " + TABLE + " where login_name=#{loginName}")
	public Integer deleteAccountRole(@Param("loginName") String loginName);
}
