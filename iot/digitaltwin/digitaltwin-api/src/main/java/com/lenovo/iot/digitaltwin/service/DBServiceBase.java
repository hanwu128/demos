package com.lenovo.iot.digitaltwin.service;

public abstract class DBServiceBase {
    public abstract String getStatement(String id);
    public String getStatement(String namespace, String id){
        return namespace + "." + id;
    }
}
