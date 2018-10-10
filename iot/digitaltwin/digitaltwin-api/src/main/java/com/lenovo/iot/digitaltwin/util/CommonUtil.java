package com.lenovo.iot.digitaltwin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.digitaltwin.model.DigitalTwinInstanceAttr;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Desc: 通用工具集
 * @Name: com.lenovo.iot.digitaltwin.util.CommonUtil
 * @Author: chench9@lenovo.com
 * @Date: 2018/7/13
 */
public class CommonUtil {

    /**
     * 解析DT实例属性数组
     *
     * @param arr
     * @return
     */
    public static List<DigitalTwinInstanceAttr> parseDigitalTwinAttrList(JSONArray arr) {
        if (arr == null) {
            return null;
        }
        int size = arr.size();
        List<DigitalTwinInstanceAttr> list = new ArrayList<DigitalTwinInstanceAttr>(size);
        for (int i = 0; i < size; i++) {
            JSONObject json = arr.getJSONObject(i);
            Long id = json.getLong("id");
            String expectvalue = json.getString("expectvalue");
            //id和expectvalue不为空时添加到DT属性更新List
            if (null != id && null != expectvalue && !"".equals(expectvalue)) {
                DigitalTwinInstanceAttr attr = new DigitalTwinInstanceAttr();
                attr.setId(id);
                attr.setExpectvalue(expectvalue);
                list.add(attr);
            }
        }
        return list;
    }

    /**
     * URL编码
     *
     * @param str
     * @return
     */
    public static String urlEncode(String str) {
        if (str == null || "".equals(str.trim())) {
            return str;
        }

        try {
            str = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * String转Boolean
     *
     * @param str
     * @return boolean
     */
    public static boolean str2Boolean(String str) {
        if (str == null || "".equals(str.trim())) {
            return false;
        }
        try {
            return Boolean.valueOf(str);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * String转Integer
     *
     * @param str
     * @return Integer
     */
    public static Integer str2Integer(String str) {
        if (str == null || "".equals(str.trim())) {
            return -1;
        }
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * String转Long
     *
     * @param str
     * @return Long
     */
    public static Long str2Long(String str) {
        if (str == null || "".equals(str.trim())) {
            return 0L;
        }
        try {
            return Long.valueOf(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * Object转Boolean
     *
     * @param obj
     * @return Boolean
     */
    public static Boolean obj2Boolean(Object obj) {
        if (obj == null || "".equals(obj)) {
            return false;
        }
        try {
            return Boolean.valueOf(String.valueOf(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Object转String
     *
     * @param obj
     * @return String
     */
    public static String obj2String(Object obj) {
        if (obj == null || "".equals(obj)) {
            return null;
        }
        try {
            return String.valueOf(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * String转Double
     *
     * @param str
     * @return double
     */
    public static double str2Double(String str) {
        if (str == null || "".equals(str.trim())) {
            return 0d;
        }
        try {
            return Double.valueOf(str);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return 0d;
    }

    /**
     * String转JSONObject
     *
     * @param str
     * @return JSONObject
     */
    public static JSONObject str2JSONObject(String str) {
        if (str == null || "".equals(str.trim())) {
            return new JSONObject();
        }
        try {
            return JSONObject.parseObject(str);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    /**
     * String转JSONArray
     *
     * @param str
     * @return JSONArray
     */
    public static JSONArray str2JSONArray(String str) {
        if (str == null || "".equals(str.trim())) {
            return new JSONArray();
        }
        try {
            return JSONObject.parseArray(str);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    /**
     * Object转JSON
     *
     * @param obj
     * @return
     */
    public static JSON obj2JSON(Object obj) {
        if (obj == null || "".equals(obj)) {
            return new JSONObject();
        }

        try {
            return (JSON) JSONObject.toJSON(obj);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static String formatDtName(String str) {
        if (str == null || "".equals(str.trim())) {
            return str;
        }

        try {
            str = str.replaceAll(" ", "_");
            str = str.replaceAll("\r", "_");
            str = str.replaceAll("\n", "_");
            str = str.replaceAll("\t", "_");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private CommonUtil() {
    }

    /**
     * Map根据Key排序
     *
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    public static class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

    /**
     * 时间戳转换时间
     *
     * @param ms     时间戳为毫秒级，否则格式化会出错
     * @param format 格式化样式
     * @return
     */
    public static String timeStamp2Date(String ms, String format) {
        if (ms == null || ms.isEmpty() || ms.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(ms)));
    }

    /**
     * 判断字符串是否为数值
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        try {
            Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]+)?$");
            Matcher isNum = pattern.matcher(str);
            if (!isNum.matches()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否含有特殊字符包含全部
     *
     * @param str
     * @return
     */
    public static boolean isSpecialChar(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        try {
            String regex = "[`~!@#$%^&*()_+=|{}',:;\\-\\[\\].<>/?！￥（）—【】‘；：”“’。，、？]";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            return matcher.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 参数是否含有特殊字符，排除符号-（中横杠）
     *
     * @param str
     * @return
     */
    public static boolean isSpecialCharOutRung(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        try {
            String regex = "[`~!@#$%^&*()_+=|{}',:;\\[\\].<>/?！￥（）—【】‘；：”“’。，、？]";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            return matcher.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 参数是否含有特殊字符，排除符号_（下划线）
     *
     * @param str
     * @return
     */
    public static boolean isSpecialCharOutUnderline(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        try {
            String regex = "[`~!@#$%^&*()+=|{}',:;\\-\\[\\].<>/?！￥（）—【】‘；：”“’。，、？]";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            return matcher.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否含有特殊字符，排除,（英文逗号）
     *
     * @param str
     * @return
     */
    public static boolean isSpecialCharOutComma(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        try {
            String regex = "[`~!@#$%^&*()_+=|{}':;\\-\\[\\].<>/?！￥（）—【】‘；：”“’。，、？]";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            return matcher.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 使用正则表达式来判断字符串中是否包含字母
     *
     * @param str
     * @return
     */
    public static boolean isContainsLetter(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }

        try {
            String regex = ".*[a-zA-Z]+.*";
            Matcher matcher = Pattern.compile(regex).matcher(str);
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断字符串中是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        try {
            Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher matcher = pattern.matcher(str);
            return matcher.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断Object是否为Boolean数据
     *
     * @param obj
     * @return Boolean
     */
    public static Boolean isBoolean(Object obj) {
        if (obj == null || "".equals(obj)) {
            return false;
        }
        try {
            String b = obj2String(obj);
            if ("true".equalsIgnoreCase(b) || "false".equalsIgnoreCase(b)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断Object是否为Array
     *
     * @param obj
     * @return Boolean
     */
    public static Boolean isArray(Object obj) {
        if (obj == null || "".equals(obj)) {
            return false;
        }
        try {
            JSONArray json = JSONObject.parseArray(obj2String(obj));
            if (json != null && json.size() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断Object是否为Object
     *
     * @param obj
     * @return Boolean
     */
    public static Boolean isObject(Object obj) {
        if (obj == null || "".equals(obj)) {
            return false;
        }
        try {
            JSONObject json = JSONObject.parseObject(obj2String(obj));
            if (json != null && json.size() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
