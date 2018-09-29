package com.hw.jwt.service.impl;

import com.hw.jwt.entity.Employee;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = new Employee();
        return new User(employee.getUsername(), employee.getPassword(), emptyList());
    }
}
