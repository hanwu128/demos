package com.hw.service.impl;

import com.hw.bean.UserAddress;
import com.hw.service.UserService;

import java.util.Arrays;
import java.util.List;

public class UserServiceImpl implements UserService {

    @Override
    public List<UserAddress> getUserAddressList(String userId) {
        UserAddress address1 = new UserAddress(1, "北京市海淀区", "1", "李", "123", "Y");
        UserAddress address2 = new UserAddress(2, "北京市海淀区联想", "1", "李", "123", "N");
        return Arrays.asList(address1, address2);
    }

}
