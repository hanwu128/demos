package com.lenovo.iot.devicemanager.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;

import com.google.gson.GsonBuilder;
import com.lenovo.iot.devicemanager.model.Account;

public class WebUtil {

	public static final String WEB_INF = "/WEB-INF/";
	
	private WebUtil(){}
		
	/**
	 * 重定向处�?
	 * @param req
	 * @param resp
	 * @param page
	 * @throws IOException
	 */
	public static void redirect(HttpServletRequest req, HttpServletResponse resp, String page) throws IOException {
		if(!page.startsWith("/") && !page.startsWith("http")) {
			page = "/" + page;
		}
		resp.sendRedirect(req.getContextPath() + page);
	}

	//Get parameter
	public static String getPara(final HttpServletRequest req, final String key){
		String para = req.getParameter(key);
		if(para==null){
			para = "";
		}
		//LogUtil.log("key="+key + ", para="+para);
		return para;
	}

	//Get parameter int
	public static int getParaInt(final HttpServletRequest req, final String key, int defaultValue){
		int result = defaultValue;
		String para = req.getParameter(key);
		if(para!=null && para.length()>0){
			result = Integer.parseInt(para);
		}
		return result;
	}
	
	public static long getParaLong(final HttpServletRequest req, final String key, long defaultValue){
		long result = defaultValue;
		String para = req.getParameter(key);
		if(para!=null && para.length()>0){
			result = Long.parseLong(para);
		}
		return result;
	}
	
	public static boolean getParaBoolean(final HttpServletRequest req, final String key, boolean defaultValue){
		boolean result = defaultValue;
		String para = req.getParameter(key);
		if(para!=null && para.length()>0){
			result = Boolean.parseBoolean(para);
		}
		return result;
	}

	//Get host
	public static String getHost(final HttpServletRequest req){
		String url = req.getRequestURL().toString();
		if(url.startsWith("http://moc")){
			url = url.replace("http://", "https://");
		}
		int p = url.indexOf("/",10);
		return url.substring(0, p);
	}

/*	
	private static final byte HEX[]={
		'1','A','J','S',
		'2','B','K','T',
		'3','C','L','U',
		'4','D','M','V',
		'5','E','N','W',
		'6','F','O','X',
		'7','G','P','Y',
		'8','H','Q','Z',
		'9','I','R','0'};
    // 为了兼容老数据，将此方法改回原来的方式�?? 
	public static String md5(String str) {
    	String result = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
       		md5.update(str.getBytes());
        	byte[] buf = md5.digest();
       		//把密文转换成十六进制的字符串形式  
            byte[] buf2 = new byte[buf.length];  
            //int k = 0;
            for (int i = 0; i < buf.length; i++) {  
                //buf2[i] = HEX[buf[i] >>> 4 & 0xf];
                buf2[i] = HEX[buf[i] & 35];
            }
            result = new String(buf2);
        } catch (RuntimeException e) {
        	log.debug("e: RuntimeException in md5().");
        } catch (NoSuchAlgorithmException e) {
        	log.debug("e: RuntimeException in md5().");
        }
        return result;
//		return md5(str.getBytes());
    }
*/
	
	public static String md5(byte[] bytes) {
		try{
    		MessageDigest md = MessageDigest.getInstance("MD5");
    	    md.update(bytes);    	    
    	    StringBuffer buf=new StringBuffer();    	    
    	    for(byte b:md.digest())
    	    	buf.append(String.format("%02x", b&0xff) );    	     
    	    return buf.toString();
    	}catch( Exception e ){
    		e.printStackTrace(); 
    		return null;
    	}
	}

	//Get list string
	public static String getString(List<String> list){
        int size = list.size();
        StringBuilder sb = new StringBuilder(size*20);
        for(int i=0;i<size;i++){
        	if(i>0){
        		sb.append(",");
        	}
        	sb.append(list.get(i));
        }
        return sb.toString();
	}
	
	//获取中文参数
	public static String newUtf8String(String para) throws UnsupportedEncodingException{
		return new String(para.getBytes("ISO8859-1"),"UTF-8");
	}
	
	//格式化时间，返回yyyy-mm-dd HH:MM:SS
	public static String format(Timestamp time){
		String s = "";
		if(time!=null){
			s = time.toString().substring(0,19);
		}
		return s;
	}
	public static String format(Long time){
	    SimpleDateFormat formatter; 
	    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	    String ctime = formatter.format(time); 
	    return ctime; 
	}
	
	/**
	 * 判断字符串是否为�?
	 * */
	public static boolean isEmpty(String str){
		if("".equals(str) || str == null){
			return true;
		}
		return false;
	}
	
