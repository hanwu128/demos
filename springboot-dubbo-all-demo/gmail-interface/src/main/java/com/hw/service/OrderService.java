package com.hw.service;

import com.hw.bean.UserAddress;

import java.util.List;

public interface OrderService {

    public List<UserAddress> initOrder(String userId);

}
