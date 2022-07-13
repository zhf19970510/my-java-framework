package com.zhf.entity.model.mail;

/**
 * @author: 曾鸿发
 * @create: 2022-06-13 12:31
 * @description：
 **/
public class MailModel {
    /**
     * 邮件模板Id
     */
    private Integer id;
    /**
     * 模板类型
     */
    private Integer modelType;
    /**
     * 模板名称
     */
    private String modelName;
    /**
     * 中文主题
     */
    private String chSubject;
    /**
     * 英文主题
     */
    private String enSubject;
    /**
     * 中文模板内容
     */
    private String ch;
    /**
     * 英文模板内容
     */
    private String en;
    /**
     * 最后一次修改日期
     */
    private String modifiedDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getModelType() {
        return modelType;
    }

    public void setModelType(Integer modelType) {
        this.modelType = modelType;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getChSubject() {
        return chSubject;
    }

    public void setChSubject(String chSubject) {
        this.chSubject = chSubject;
    }

    public String getEnSubject() {
        return enSubject;
    }

    public void setEnSubject(String enSubject) {
        this.enSubject = enSubject;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
