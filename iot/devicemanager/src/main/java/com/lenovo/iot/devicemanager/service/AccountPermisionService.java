/**
 * 
 */
package com.lenovo.iot.devicemanager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.lenovo.iot.devicemanager.dao.AccountPermisionDao;
import com.lenovo.iot.devicemanager.model.AccountPermission;

/**
 * @desc com.lenovo.iot.devicemanager.service.AccountPermisionService
 * @author chench9@lenovo.com
 * @date 2017年11月9日
 */
@Service
public class AccountPermisionService {
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private AccountPermisionDao accountPermisionDao;
	
	public List<String> getAccountPermisionList(String loginName) {
		return accountPermisionDao.getAccountPermisionList(loginName);
	}
	
	/**
	 * 更新账户权限
	 * @param loginName
	 * @param permissionList
	 * @return
	 */
	public Integer updateAccountPermission(String loginName, List<String> permissionList) {
		List<AccountPermission> accountPermissionList = new ArrayList<AccountPermission>();
		for(String permCode : permissionList) {
			AccountPermission ap = new AccountPermission();
			ap.setLoginName(loginName);
			ap.setPermCode(permCode);
			ap.setEditable((short) 1);
			accountPermissionList.add(ap);
		}
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			accountPermisionDao.deleteAccountPermission(loginName);
			accountPermisionDao.addAccountPermissionList(accountPermissionList);
			transactionManager.commit(status);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
		}
		return 0;
	}
}
