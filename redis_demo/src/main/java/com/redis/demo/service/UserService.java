package com.redis.demo.service;

import com.redis.demo.dao.UserDao;
import com.redis.demo.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserDao userDao;


    /**
     * 查询User列表
     */
    public List<User> getUserList() {
        return userDao.getList();
    }

    /**
     * 查询User
     */
    public User getById(long id) {
        return userDao.getById(id);
    }


    /**
     * 添加User
     */
    @Transactional
    public Integer addUser(User user) {
        int rows = userDao.add(user);
        return rows;
    }

    /**
     * 更新User
     */
    @Transactional
    public Integer updateUser(User user) {

        int rows = userDao.update(user);

        return rows;
    }

    /**
     * 删除User
     */
    @Transactional
    public Integer deleteUser(List<Long> istIdList) {
        int rows = userDao.delete(istIdList);
        return rows;
    }
}
