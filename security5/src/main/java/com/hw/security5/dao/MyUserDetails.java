package com.hw.security5.dao;

import com.hw.security5.entity.Roles;
import com.hw.security5.entity.SysUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetails implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(MyUserDetails.class);

    @Autowired
    SysUserDao sysUserDao;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        SysUser sysUser = sysUserDao.findByName(s);

        if (sysUser == null) {

            throw new UsernameNotFoundException("用户不存在");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Roles roles : sysUser.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(roles.getRname()));
        }
        return new org.springframework.security.core.userdetails.User(
                sysUser.getName(), sysUser.getPassword(), authorities);
    }


}
