package com.hw.blog.util;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * 工具类
 */
public class CommonUtil {

    /**
     * String装Timestamp
     *
     * @param time
     * @param format
     * @return
     */
    public static Long getTimestamp(String time, String format) {
        if (StringUtils.isEmpty(time)) return null;
        if (StringUtils.isEmpty(format)) format = "yyyy-MM-dd HH:mm:ss";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Date date = simpleDateFormat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Long转String
     *
     * @param lon
     * @return
     */
    public static String longToString(Long lon) {
        if (lon == null || lon <= 0) return null;
        try {
            String.valueOf(lon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算年龄
     *
     * @param birthday
     * @return
     */
    public static Integer getAge(String birthday) {
        try {
            // 格式化传入的时间
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = format.parse(birthday);
            int age = 0;
            Calendar now = Calendar.getInstance();
            now.setTime(new Date()); // 当前时间

            Calendar birth = Calendar.getInstance();
            birth.setTime(parse); // 传入的时间

            //如果传入的时间，在当前时间的后面，返回0岁
            if (birth.after(now)) {
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 生成UUID
     *
     * @return
     */
    public static String getUUID() {
        //取消短-
        return UUID.randomUUID().toString().replace("-", "");

    }

}
