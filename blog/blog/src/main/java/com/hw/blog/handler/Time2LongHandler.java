package com.hw.blog.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * 将时间戳转换为数据格式
 **/
public class Time2LongHandler extends BaseTypeHandler<Long> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Long parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter);
    }

    @Override
    public Long getNullableResult(ResultSet rs, String columnName) throws SQLException {
        try {
            // 返回的时间戳不准确
            /*Date date = rs.getDate(columnName);
            if(date != null) {
                return date.getTime();
            }*/
            return rs.getTimestamp(columnName, Calendar.getInstance()).getTime();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public Long getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            /*Date date = rs.getDate(columnIndex);
            if(date != null) {
                return date.getTime();
            }*/
            return rs.getTimestamp(columnIndex, Calendar.getInstance()).getTime();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public Long getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        try {
            /*Date date = cs.getDate(columnIndex);
            if(date != null) {
                return date.getTime();
            }*/
            return cs.getTimestamp(columnIndex, Calendar.getInstance()).getTime();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return 0L;
    }
}
