package com.lenovo.iot.devicemanager.model.vo;

import java.io.Serializable;

/**
 * Created by root on 2017/7/10.
 */
public class JarVersionVo implements Serializable {

    private static final long serialVersionUID = 2768501636673916947L;

    private Integer id;

    private String jarFileName;

    private String jarVersion;

    private Integer born;

    private String jarVersionCode;

    private String jarWholeFilePath;

    private Integer uploadTime;

    private String jarStatus;

    private String ownerCode;

    private String jarFingerprint;

    private String appId;

    private String appName;

    private String jarGitPath;

    private String gitUserName;

    private String gitPassWord;

    private String uploadMacheId;

    private String desJsonString;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJarFileName() {
        return jarFileName;
    }

    public void setJarFileName(String jarFileName) {
        this.jarFileName = jarFileName;
    }

    public String getJarVersion() {
        return jarVersion;
    }

    public void setJarVersion(String jarVersion) {
        this.jarVersion = jarVersion;
    }

    public Integer getBorn() {
        return born;
    }

    public void setBorn(Integer born) {
        this.born = born;
    }

    public String getJarVersionCode() {
        return jarVersionCode;
    }

    public void setJarVersionCode(String jarVersionCode) {
        this.jarVersionCode = jarVersionCode;
    }

    public String getJarWholeFilePath() {
        return jarWholeFilePath;
    }

    public void setJarWholeFilePath(String jarWholeFilePath) {
        this.jarWholeFilePath = jarWholeFilePath;
    }

    public Integer getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Integer uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getJarStatus() {
        return jarStatus;
    }

    public void setJarStatus(String jarStatus) {
        this.jarStatus = jarStatus;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getJarFingerprint() {
        return jarFingerprint;
    }

    public void setJarFingerprint(String jarFingerprint) {
        this.jarFingerprint = jarFingerprint;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getJarGitPath() {
        return jarGitPath;
    }

    public void setJarGitPath(String jarGitPath) {
        this.jarGitPath = jarGitPath;
    }

    public String getGitUserName() {
        return gitUserName;
    }

    public void setGitUserName(String gitUserName) {
        this.gitUserName = gitUserName;
    }

    public String getGitPassWord() {
        return gitPassWord;
    }

    public void setGitPassWord(String gitPassWord) {
        this.gitPassWord = gitPassWord;
    }

    public String getUploadMacheId() {
        return uploadMacheId;
    }

    public void setUploadMacheId(String uploadMacheId) {
        this.uploadMacheId = uploadMacheId;
    }

    public String getDesJsonString() {
        return desJsonString;
    }

    public void setDesJsonString(String desJsonString) {
        this.desJsonString = desJsonString;
    }
}
