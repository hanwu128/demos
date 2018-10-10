/**
 * 
 */
package com.lenovo.iot.devicemanager.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.lenovo.iot.devicemanager.util.WebUtil;

/**
 * 修改响应消息头
 * @desc com.lenovo.iot.devicemanager.filter.CROSHeaderAdvice
 * @author chench9@lenovo.com
 * @date 2017年11月24日
 */
@ControllerAdvice
public class CROSHeaderAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		ServletServerHttpRequest ssReq = (ServletServerHttpRequest)request;
		ServletServerHttpResponse ssResp = (ServletServerHttpResponse)response;
		if(ssReq == null || ssResp == null
				|| ssReq.getServletRequest() == null
				|| ssResp.getServletResponse() == null) {
			return body;
		}
		
		HttpServletRequest req = ssReq.getServletRequest();
		HttpServletResponse resp = ssResp.getServletResponse();
		String originHeader = "Access-Control-Allow-Origin";
		if(!resp.containsHeader(originHeader)) {
			/*String origin = req.getHeader("Origin");
			if(origin == null) {
				String referer = req.getHeader("Referer");
				if(referer != null) {
					origin = referer.substring(0, referer.indexOf("/", 7));
				}
			}*/
			String origin = WebUtil.getCORSOrigin(req);
			resp.setHeader("Access-Control-Allow-Origin", origin);
		}
		
		String credentialHeader = "Access-Control-Allow-Credentials";
		if(!resp.containsHeader(credentialHeader)) {
			resp.setHeader(credentialHeader, "true");
		}
		return body;
	}

}
