package com.hw.blog.dao;

import com.hw.blog.model.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionDao {

    List<Permission> getAll();

    List<Permission> getByUserId(Long id);

}
