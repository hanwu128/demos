package com.lenovo.iot.digitaltwin.exception;

import java.sql.SQLException;

/**
 * @Desc: 唯一属性记录异常
 * @Name: com.lenovo.iot.digitaltwin.exception.NullRecordExeception
 * @Author: hanwu
 * @Date: 2018/9/11 13:40
 */
public class NullRecordExeception extends SQLException {

    public NullRecordExeception() {
    }

    public NullRecordExeception(String reason) {
        super(reason);
    }
}
