/**
 * 
 */
package com.lenovo.iot.devicemanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lenovo.iot.devicemanager.dao.PermissionDao;
import com.lenovo.iot.devicemanager.model.Permission;

/**
 * @desc com.lenovo.iot.devicemanager.service.PermissionService
 * @author chench9@lenovo.com
 * @date 2017年11月8日
 */
@Service
public class PermissionService {
	@Autowired
	private PermissionDao permissionDao;
	
	public List<Permission> getAdminDefaultPermissionList() {
		return permissionDao.getAdminDefaultPermissionList();
	}
}
