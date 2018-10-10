/**
 * 
 */
package com.lenovo.iot.devicemanager.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 处理跨域请求
 * @desc com.lenovo.iot.devicemanager.filter.CROSFilter
 * @author chench9@lenovo.com
 * @date 2017年11月8日
 */
public class CROSFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(CROSFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if(logger.isDebugEnabled()) {
			logger.debug(String.format("CORS filter init"));
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if(logger.isDebugEnabled()) {
			logger.debug(String.format("CORS filter do filter"));
		}
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		// fix使用@CrossOrigin
		/*String origin = req.getHeader("Origin");
		resp.setHeader("Access-Control-Allow-Origin", origin);            	  // 允许指定域访问跨域资源
		resp.setHeader("Access-Control-Allow-Credentials", "true");*/
		
		if(RequestMethod.OPTIONS.toString().equals(req.getMethod())) {
			String origin = req.getHeader("Origin");
			resp.setHeader("Access-Control-Allow-Origin", origin);            // 允许指定域访问跨域资源
			resp.setHeader("Access-Control-Allow-Credentials", "true");
			String allowMethod = req.getHeader("Access-Control-Request-Method");
			String allowHeaders = req.getHeader("Access-Control-Request-Headers");
			resp.setHeader("Access-Control-Max-Age", "86400");            // 浏览器缓存预检请求结果时间,单位:秒
			resp.setHeader("Access-Control-Allow-Methods", allowMethod);  // 允许浏览器在预检请求成功之后发送的实际请求方法名
			resp.setHeader("Access-Control-Allow-Headers", allowHeaders); // 允许浏览器发送的请求消息头
			return;
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		if(logger.isDebugEnabled()) {
			logger.debug(String.format("CORS filter destroy"));
		}
	}
}
