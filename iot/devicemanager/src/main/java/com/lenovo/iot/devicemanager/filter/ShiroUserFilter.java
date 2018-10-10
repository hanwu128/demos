/**
 * 
 */
package com.lenovo.iot.devicemanager.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.UserFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lenovo.iot.devicemanager.model.Account;
import com.lenovo.iot.devicemanager.model.JsonResp;
import com.lenovo.iot.devicemanager.util.IotMgrConstants;
import com.lenovo.iot.devicemanager.util.LoginedAccountHolder;
import com.lenovo.iot.devicemanager.util.WebUtil;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * 扩展shiro认证拦截过滤器<br />
 * 做2件事情:<br />
 * 1. 如果账户已经登录,则将session中保存的账户信息保存在线程上下文中,在业务逻辑中使用<br />
 * 2. 如果账户未登录,覆盖默认的重定向到登录页,直接返回json响应
 * @desc com.lenovo.iot.devicemanager.filter.ShiroUserFilter
 * @author chench9@lenovo.com
 * @date 2017年11月16日
 */
public class ShiroUserFilter extends UserFilter {
	private static final Logger logger = LoggerFactory.getLogger(ShiroUserFilter.class);
	private static final Set<String> debugAnonUrlSet = new HashSet<String>();
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
	    String debug = System.getProperty("iot.debug");
	    if(debug != null && "true".equals(debug)) {
	        return checkDebugAnonUrl(request);
        }

		boolean allowed = super.isAccessAllowed(request, response, mappedValue);
		if(!allowed) {
			return allowed;
		}
		
		// 用户已经登录,需要把session中保存的登录信息保存起来
		HttpServletRequest req = (HttpServletRequest)request;
		Object account = req.getSession().getAttribute(IotMgrConstants.KEY_LOGIN_USER);
		LoginedAccountHolder.saveLoginedAccount((Account) account);
		return allowed;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		if(logger.isErrorEnabled()) {
			logger.error("account need login for: {}",  ((HttpServletRequest)request).getServletPath());
		}
		
		// session超时后，需要对请求处理跨域问题
		String origin = WebUtil.getCORSOrigin((HttpServletRequest) request);
		HttpServletResponse resp = (HttpServletResponse)response;
		resp.setHeader("Access-Control-Allow-Origin", origin);
		resp.setHeader("Access-Control-Allow-Credentials", "true");
		resp.getWriter().write(JsonResp.getJsonRespError(JsonResp.SC_NOT_LOGINED, "account not logined").toString());
		resp.getWriter().flush();
		resp.getWriter().close();
		return false;
	}

	private boolean checkDebugAnonUrl(ServletRequest request) {
	    if(debugAnonUrlSet.isEmpty()) {
            try {
                InputStream is = getClass().getClassLoader().getResourceAsStream("anon.debug");
                DataInputStream dis = new DataInputStream(is);
                String url = null;
                while((url = dis.readLine()) != null) {
                    if(!"".equals(url.trim())) {
                        debugAnonUrlSet.add(url.trim());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        HttpServletRequest req = (HttpServletRequest)request;
	    String uri = req.getRequestURI();
	    if(debugAnonUrlSet.contains(uri)) {
	        return true;
        }
        return false;
    }
}
