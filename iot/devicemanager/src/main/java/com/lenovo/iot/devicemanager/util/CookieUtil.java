package com.lenovo.iot.devicemanager.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class CookieUtil {
	private static final String TAG = "CookieUtil";
	private static final String DES_KEY = "qwertyuiopasdfghjklzxcvbnm";
	// 使用REDIS进行共享session处理

	private static Logger log = Logger.getLogger("fordebug");

	public static final String LOGIN_COMPANY_ID = "login_company_id";

	private CookieUtil() { }

	// Getter
	public static String getString(HttpServletRequest req, String key) {
		String str = null;
		Cookie[] ck = req.getCookies();
		if (ck != null) {
			try {
				String name = Md5.encryption(key);
				for (Cookie c : ck) {
					if (c.getName().equals(name)) {
						str = DesUtil.decrypt(URLDecoder.decode(c.getValue(), "UTF-8"), DES_KEY);
						break;
					}
				}
			} catch (Exception e) {
				log.info(TAG + ":Exception in getString().");
			}
		}
		return str;
	}

	// Setter
	public static void setString(HttpServletResponse resp, String key, Object obj) {
		try {
			String name = Md5.encryption(key);
			// String s = URLEncoder.encode(obj.toString(),"UTF-8");
			String encrypt = DesUtil.encrypt(obj.toString(), DES_KEY);
			Cookie c = new Cookie(name, URLEncoder.encode(encrypt, "UTF-8"));
			c.setMaxAge(-1);// 关闭浏览器后，cookie立即失效
			c.setPath("/");// 项目所有目录均有效
			resp.addCookie(c);// 覆盖之前的
		} catch (Exception e) {
			log.info(TAG + ":Exception in setString().");
		}
	}

	// Remove
	public static void removeString(HttpServletResponse resp, String key) {
		try {
			String name = Md5.encryption(key);
			Cookie c = new Cookie(name, null);
			c.setMaxAge(0);// 立即删除型
			c.setPath("/");// 项目所有目录均有效，这句很关键，否则不敢保证删除
			resp.addCookie(c);// 覆盖之前的
		} catch (Exception e) {
			log.info(TAG + ":Exception in removeString().");
		}
	}
}
