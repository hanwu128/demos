package com.lenovo.iot.devicemanager.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.lenovo.iot.devicemanager.util.XSSRequestWrapper;

public class SecurityFilter implements Filter{
//	private final String tag = "SecurityFilter";
//	private static final Logger logger = Logger.getLogger(SecurityFilter.class);

//	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		HttpServletRequest r = (HttpServletRequest) request;
//		String path = r.getServletPath();
/*
		Map<String, String[]> map = request.getParameterMap();
		for(String[] st : map.values()){
			for(String s : st){
				if (s != null) {
					s = s.toLowerCase();
					if(s.contains(" and ") || s.contains(" or ") ){ //|| s.contains("--")){
						String msg = "Invalid para : " + s;
						//Log.info(tag, msg);
						response.getWriter().write(msg);
						return;
					}
				}
			}
		}
 */
//		if(checkLogin(r, path)){
			//chain.doFilter(request, response);
			XSSRequestWrapper xssrequest = new XSSRequestWrapper((HttpServletRequest)request);
			chain.doFilter(xssrequest, response);
//		}else{
//			// 重定向到登录页面			
//			logger.warn("logout and redirect home page");
//			WebUtil.logout((HttpServletRequest)request, (HttpServletResponse)response, "session time out");
//		}
	}

	public void init(FilterConfig cfg) throws ServletException {
		//String path = cfg.getServletContext().getContextPath();
		//cfg.getServletContext().setAttribute("base", path);
	}
	
	public void destroy() {
		//Log.info(tag, "destroy");
	}

/*
	private boolean checkLogin(HttpServletRequest req, String path) throws UnsupportedEncodingException{
		boolean result = false;
		
		if(checkStaticRes(path)) {
			return true;
		}
		
		if(path.contains("/api/")) {
			return true; // api请求不拦截
		}
		
		if(path.contains("/app_")) {
			return true; // appi请求不拦截
		}
		
		if(path.contains("/adminapp_")) {
			return true; // 请求不拦截
		}
		
		if(path.contains("/home_login.do")) {
			return true;
		}
		
		if(path.contains("/monitor_/")){
			return true;	// 监控moc服务不拦截
		}
		
		if(path.contains("/appdownload.do")) {
			return true; // app下载页面不拦截
		}
		
		// 忘记密码,找回密码
		if(path.contains("/forget_pwd.do") ||
				path.contains("/reset_pwd_code.do") ||
				path.contains("/reset_pwd.do")) {
			return true;
		}
		
		if(path.contains("/home.do") || 
				path.contains("/home_admin_logout.do") ||
				path.contains("/dashboard/mainlogin.do") ||
				path.contains("/dashboard/mainlogout.do") ||
				path.contains("/trial_company_save.do") ||
				path.contains("/home_login_callback.do") ||
				path.contains("/home_login_demo.do") ||
				path.contains("/home_logout.do")) {
			return true;
		}
		
		Subject subject = null;
		try {
			subject = SecurityUtils.getSubject();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		if(subject == null || !subject.isAuthenticated()) {
			logger.error(String.format("admin user not login: %s", path));
			return false;
		}
		
		if(path.equals("/home_login.do") || path.startsWith("/res/login/") || path.startsWith("/adminapp_")){
			result = true;
		} else if(path.equals("/home.do") || path.startsWith("/res/home/") || path.startsWith("/home_") || path.startsWith("/trial_company_save.do")){
			result = true;

		} else if(SessionUtil.getString(req, SessionUtil.LOGIN_STAFF_ID) != null) {
			result = true;
		}
		return result;
	}
	
	private boolean checkStaticRes(String path) {
		return path.endsWith(".css") || 
			   path.endsWith(".js") || 
			   path.endsWith(".jpg") ||
			   path.endsWith(".jpeg") ||
			   path.endsWith(".png") ||
			   path.endsWith(".gif");
	}
 */	
}
