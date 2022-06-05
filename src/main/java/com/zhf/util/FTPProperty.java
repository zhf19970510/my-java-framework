package com.zhf.util;

import java.io.Serializable;

/**
 * @author: 曾鸿发
 * @create: 2022-06-04 20:07
 * @description：
 **/
public class FTPProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ftpHost;

    private String ftpUserName;

    private String ftpPassword;

    private int ftpPort;

    private String ftpPath;

    private String ftpEncoding = "UTF-8";

    private String model;

    public FTPProperty() {
        this.model = models.PASSIVE.value();
    }

    public String getFtpHost() {
        return ftpHost;
    }

    public void setFtpHost(String ftpHost) {
        this.ftpHost = ftpHost;
    }

    public String getFtpUserName() {
        return ftpUserName;
    }

    public void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public String getFtpEncoding() {
        return ftpEncoding;
    }

    public void setFtpEncoding(String ftpEncoding) {
        this.ftpEncoding = ftpEncoding;
    }

    public String getModel() {
        return model;
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public static enum models {
        PASSIVE("passive"),
        ACTIVE("active");

        private String value;

        private models(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }
}
