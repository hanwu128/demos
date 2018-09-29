package enn.cn.dataimport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/10/26.
 */
public class DateTimeUtil {
    static Date parse(String date,String fm){
        Date res=null;
        try {
            SimpleDateFormat sft=new SimpleDateFormat(fm);
            res=sft.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

}
