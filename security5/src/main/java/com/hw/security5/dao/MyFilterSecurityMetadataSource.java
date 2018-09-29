package com.hw.security5.dao;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component("myFilterSecurityMetadataSource")
public class MyFilterSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

    /*
     * 这个例子放在构造方法里初始化url权限数据，我们只要保证在 getAttributes()之前初始好数据就可以了
     */
    public MyFilterSecurityMetadataSource() {
        Map<RequestMatcher, Collection<ConfigAttribute>> map = new HashMap<>();

        AntPathRequestMatcher matcher = new AntPathRequestMatcher("/index");
        SecurityConfig config = new SecurityConfig("ROLE_ADMIN");
        ArrayList<ConfigAttribute> configs = new ArrayList<>();
        configs.add(config);
        map.put(matcher, configs);

        AntPathRequestMatcher matcher2 = new AntPathRequestMatcher("/news");
        SecurityConfig config2 = new SecurityConfig("ROLE_USER");
        ArrayList<ConfigAttribute> configs2 = new ArrayList<>();
        configs2.add(config2);
        map.put(matcher2, configs2);

        this.requestMap = map;
    }


    /**
     * 在我们初始化的权限数据中找到对应当前url的权限数据
     *
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        HttpServletRequest request = fi.getRequest();
        String url = fi.getRequestUrl();
        String httpMethod = fi.getRequest().getMethod();

        //遍历我们初始化的权限数据，找到对应的url对应的权限
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}