package com.hw.security.dao;

import com.hw.security.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by yangyibo on 17/1/20.
 */
public interface PermissionDao {
    public List<Permission> findAll();
    public List<Permission> findByAdminUserId(int userId);
}
