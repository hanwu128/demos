package com.hw.blog.service;

import com.hw.blog.model.Permission;

import java.util.List;

public interface PermissionService {

    List<Permission> getAll();

    List<Permission> getByUserId(Long id);

}
