package com.lenovo.iot.devicemanager.util;

import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class XSSRequestWrapper extends HttpServletRequestWrapper {
//	private static final Logger logger = Logger.getLogger(SecurityFilter.class);
	private static final String REG = "(.*\\s*)((<\\s*script\\s*)|(\\s*onwheel\\s*)|(\\s*prompt\\s*)|(<\\s*embed\\s*)|(<\\s*style\\s*)|(<\\s*img\\s*)|(<\\s*image\\s*)|(<\\s*frame\\s*)|(<\\s*object\\s*)|(<\\s*iframe\\s*)|(<\\s*a\\s*)|(<\\s*frameset\\s*)|(<\\s*meta\\s*)|(<\\s*xml\\s*)|(<\\s*applet\\s*)|(\\s*onmouse\\s*)|(<\\s*link\\s*)|(\\s*onload\\s*)|(\\s*onblur\\s*)|(\\s*onchange\\s*)|(\\s*onclick\\s*)|(\\s*ondblclick\\s*)|(\\s*onfocus\\s*)|(\\s*onkey\\s*)|(\\s*onmouseover\\s*)|(\\s*\"\\s*)|(\\s*onselect\\s*)|(\\s*'\\s*)|(\\s*alert\\s*\\())(.*\\s*)";
	private static final Pattern P =Pattern.compile(REG,Pattern.CASE_INSENSITIVE);
	public XSSRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	 @Override
	    public String[] getParameterValues(String parameter) {
//		 	logger.info("getParameterValues="+parameter);
	        String[] values = super.getParameterValues(parameter);
	        if (values == null) {
	            return null;
	        }
	        int count = values.length;
	        String[] encodedValues = new String[count];
	        for (int i = 0; i < count; i++) {
	            encodedValues[i] = stripXSS(values[i]);
	        }
	        return encodedValues;
	    }
	 
	    @Override
	    public String getParameter(String parameter) {
	        String value = super.getParameter(parameter);
/*	        
	        //判断在登录、注册、找回密码时候的密码参数key不过滤检查sUrlRegister
            if("j_password".equalsIgnoreCase(parameter)||"staff_pass".equalsIgnoreCase(parameter) 
            	|| "token".equalsIgnoreCase(parameter) || "noti_link".equalsIgnoreCase(parameter)
            	|| "device_id".equalsIgnoreCase(parameter) || "device_cid".equalsIgnoreCase(parameter)
            	|| "login_name".equalsIgnoreCase(parameter) || "admin_name".equalsIgnoreCase(parameter) 
            	|| "st".equalsIgnoreCase(parameter)){
            	return value;
            }
*/           
            return stripXSS(value);
	    }
	    
	    @Override
	    public String getHeader(String name) {
	        String value = super.getHeader(name);
	        return stripXSS(value);
	    }
	    

	    private String stripXSS(String value) {
	        if (value != null) {
	        	//value = StringEscapeUtils.escapeHtml(value); 
	        	//value = StringEscapeUtils.escapeJavaScript(value); 
	        	value = StringEscapeUtils.escapeSql(value);
	        	value = unescapeHtmlZh(value);
	        }
	       
	        return value;
	    }
	    /**
	     * 校验xss正则
	     * @param value
	     * @return
	     */
	    private boolean regularXss(String value){
	    	if(StringUtils.isBlank(value)){
	    		return false;
	    	}
	    	Matcher m = P.matcher(value);
	    	return m.find();
	    }
		
		/**
		 * 解码出经过{@link StringEscapeUtils.escapeHtml()}编码后的中文
		 * @param str
		 * @return
		 */
		public static String unescapeHtmlZh(String str) {
			StringWriter writer = new StringWriter();
			for(int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if('&' != c) {
					writer.write(c+"");
					continue;
				}
				
				// 试探一下
				if('#' != str.charAt(i+1)) {
					writer.write(c+"");
					continue;
				}
				
				int j = str.substring(i, str.length()).indexOf(";");
				if(j >= 0) {
					j = i + j;
					String s = str.substring(i+2, j);
					writer.write(Integer.valueOf(s));
					i = i + s.length() + 2;
				}
			}
			
			return writer.toString();
		}
}
