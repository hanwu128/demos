package com.redis.demo.dao;


import com.redis.demo.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@CacheConfig
public interface UserDao {
    @Cacheable(key = "getList")
    public List<User> getList();
    @Cacheable(key = "getUserById")
    public User getById(long id);
    public Integer add(User user);
    public Integer update(User user);
    public Integer delete(List<Long> istIdList);
}
