package com.hw.blog.filter;

import com.hw.blog.interceptor.BodyReaderWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 权限token验证
 */
@WebFilter
public class ATokenFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ATokenFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (logger.isDebugEnabled()) {
            logger.debug("token filter init");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (logger.isDebugEnabled()) {
            logger.debug("token filter do filter");
        }
        // 防止流只读一次，把流再写进request
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        ServletRequest requestWrapper = new BodyReaderWrapper(httpServletRequest);
        chain.doFilter(requestWrapper, response);
    }

    @Override
    public void destroy() {
        if (logger.isDebugEnabled()) {
            logger.debug("token filter destroy");
        }
    }
}
