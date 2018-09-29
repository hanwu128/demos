package com.example.cache.service;

import com.example.cache.bean.Department;
import com.example.cache.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@CacheConfig(cacheNames = "dep", cacheManager = "depCacheManager")
@Service
public class DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 缓存的数据存入redis；
     * 第二次从缓存查询不能反序列化回来；
     * 存的是json数据，CacheManager默认使用自定义的RedisTemplate操作Redis
     *
     * @param id
     * @return
     */
    @Cacheable(key = "#result.id")
    public Department getDepById(Integer id) {
        return departmentMapper.getDepById(id);
    }

}
