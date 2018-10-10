package com.example.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hw.bean.UserAddress;
import com.hw.service.OrderService;
import com.hw.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 1、将服务提供者注册到注册中心（暴露服务）
 * 1）导入dubbo依赖，操作zookeeper客户端工具（curator）
 * <p>
 * 2、让消费者去注册中心订阅服务提供者的服务
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Reference
    UserService userService;

    @Override
    public List<UserAddress> initOrder(String userId) {

        //1、查询用户的收货地址
        List<UserAddress> addressList = userService.getUserAddressList(userId);
        System.out.println(addressList);
        return addressList;

    }

}

