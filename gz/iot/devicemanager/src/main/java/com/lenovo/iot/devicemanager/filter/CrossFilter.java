package com.lenovo.iot.devicemanager.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Created by root on 2017/2/15.
 */
public class CrossFilter extends HandlerInterceptorAdapter {

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//		String originHeader = "Access-Control-Allow-Origin";
//		if(response.containsHeader(originHeader)) {
//			String origin = request.getHeader("Origin");
//			if(origin == null) {
//				String referer = request.getHeader("Referer");
//				if(referer != null) {
//					origin = referer.substring(0, referer.indexOf("/", 7));
//				}
//			}
//			response.setHeader("Access-Control-Allow-Origin", origin);
//		}
//		
//		String credentialHeader = "Access-Control-Allow-Credentials";
//		if(response.containsHeader(credentialHeader)) {
//			response.setHeader("Access-Control-Allow-Credentials", "true");
//		}
    }
    
}
