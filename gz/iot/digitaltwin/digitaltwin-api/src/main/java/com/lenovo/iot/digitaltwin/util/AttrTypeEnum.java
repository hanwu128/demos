package com.lenovo.iot.digitaltwin.util;

public enum AttrTypeEnum {
    TYPE_STATE("State"),
    TYPE_STREAM("Stream"),
    TYPE_DESCRIPTION("Description");

    private String type = "";

    private AttrTypeEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
