/**
 * 
 */
package com.lenovo.iot.devicemanager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.lenovo.iot.devicemanager.dao.AccountDao;
import com.lenovo.iot.devicemanager.dao.AccountPermisionDao;
import com.lenovo.iot.devicemanager.dao.AccountRoleDao;
import com.lenovo.iot.devicemanager.dao.PermissionDao;
import com.lenovo.iot.devicemanager.model.Account;
import com.lenovo.iot.devicemanager.model.AccountPermission;
import com.lenovo.iot.devicemanager.model.AccountRole;
import com.lenovo.iot.devicemanager.model.Permission;
import com.lenovo.iot.devicemanager.util.Pagination;

/**
 * @desc com.lenovo.iot.devicemanager.service.AccountService
 * @author chench9@lenovo.com
 * @date 2017年11月6日
 */
@Service
public class AccountService {
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private AccountRoleDao accountRoleDao;
	
	@Autowired
	private AccountPermisionDao accountPermisionDao;
	
	@Autowired
	private PermissionDao permissionDao;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	public Account getAccountByLoginName(String loginName) {
		return accountDao.getAccountByLoginName(loginName);
	}
	
	public List<Account> getAccountList(Pagination page) {
    	int end = page.getPagesize();
		int start = (page.getCurrent() - 1) * page.getPagesize();
		page.setTotal(accountDao.getAccountListCount());
		
		if(page.getOrder_by() == null || page.getOrder_by().isEmpty()) {
			page.setOrder_by("create_time");
		} else if(page.getOrder_by().equalsIgnoreCase("loginName")) {
			page.setOrder_by("login_name");
		}
		page.setOrder_by("a." + page.getOrder_by());
		if(page.getAsc() == null || page.getAsc().isEmpty() || page.getAsc().equalsIgnoreCase("descending") ) {
			page.setAsc("desc");
		}

		return accountDao.getAccountList(start, end, page.getOrder_by(), page.getAsc());
	}
	
	public Integer updateAccountPassword(String password, long id) {
		return accountDao.updateAccountPassword(password, id);
	}
	
	public Integer updateAccountNamePhone(String name, String phone, long id) {
		return accountDao.updateAccountNamePhone(name, phone, id);
	}
	
	/**
	 * 添加账户,在一个事务中完成
	 * @param account
	 * @param accountRole
	 * @return
	 */
	public Integer addAccount(Account account, AccountRole accountRole) throws DuplicateKeyException {
		// 查询二级账户默认权限
		List<AccountPermission> accountPermissionList = new ArrayList<AccountPermission>();
		List<Permission> permissionList = permissionDao.getAdminDefaultPermissionList();
		for(Permission p : permissionList) {
			AccountPermission ap = new AccountPermission();
			ap.setLoginName(account.getLoginName());
			//ap.setEditable((short) 0);
			ap.setEditable(p.getEditable());
			ap.setPermCode(p.getCode());
			accountPermissionList.add(ap);
		}
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			// 添加账户
			accountDao.addAccount(account);
			// 添加账户角色
			accountRoleDao.addAccountRole(accountRole);
			// 添加账户权限
			accountPermisionDao.addAccountPermissionList(accountPermissionList);
			transactionManager.commit(status);
			return 1;
		} catch(DuplicateKeyException e) {
			e.printStackTrace();
			transactionManager.rollback(status);
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
		}
		return 0;
	}
	
	public Integer enabledAccount(String loginName, int enable) {
		return accountDao.enabledAccount(loginName, enable);
	}
	
	/**
	 * 删除账户 <br />
	 * 删除账户时需要删除账户角色和权限
	 * @param loginName
	 * @return
	 */
	public Integer deleteAccount(String loginName) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			// 删除账户
			accountDao.deleteAccount(loginName);
			// 删除账户角色
			accountRoleDao.deleteAccountRole(loginName);
			// 删除账户权限
			accountPermisionDao.deleteAccountPermissionAll(loginName);
			transactionManager.commit(status);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
		}
		return 0;
	}
}
