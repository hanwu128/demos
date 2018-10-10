package com.lenovo.iot.digitaltwin.util;

public enum DataTypeEnum {
    TYPE_STRING("String"),
    TYPE_BOOLEAN("Boolean"),
    TYPE_NUMBER("Number"),
    TYPE_OBJECT("Object"),
    TYPE_ARRAY("Array");

    private String type = "";

    private DataTypeEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