	public static String listToString(List<String> list, String sign){
		StringBuilder builder = new StringBuilder();
		if(!CollectionUtils.isEmpty(list)){
			for(String str : list){
				builder.append(str).append(sign);
			}
			return builder.substring(0, builder.length()-1);
		}else{
			return builder.toString();
		}
		
	}
	
	
	/**
	 * 字符串转成list
	 * */
	public static List<String> stringToList(String str){
		String[] tmpAttr = str.split(",");
		List<String> list = new ArrayList<String>();
		for(String attr : tmpAttr ){
			list.add(attr);
		}
		return list;
	}
	
	/**
	 * 把一个有可能超过1000条数据的字符串转换成list
	 * �? 放入SQL的in�?
	 * */
	public static List<List<String>> covToGroupList(String groupStr){
		List<String> groupList = WebUtil.stringToList(groupStr);
		int size = groupList.size();
		int count = size / 1000;
		
		List<List<String>> result = new ArrayList<List<String>>();
		
		for(int i=0; i<=count; i++){
			int begin = i * 1000;
			int end = (begin+1) * 1000;
			if(end > size){
				end = size;
			}
			List<String> list = new ArrayList<String>();
			for(;begin<end;begin++){
				list.add(groupList.get(begin));
			}
			result.add(list);
		}
		return result;
	}
	/**
	 * 使用‘�?�包裹字符串，并且�?�号分割
	 * */
	public static String getStringEx(List<String> list){
        int size = list.size();
        StringBuilder sb = new StringBuilder(size*20);
        for(int i=0;i<size;i++){
        	if(i>0){
        		sb.append(",");
        	}
        	sb.append("'");
        	sb.append(list.get(i));
        	sb.append("'");
        }
        return sb.toString();
	}
	
	/**
	 * 将对象转换为json格式
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) {
		return new GsonBuilder().create().toJson(obj);
	}
	
	/**
	 * 将json对象转换为对�?
	 * @param json
	 * @param classOfT
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> classOfT) {
		return new GsonBuilder().create().fromJson(json, classOfT);
	}
	
	/**
	 * 将json字节数组转换为对�?
	 * @param bytes
	 * @param classOfT
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static <T> T fromJson(byte[] bytes, Class<T> classOfT) throws UnsupportedEncodingException {
		String json = new String(bytes, "UTF-8");
		return new GsonBuilder().create().fromJson(json, classOfT);
	}
	
	/**
	 * 将数据写给客户端
	 * @param req
	 * @param resp
	 * @param data
	 * @throws IOException 
	 */
	public static void writeToJson(HttpServletRequest req, HttpServletResponse resp, Object data) throws IOException {
		resp.getWriter().write(toJson(data));
		resp.getWriter().flush();
	}
	public static String dateToString(Date time,String formatString){ 
	    SimpleDateFormat formatter; 
	    formatter = new SimpleDateFormat(formatString); 
	    String ctime = formatter.format(time); 
	    return ctime; 
	} 
	public static String NowString(String formatString){ 
	    return dateToString(new Date(),formatString); 
	}
	
//	//返回jsonp格式数据  
//	public static void response(HttpServletRequest request, HttpServletResponse response, String result) throws Exception {
//		response.setCharacterEncoding("utf-8");
//        response.getWriter().write(result);
//
////		//JSONP
////        PrintWriter out = response.getWriter();
////        String jsonpCallback = request.getParameter("jsonpCallback");
////        out.println(jsonpCallback + "(" + result + ")");
////        out.flush();  
////        out.close();
//	}
	
	/**
	 * 设置某些字段不返回给客户端
	 * @param account
	 * @return
	 */
	public static Account formatAccount(Account account) {
		if(account == null) {
			return account;
		}
		
		Account format = new Account();
		format.setCompanyId(account.getCompanyId());
		format.setCreateTime(account.getCreateTime());
		format.setEmail(account.getEmail());
		format.setEnable(account.getEnable());
		format.setLoginName(account.getLoginName());
		format.setName(account.getName());
		format.setPhone(account.getPhone());
		format.setUpdateTime(account.getUpdateTime());
		format.setId(null);
		format.setPassword(null);
		return format;
	}
	
	/**
	 * 校验邮箱地址是否正确
	 * @param email
	 * @return
	 */
	public static boolean verifyEmail(String email) {
		if(email == null || "".equals(email.trim())) {
			return false;
		}
		String check = "^([a-z0-9A-Z]+[-|\\._]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";  
		Pattern regex = Pattern.compile(check);  
		Matcher matcher = regex.matcher(email);  
		return matcher.matches(); 
	}
	
	/**
	 * 从请求消息头中读取跨域信息
	 * @param req
	 * @return
	 */
	public static String getCORSOrigin(HttpServletRequest req) {
		if(req == null) {
			return "";
		}
		
		String origin = req.getHeader("Origin");
		if(origin == null) {
			String referer = req.getHeader("Referer");
			if(referer != null) {
				origin = referer.substring(0, referer.indexOf("/", 7));
			}
		}
		return origin;
	}
}
