package com.lenovo.iot.devicemanager.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class SessionUtil {
	private static final String TAG = "SessionUtil";
	private static final String DES_KEY = "qwertyuiopasdfghjklzxcvbnm";
	// 使用REDIS进行共享session处理

	private static Logger log = Logger.getLogger("fordebug");

	public static final String LOGIN_COMPANY_ID = "login_company_id";

	private SessionUtil() { }

	// Getter
	/**
	 * 保存对象到session
	 * 
	 * @param req
	 * @param key
	 * @param value
	 */
	public static void save(HttpServletRequest req, String key, Object value) {
		req.getSession().setAttribute(key, value);
	}

	/**
	 * 从session中取对象
	 * 
	 * @param req
	 * @param key
	 * @return
	 */
	public static Object get(HttpServletRequest req, String key) {
		return req.getSession().getAttribute(key);
	}

}
