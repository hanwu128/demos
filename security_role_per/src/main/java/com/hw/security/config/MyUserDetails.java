package com.hw.security.config;

import com.hw.security.entity.SysRole;
import com.hw.security.entity.SysUser;
import com.hw.security.entity.Syspermission;
import com.hw.security.dao.SysPermissionDao;
import com.hw.security.dao.SysRoleDao;
import com.hw.security.dao.SysUserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
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
    private SysUserDao sysUserDao;

    @Autowired
    private SysRoleDao roleDao;

    @Autowired
    private SysPermissionDao permissionDao;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        SysUser sysUser = sysUserDao.findByName(s);

        if (sysUser == null) {

            throw new UsernameNotFoundException("用户不存在");
        }

        List<SysRole> roles = sysUser.getRoles();

        List<GrantedAuthority> grantedAuthorities = null;
        for (SysRole role : roles) {
            SysRole sysRole = roleDao.findByRoleName(role.getName());

            List<Syspermission> permissions = sysRole.getPermissions();
            grantedAuthorities = new ArrayList<>();
            for (Syspermission permission : permissions) {
                if (permission != null && permission.getName() != null) {

                    GrantedAuthority grantedAuthority = new MyGrantedAuthority(permission.getUrl(), permission.getMethod());
                    grantedAuthorities.add(grantedAuthority);
                }
            }
        }

        /*List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (SysRole roles : sysUser.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(roles.getName()));
        }*/
        return new org.springframework.security.core.userdetails.User(
                sysUser.getUsername(), sysUser.getPassword(), grantedAuthorities);
    }


}
