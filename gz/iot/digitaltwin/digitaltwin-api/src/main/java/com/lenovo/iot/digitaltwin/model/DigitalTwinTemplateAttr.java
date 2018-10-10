package com.lenovo.iot.digitaltwin.model;

import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * Desc: DT模板属性
 * Name: com.lenovo.iot.digitaltwin.model.DigitalTwinTemplateAttr
 * Author: chench9@lenovo.com
 * Date: 2018/6/5 16:12
 **/
@Alias("DigitalTwinTemplateAttr")
public class DigitalTwinTemplateAttr extends AttrBase {
    private Long tpl = 0L;

    public Long getTpl() {
        return tpl;
    }

    public void setTpl(Long tpl) {
        this.tpl = tpl;
    }
}
