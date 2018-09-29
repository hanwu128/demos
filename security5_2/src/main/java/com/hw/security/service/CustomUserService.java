package com.hw.security.service;

import com.hw.security.dao.PermissionDao;
import com.hw.security.dao.UserDao;
import com.hw.security.entity.Permission;
import com.hw.security.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyibo on 17/1/18.
 */
@Service
public class CustomUserService implements UserDetailsService { //自定义UserDetailsService 接口

    @Autowired
    UserDao userDao;
    @Autowired
    PermissionDao permissionDao;

    public UserDetails loadUserByUsername(String username) {
        SysUser user = userDao.findByUserName(username);
        if (user != null) {
            List<Permission> permissions = permissionDao.findByAdminUserId(user.getId());
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (Permission permission : permissions) {
                if (permission != null && permission.getName() != null) {

                    GrantedAuthority grantedAuthority = new MyGrantedAuthority(permission.getUrl(), permission.getMethod());
                    grantedAuthorities.add(grantedAuthority);
                }
            }
            return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
        } else {
            throw new UsernameNotFoundException("admin: " + username + " do not exist!");
        }
    }

}
