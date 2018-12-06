package com.hw.shiro.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置类
 */
@Configuration
public class ShiroConfig {

    /**
     * 创建ShiroFilterFactoryBean
     *
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(getDefaultWebSecurityManager());

        /**
         * 添加Shiro内置过滤器，可以实现权限的相关拦截器
         * 常用的过滤器：
         * anon：无需认证（登陆）访问
         * authc：必须认证才能访问
         * user：如果使用remenberMe的功能可以直接访问
         * perms：该资源必须得到资源权限才可以访问
         * role：该资源必须得到角色权限才可以访问
         */
        Map<String, String> filterMap = new LinkedHashMap<String, String>();
        filterMap.put("/add", "authc");
        filterMap.put("/update", "authc");
        filterMap.put("/aaa", "authc");

        //授权过滤器
        //注意：当前授权拦截后，shiro会跳转到未授权的页面
        filterMap.put("/add", "perms[user:add]");
        filterMap.put("/update", "perms[user:update]");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        //设置默认登陆页面
        shiroFilterFactoryBean.setLoginUrl("/tologin");

        //设置未授权提示页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauth");

        return shiroFilterFactoryBean;
    }

    /**
     * 创建DefaultWebSecurityManager
     *
     * @return
     */
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联realm
        securityManager.setRealm(getRealm());
        return securityManager;
    }

    /**
     * 创建Realm
     *
     * @return
     */
    @Bean
    public UserRealm getRealm() {
        return new UserRealm();
    }

    /**
     * 配置ShiroDialect，用于thymeleaf和shiro标签配合使用
     *
     * @return
     */
    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }


}
