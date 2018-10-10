/**
 * 
 */
package com.lenovo.iot.devicemanager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.lenovo.iot.devicemanager.model.AccountPermission;

/**
 * @desc com.lenovo.iot.devicemanager.dao.AccountPermisionDao
 * @author chench9@lenovo.com
 * @date 2017年11月9日
 */
@Repository
public interface AccountPermisionDao {
	static final String TABLE = "account_permission";
	static final String FIELDS = "id,login_name,perm_code,editable,create_time,update_time";
	
	/**
	 * 查询账户权限列表
	 * @param loginName
	 * @return
	 */
	@Select("select p.val from account_permission as ap " +
	        "left join permission as p " + 
			"on ap.perm_code = p.code " +
	        "where ap.login_name = #{loginName} order by p.create_time")
	public List<String> getAccountPermisionList(@Param("loginName") String loginName);
	
	/**
	 * 添加账户权限
	 * @param accountPermissionList
	 * @return
	 */
	@InsertProvider(type = AccountPermissionProvider.class, method = "addAccountPermissionList")
	public Integer addAccountPermissionList(List<AccountPermission> accountPermissionList);
	
	/**
	 * 删除账户非默认权限
	 * @param loginName
	 * @return
	 */
	@Delete("delete from " + TABLE + " where login_name=#{loginName} and editable > 0")
	public Integer deleteAccountPermission(@Param("loginName") String loginName);
	
	/**
	 * 删除账户所有权限
	 * @param loginName
	 * @return
	 */
	@Delete("delete from " + TABLE + " where login_name=#{loginName}")
	public Integer deleteAccountPermissionAll(@Param("loginName") String loginName);
	
	class AccountPermissionProvider {
		public String addAccountPermissionList(Map<String, List<AccountPermission>> map) {
			List<AccountPermission> accountPermissionList = map.get("list");
			
			StringBuilder builder = new StringBuilder()
					.append("insert into account_permission(login_name,perm_code,editable,create_time,update_time) values");
			int size = accountPermissionList.size();
			int beforeLast = size - 1;
			for(int i = 0; i < size; i++) {
				AccountPermission ap = accountPermissionList.get(i);
				builder.append("(")
				.append("'").append(ap.getLoginName()).append("',")
				.append("'").append(ap.getPermCode()).append("',")
				.append(ap.getEditable()).append(",")
				.append("now(),now()")
				.append(")");
				if(i < beforeLast) {
					builder.append(",");
				}
			}
			
			return builder.toString();
		}
	}
	
}
