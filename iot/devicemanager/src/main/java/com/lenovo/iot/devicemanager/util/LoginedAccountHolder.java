/**
 * 
 */
package com.lenovo.iot.devicemanager.util;

import com.lenovo.iot.devicemanager.model.Account;

/**
 * 在当前线程上下文保存登录用户信息
 * @desc com.lenovo.iot.devicemanager.util.LoginedAccountHolder
 * @author chench9@lenovo.com
 * @date 2017年11月9日
 */
public class LoginedAccountHolder {
	private static final ThreadLocal<Account> THREAD_LOCAL = new ThreadLocal<Account>();
	
	/**
	 * 保存当前已经登陆的用户对象
	 * @param account
	 */
	public static void saveLoginedAccount(Account account) {
		THREAD_LOCAL.set(account);
	}
	
	/**
	 * 返回当前已经登录的用户对象
	 * @return
	 */
	public static Account getLoginedAccount() {
		Account account = THREAD_LOCAL.get();
		if(account == null) {
			account = new Account();
		}
		
		return account;
	}
	
	private LoginedAccountHolder() {
	}
}
