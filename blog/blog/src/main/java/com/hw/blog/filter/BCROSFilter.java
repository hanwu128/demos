package com.hw.blog.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Desc: 解决浏览器跨域访问限制,Filter默认根据文件名排序,本程序Filter按照英文字母顺序进行排序,
 **/
@WebFilter(urlPatterns = {"/**"})
public class BCROSFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(BCROSFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (logger.isDebugEnabled()) {
            logger.debug("CORS filter init");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (logger.isDebugEnabled()) {
            logger.debug("CORS filter do filter");
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String origin = req.getHeader("Origin");
        if (origin == null) {
            origin = req.getHeader("Referer");      // 如果从消息头Origin中取不到值，就从Referer中获取
        }
        resp.setHeader("Access-Control-Allow-Origin", origin);      // 允许指定域访问跨域资源
        resp.setHeader("Access-Control-Allow-Credentials", "true");       // 允许客户端携带跨域cookie，此时origin值不能为“*”，只能为指定单一域名

        if (RequestMethod.OPTIONS.toString().equals(req.getMethod())) {
            String allowMethod = req.getHeader("Access-Control-Request-Method");
            String allowHeaders = req.getHeader("Access-Control-Request-Headers");
            resp.setHeader("Access-Control-Max-Age", "86400");            // 浏览器缓存预检请求结果时间,单位:秒
            resp.setHeader("Access-Control-Allow-Methods", allowMethod);        // 允许浏览器在预检请求成功之后发送的实际请求方法名
            resp.setHeader("Access-Control-Allow-Headers", allowHeaders);       // 允许浏览器发送的请求消息头
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        if (logger.isDebugEnabled()) {
            logger.debug("CORS filter destroy");
        }
    }
}

