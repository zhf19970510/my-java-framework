package com.zhf.entity.model.mail;

import java.io.Serializable;

/**
 * mail_list
 *
 * @author 曾鸿发
 */
public class MailList implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String mailAddress;

    private String receiverType;

    /**
     * 收件人类型2
     */
    private String receiverType2;

    /**
     * 收件人类型3
     */
    private String receiverType3;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public String getReceiverType2() {
        return receiverType2;
    }

    public void setReceiverType2(String receiverType2) {
        this.receiverType2 = receiverType2;
    }

    public String getReceiverType3() {
        return receiverType3;
    }

    public void setReceiverType3(String receiverType3) {
        this.receiverType3 = receiverType3;
    }
}