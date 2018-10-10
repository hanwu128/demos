package com.lenovo.iot.digitaltwin.exception;

import java.sql.SQLException;

/**
 * @Desc: 唯一属性记录异常
 * @Name: com.lenovo.iot.digitaltwin.exception.UniqueAttrRecordExeception
 * @Author: hanwu
 * @Date: 2018/9/11 13:40
 */
public class UniqueAttrRecordExeception extends SQLException {

    public UniqueAttrRecordExeception() {
    }

    public UniqueAttrRecordExeception(String reason) {
        super(reason);
    }
}
