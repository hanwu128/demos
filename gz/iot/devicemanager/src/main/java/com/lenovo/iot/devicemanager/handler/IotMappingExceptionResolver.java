/**
 * 
 */
package com.lenovo.iot.devicemanager.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.lenovo.iot.devicemanager.model.JsonResp;

/**
 * 处理全局异常
 * @desc com.lenovo.iot.devicemanager.handler.IotMappingExceptionResolver
 * @author chench9@lenovo.com
 * @date 2017年11月10日
 */
public class IotMappingExceptionResolver implements HandlerExceptionResolver {
	private static final Logger logger = LoggerFactory.getLogger(IotMappingExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		if(logger.isDebugEnabled()) {
			logger.debug("iot mapping exception resolver resolve exception");
		}
		
		// 对于API访问的权限异常,返回json格式的响应; 否则返回错误页面
		// shiro授权异常
		if(ex instanceof UnauthorizedException) {
			logger.error("unauthorized exception", ex);
			if(accessByApi(request)) {
				try {
					JsonResp json =	JsonResp.getJsonRespError(HttpServletResponse.SC_FORBIDDEN, "account unauthorized");
					response.getWriter().write(JSON.toJSONString(json));
					response.getWriter().flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			return new ModelAndView("forbidden");
		}
		
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("exception", ex);
		return mv ;
	}

	/**
	 * 根据请求消息头判断是否为api请求
	 * @param request
	 * @return
	 */
	private boolean accessByApi(HttpServletRequest request) {
		String acceptHeader = request.getHeader("Accept");
		if(acceptHeader.contains("application/json")) {
			return true;
		}
		
		String requestType = request.getHeader("X-Requested-With");
		if("XMLHttpRequest".equals(requestType)) {
			return true;
		}
		
		return false;
	}

}
