package com.hw.blog.shiro;

import com.hw.blog.model.User;
import com.hw.blog.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;

/**
 * 自定义Realm
 */
public class UserRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    /**
     * 执行授权逻辑
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("执行授权逻辑");

        //给资源进行授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //添加资源的授权字符串
        //info.addStringPermission("user:add");
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        User user2 = userService.getById(user.getId());

        //info.addStringPermission(user2.getPerms());

        //return info;
        return null;
    }

    /**
     * 执行认证逻辑
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行认证逻辑");

        //编写Shiro判断逻辑，判断用户名和密码
        //1、判断用户名
        UsernamePasswordToken token1 = (UsernamePasswordToken) token;

        User user = userService.getByName(token1.getUsername());

        if (user == null) {
            //用户名不存在
            return null;//Shiro底层会抛出UnknownAccountException
        }

        //2、判断密码
        return new SimpleAuthenticationInfo(user, user.getPassword(), "");
    }
}
