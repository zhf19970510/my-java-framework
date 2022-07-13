package com.zhf.entity.model.mail;

import com.zhf.util.FileUtil;

import javax.activation.DataSource;

/**
 * @author: 曾鸿发
 * @create: 2022-06-13 13:54
 * @description：
 **/
public class AttachedFile {
    String filePath;
    String nameInEmail;
    String url;
    String description;
    private DataSource dataSource;

    public AttachedFile(String filePath, String nameInEmail, String description) {
        this.filePath = filePath;
        this.nameInEmail = nameInEmail;
        this.description = description;
    }

    public AttachedFile(String filePath, String description) {
        this.filePath = filePath;
        this.nameInEmail = FileUtil.getFileName(filePath);
        this.description = description;
    }

    public AttachedFile(String filePath) {
        this.filePath = filePath;
        this.nameInEmail = FileUtil.getFileName(filePath);
    }

    public AttachedFile(DataSource dataSource, String nameInEmail) {
        this.dataSource = dataSource;
        this.nameInEmail = nameInEmail;
    }

    public AttachedFile(){
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getNameInEmail() {
        return nameInEmail;
    }

    public void setNameInEmail(String nameInEmail) {
        this.nameInEmail = nameInEmail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
