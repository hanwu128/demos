package com.lenovo.iot.devicemanager.model.vo;

import java.io.Serializable;

/**
 * Created by root on 2017/7/11.
 */
public class Messenger implements Serializable {

    private static final long serialVersionUID = 4338428941272029284L;

    private String ipAgentServer;

    private String portAgentServer;

    private String controllerAlias;//apacheAdgent

    private String commonJsonParameter;

    private String taskId;

    private String jarWholeFilePath;

    private String httpClinetGetOrPostFlag;

    private boolean statusCallbackFromApacheAdgetn;

    private String callbackRestfulURL;//下载接口restful URL

    private String jarFileName;

    private String commonJSONMessageStr;//

    private String topicName;

    private Integer qos;

    private String task_type;

    private String desc;

    public String getIpAgentServer() {
        return ipAgentServer;
    }

    public void setIpAgentServer(String ipAgentServer) {
        this.ipAgentServer = ipAgentServer;
    }

    public String getPortAgentServer() {
        return portAgentServer;
    }

    public void setPortAgentServer(String portAgentServer) {
        this.portAgentServer = portAgentServer;
    }

    public String getControllerAlias() {
        return controllerAlias;
    }

    public void setControllerAlias(String controllerAlias) {
        this.controllerAlias = controllerAlias;
    }

    public String getCommonJsonParameter() {
        return commonJsonParameter;
    }

    public void setCommonJsonParameter(String commonJsonParameter) {
        this.commonJsonParameter = commonJsonParameter;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getJarWholeFilePath() {
        return jarWholeFilePath;
    }

    public void setJarWholeFilePath(String jarWholeFilePath) {
        this.jarWholeFilePath = jarWholeFilePath;
    }

    public String getHttpClinetGetOrPostFlag() {
        return httpClinetGetOrPostFlag;
    }

    public void setHttpClinetGetOrPostFlag(String httpClinetGetOrPostFlag) {
        this.httpClinetGetOrPostFlag = httpClinetGetOrPostFlag;
    }

    public boolean getStatusCallbackFromApacheAdgetn() {
        return statusCallbackFromApacheAdgetn;
    }

    public void setStatusCallbackFromApacheAdgetn(boolean statusCallbackFromApacheAdgetn) {
        this.statusCallbackFromApacheAdgetn = statusCallbackFromApacheAdgetn;
    }

    public String getCallbackRestfulURL() {
        return callbackRestfulURL;
    }

    public void setCallbackRestfulURL(String callbackRestfulURL) {
        this.callbackRestfulURL = callbackRestfulURL;
    }

    public String getJarFileName() {
        return jarFileName;
    }

    public void setJarFileName(String jarFileName) {
        this.jarFileName = jarFileName;
    }

    public String getCommonJSONMessageStr() {
        return commonJSONMessageStr;
    }

    public void setCommonJSONMessageStr(String commonJSONMessageStr) {
        this.commonJSONMessageStr = commonJSONMessageStr;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Integer getQos() {
        return qos;
    }

    public void setQos(Integer qos) {
        this.qos = qos;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
