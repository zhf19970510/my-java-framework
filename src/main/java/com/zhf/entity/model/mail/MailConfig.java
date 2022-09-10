package com.zhf.entity.model.mail;

import java.io.Serializable;
import java.util.Properties;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * mail_config
 *
 * @author
 */
public class MailConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer mailCode;

    private String mailName;

    private String mailAddress;

    private String mailUsername;

    private String mailPassword;

    private String environment;

    private String mailHost;

    private Integer mailPort;

    private String mailProtocol;

    private String mailAuth;

    private String valid;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getMailCode() {
        return mailCode;
    }

    public void setMailCode(Integer mailCode) {
        this.mailCode = mailCode;
    }

    public String getMailName() {
        return mailName;
    }

    public void setMailName(String mailName) {
        this.mailName = mailName;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public void setMailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public void setMailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getMailHost() {
        return mailHost;
    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public Integer getMailPort() {
        return mailPort;
    }

    public void setMailPort(Integer mailPort) {
        this.mailPort = mailPort;
    }

    public String getMailProtocol() {
        return mailProtocol;
    }

    public void setMailProtocol(String mailProtocol) {
        this.mailProtocol = mailProtocol;
    }

    public String getMailAuth() {
        return mailAuth;
    }

    public void setMailAuth(String mailAuth) {
        this.mailAuth = mailAuth;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    /**
     * 获取邮件配置对象
     *
     * @return 邮件配置对象
     */
    public Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", mailHost);
        properties.setProperty("mail.smtp.port", mailPort.toString());
        properties.setProperty("mail.transport.protocol", mailProtocol);
        properties.setProperty("mail.smtp.auth", mailAuth);
        properties.setProperty("mail.imap.starttls.enable", "true");
        return properties;
    }

    public Properties getReceiverProperties() {
        Properties properties = new Properties();
        if (StringUtils.equals(mailProtocol, "pop3")) {
            properties.setProperty("mail.pop3.host", mailHost);
            properties.setProperty("mail.pop3.port", mailPort.toString());
            properties.setProperty("mail.store.protocol", mailProtocol);
        } else if (StringUtils.equals(mailProtocol, "imap")) {
            properties.setProperty("mail.imap.host", mailHost);
            properties.setProperty("mail.imap.port", mailPort.toString());
            properties.setProperty("mail.imap.protocol", mailProtocol);
        } else {
            throw new RuntimeException("getReceiverProperties: unknown mail protocol");
        }
        return properties;
    }

}