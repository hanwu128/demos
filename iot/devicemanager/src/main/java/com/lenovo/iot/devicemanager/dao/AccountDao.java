/**
 * 
 */
package com.lenovo.iot.devicemanager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.lenovo.iot.devicemanager.model.Account;

/**
 * @desc com.lenovo.iot.devicemanager.dao.AccountDao
 * @author chench9@lenovo.com
 * @date 2017年11月6日
 */
@Repository
public interface AccountDao {
	static final String TABLE = "account";
	static final String FIELDS = "id,name,login_name,email,phone,enable,company_id,create_time,update_time";
	static final String FIELDS_INSERT = "name,login_name,email,password,company_id,create_time,update_time";
	
	/**
	 * 添加账户
	 * @param account
	 * @return
	 */
	@Insert("insert into " + TABLE + "(" + FIELDS_INSERT + ") values(#{name},#{loginName},#{email},#{password},#{companyId},now(),now())")
	public Integer addAccount(Account account);
	
	/**
	 * 根据登录名查询账户
	 * @param loginName
	 * @return
	 */
	@Select("select " + FIELDS + " from " + TABLE + " where login_name = #{loginName}")
	public Account getAccountByLoginName(@Param("loginName") String loginName);
	
	/**
	 * 查询账户列表
	 * @param start
	 * @param offset
	 * @return
	 */
	//@Select("select " + FIELDS + " from " + TABLE + " order by create_time DESC limit #{start}, #{offset}")
	// 只查询二级账户列表
	@Select("select a.id,a.name,a.login_name,a.email,a.phone,a.enable,a.company_id,a.create_time,a.update_time from account as a " + 
			"left join account_role as ar " + 
			"on a.login_name = ar.login_name " + 
			"where ar.role_name = 'admin' order by ${order_by} ${order_rule} " + 
			"limit #{start}, #{offset}")
//	@SelectProvider(type = AccountProvider.class, method = "getAccountList")
	public List<Account> getAccountList(@Param("start") int start, @Param("offset") int offset, @Param("order_by") String order_by,  @Param("order_rule") String order_rule);

	@Select("select count(a.id) from account as a " + 
			"left join account_role as ar " + 
			"on a.login_name = ar.login_name " + 
			"where ar.role_name = 'admin'")
	public Integer getAccountListCount();
	
	/**
	 * 更新账户密码
	 * @param password
	 * @param id
	 * @return
	 */
	@Update("update " + TABLE + " set password=#{password} where id=#{id}")
	public Integer updateAccountPassword(@Param("password") String password, @Param("id") long id);
	
	/**
	 * 更新账户姓名和联系方式
	 * @param name
	 * @param phone
	 * @param id
	 * @return
	 */
	@Update("update " + TABLE + " set name=#{name},phone=#{phone} where id=#{id}")
	public Integer updateAccountNamePhone(@Param("name") String name, @Param("phone") String phone, @Param("id") long id);
	
	/**
	 * 启用/禁用账户
	 * @param loginName
	 * @param enable
	 * @return
	 */
	@Update("update " + TABLE + " set enable=#{enable} where login_name=#{loginName}")
	public Integer enabledAccount(@Param("loginName") String loginName, @Param("enable") int enable);
	
	/**
	 * 删除账户
	 * @param loginName
	 * @return
	 */
	@Delete("delete from " + TABLE + " where login_name=#{loginName}") 
	public Integer deleteAccount(@Param("loginName") String loginName);
	
//	@Select("select a.id,a.name,a.login_name,a.email,a.phone,a.enable,a.company_id,a.create_time,a.update_time from account as a " + 
//	"left join account_role as ar " + 
//	"on a.login_name = ar.login_name " + 
//	"where ar.role_name = 'admin' order by #{order_by} #{order_rule} " + 
//	"limit #{start}, #{offset}")
	
	class AccountProvider{
		public String getAccountList(Map<String, Object> map) {
			return new StringBuilder()
			.append("select a.id,a.name,a.login_name,a.email,a.phone,a.enable,a.company_id,a.create_time,a.update_time from account as a ")
			.append("left join account_role as ar ")
			.append("on a.login_name = ar.login_name ")
			.append("where ar.role_name = 'admin' order by ")
			.append(map.get("order_by")).append(" ").append(map.get("order_rule"))
			.append(" limit ")
			.append(map.get("start")).append(",").append(map.get("offset"))
			.toString();
		}
	}
	
}
