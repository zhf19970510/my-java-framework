package com.zhf.util.ftpdownload;

/**
 * @author: 曾鸿发
 * @create: 2022-06-24 08:50
 * @description：
 **/
public class FtpConfig {
    /**
     * 主机ip
     */
    private String host;
    /**
     * 主机用户名
     */
    private String username;
    /**
     * 主机密码
     */
    private String password;
    /**
     * 端口
     */
    private String port;
    /**
     * 主机文件路径
     */
    private String ftpPath;
    /**
     * 文件名称
     */
    private String filename;
    /**
     * 秘钥文件必填 eg:D:\\xxx\\test\id_rsa
     */
    private String privateKeyFile;
    /**
     * 协议默认ftp
     */
    private String protocol;
    /**
     * FTPSClient, FTPClient
     */
    private String clientType;

    public FtpConfig(String host, String username, String ftpPath, String clientType) {
        this.host = host;
        this.username = username;
        this.ftpPath = ftpPath;
        this.clientType = clientType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    @Override
    public String toString() {
        return "FtpConfig{" +
                "host='" + host + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", port='" + port + '\'' +
                ", ftpPath='" + ftpPath + '\'' +
                ", filename='" + filename + '\'' +
                ", privateKeyFile='" + privateKeyFile + '\'' +
                ", protocol='" + protocol + '\'' +
                ", clientType='" + clientType + '\'' +
                '}';
    }
}
