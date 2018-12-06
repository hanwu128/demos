package com.hw.blog.service.impl;

import com.hw.blog.dao.PermissionDao;
import com.hw.blog.model.Permission;
import com.hw.blog.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionDao permissionDao;

    @Override
    public List<Permission> getAll() {
        return permissionDao.getAll();
    }

    @Override
    public List<Permission> getByUserId(Long id) {
        return permissionDao.getByUserId(id);
    }
}
