package com.lenovo.iot.digitaltwin.exception;

import java.sql.SQLException;

/**
 * @Desc: 唯一记录异常
 * @Name: com.lenovo.iot.digitaltwin.exception.UniqueRecordExeception
 * @Author: chench9@lenovo.com
 * @Date: 2018/9/7
 */
public class UniqueRecordExeception extends SQLException {

    public UniqueRecordExeception() {
    }

    public UniqueRecordExeception(String reason) {
        super(reason);
    }
}
