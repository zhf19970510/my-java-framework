package com.zhf.util;

import cn.hutool.core.lang.Snowflake;
import com.zhf.util.FTPProperty.models;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 曾鸿发
 * @create: 2022-06-04 20:29
 * @description：
 **/
public class FTPUtils {
    private static final Logger log = LoggerFactory.getLogger(FTPUtils.class);
    private static final long monitorInternal = 5000L;

    public FTPUtils() {
    }

    public static FTPClient getFTPClient(FTPProperty property) {
        FTPClient ftpClient = null;
//        Snowflake snowflake = new Snowflake();
        try {
            ftpClient = new FTPClient();
            ftpClient.setDefaultTimeout(300000);
            ftpClient.setConnectTimeout(600000);
            ftpClient.setDataTimeout(600000);
            ftpClient.setControlEncoding(property.getFtpEncoding());
            // 如果是window，就改为 FTPClientConfig.SYST_NT
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
            conf.setServerLanguageCode("zh");
            ftpClient.configure(conf);
            if (models.ACTIVE.value().equals(property.getModel())) {
                ftpClient.enterLocalActiveMode();
                log.info("Enter active mode.");
            } else {
                ftpClient.enterLocalPassiveMode();
                log.info("Enter passive mode.");
            }
            ftpClient.connect(property.getFtpHost(), property.getFtpPort());
            ftpClient.login(property.getFtpUserName(), property.getFtpPassword());
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                log.error("FTP连接失败！" + ftpClient.getReplyString());
                ftpClient.disconnect();
            } else {
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                if (property.getFtpPath() != null && !ftpClient.changeWorkingDirectory(property.getFtpPath())) {
                    log.warn("FTP路径切换失败，请检查路径是否正确：" + property.getFtpPath());
                    ftpClient.disconnect();
                    return null;
                }

                ftpClient.setBufferSize(1024);
                log.info("FTP连接成功！");
            }
        } catch (Exception var3) {
            log.error("FTP连接错误！", var3);
        }
        return ftpClient;
    }

    public static boolean isFtpFileReady(FTPProperty property, List<String> configs) {
        FTPClient ftpClient = null;

        try {
            ftpClient = getFTPClient(property);
            if (ftpClient == null) {
                return false;
            } else {
                for (int i = 0; i < configs.size(); i++) {
                    InputStream inputStream = ftpClient.retrieveFileStream(configs.get(i));
                    if (inputStream == null || ftpClient.getReplyCode() == 550) {
                        log.info(configs.get(i) + " is not avaiable for now.");
                        return false;
                    }

                    log.info(configs.get(i) + " is available.");
                    inputStream.close();
                    ftpClient.completePendingCommand();
                }

                ftpClient.logout();
                return true;
            }

        } catch (Exception var5) {
            log.error("Check FTP Status failed.", var5);
            return false;
        }
    }

    public static boolean downloadFtpFile(FTPProperty property, List<String> configs, List<String> paths, long monitorDuration) {
        if (monitorDuration <= 0L) {
            monitorDuration = 30000L;
        }

        try {
            FTPClient ftpClient = getFTPClient(property);
            if (ftpClient == null) {
                log.warn("FTPClient获取失败！");
                return false;
            } else {
                long startTime = System.currentTimeMillis();
                FTPFile[] listFiles = ftpClient.listFiles();
                if (listFiles != null && listFiles.length > 0) {
                    log.info("开始下载FTP文件...");
                    for (int i = 0; i < configs.size(); i++) {
                        String fileName = configs.get(i);
                        FTPFile ftpFile = null;
                        FTPFile[] var12 = listFiles;
                        int var13 = listFiles.length;

                        for (int var14 = 0; var14 < var13; ++var14) {
                            FTPFile file = var12[var14];
                            if (file.getName().equals(fileName)) {
                                ftpFile = file;
                                break;
                            }
                        }

                        if (ftpFile == null) {
                            log.warn(fileName + " is not found!");
                        } else {
                            if (System.currentTimeMillis() - startTime > monitorDuration) {
                                while (i < configs.size()) {
                                    log.error("TimeOutFile " + configs.get(i) + " is not download!");
                                    ++i;

                                }
                                return false;
                            }
                            log.info("正在下载：" + ftpFile);
                            String filePath;
                            if (paths.size() == 1) {
                                filePath = paths.get(0);
                            } else {
                                filePath = paths.get(i);
                            }
                            File backupFile = new File(filePath + fileName);
                            if (!backupFile.getParentFile().exists()) {
                                backupFile.getParentFile().mkdirs();
                            }
                            try {
                                OutputStream os = new FileOutputStream(backupFile);
                                Throwable var35 = null;
                                try {
                                    if (ftpClient.retrieveFile(fileName, os) && FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                                        if (ftpFile.getSize() != backupFile.length()) {
                                            log.warn(String.format("FTP文件下载失败！FileName:%s, Error:%s", fileName, "本地文件大小与远程文件大小不一致！local:" + backupFile.length() + ";remote:" + ftpFile.getSize()));
                                            configs.add(fileName);
                                            configs.remove(i);
                                            Thread.sleep(5000L);
                                            --i;
                                        } else {
                                            os.flush();
                                            log.info("File " + fileName + " download. ");
                                        }
                                    } else {
                                        log.warn(String.format("FTP文件下载失败！ FileName:%s, Error:%s", fileName, ftpClient.getReplyString()));
                                        configs.add(fileName);
                                        configs.remove(i);
                                        Thread.sleep(5000L);
                                        --i;
                                    }
                                } catch (Throwable var28) {
                                    var35 = var28;
                                    throw var28;
                                } finally {
                                    if (os != null) {
                                        if (var35 != null) {
                                            try {
                                                os.close();
                                            } catch (Throwable var27) {
                                                var35.addSuppressed(var27);
                                            }
                                        } else {
                                            os.close();
                                        }
                                    }
                                }
                            } catch (Throwable var30) {
                                log.warn(String.format("FTP文件下载异常！FileName:%s", fileName), var30);
                                configs.add(fileName);
                                configs.remove(i);
                                Thread.sleep(5000L);
                                --i;
                            }
                        }
                    }
                    ftpClient.logout();
                    ftpClient.disconnect();
                    return true;

                } else {
                    log.warn("该文件夹下没有文件");
                    return false;
                }
            }
        } catch (Exception var31) {
            log.error("FTP文件下载出现异常！", var31);
            return false;
        }
    }

    public static List<String> listFile(FTPProperty property) {
        FTPClient ftpClient = getFTPClient(property);
        ArrayList fileNames = new ArrayList();

        try {
            if (ftpClient == null) {
                return fileNames;
            }
            FTPFile[] listFiles = ftpClient.listFiles();
            FTPFile[] var4 = listFiles;
            int var5 = listFiles.length;
            for (int var6 = 0; var6 < var5; ++var6) {
                FTPFile f = var4[var6];
                if (f.isFile()) {
                    fileNames.add(f.getName());
                }
            }
        } catch (IOException var8) {
            log.error("FTP ListFile Error!", var8);
        }
        return fileNames;
    }

    public static boolean upload(FTPProperty property, String fileName, File file) {
        boolean flag = false;
        try {
            FileInputStream input = new FileInputStream(file);
            Throwable var5 = null;
            boolean var7;
            try {
                log.info("CommonUtils start to upload ftp file: " + fileName);
                FTPClient ftpClient = getFTPClient(property);
                if (ftpClient != null) {
                    ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                    log.info("CommonUtils start to store ftp file");
                    if (models.ACTIVE.value().equals(property.getModel())) {
                        ftpClient.enterLocalActiveMode();
                        log.info("Enter active mode again.");
                    } else {
                        ftpClient.enterLocalPassiveMode();
                        log.info("Enter passive mode again.");
                    }

                    flag = ftpClient.storeFile(fileName, input);
                    if (flag) {
                        log.info("CommonUtils store ftp file success");
                        return flag;
                    }

                    log.error("FTP上传失败！ FileName:" + fileName + " ErrorL:" + ftpClient.getReplyString());
                    var7 = false;
                    return var7;
                }

                var7 = false;
            } catch (Throwable var19) {
                var5 = var19;
                throw var19;
            } finally {
                if (input != null) {
                    if (var5 != null) {
                        try {
                            input.close();
                        } catch (Throwable var18) {
                            var5.addSuppressed(var18);
                        }
                    } else {
                        input.close();
                    }
                }

            }

            return var7;
        } catch (IOException var21) {
            log.error("FTP上传异常", var21);
            return flag;
        }
    }

    /**
     * 从FTP服务器下载文件
     *
     * @param ftpHost     FTP IP地址
     * @param ftpUserName FTP 用户名
     * @param ftpPassword FTP 用户名密码
     * @param ftpPort     FTP 端口
     * @param ftpPath     FTP服务器文件所在路径 格式：/ftptest/aa
     * @param localPath   下载到本地的位置    格式： H:/download
     * @param fileName    文件名称
     * @return
     */
    public static boolean downloadFtpFile(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort, String ftpPath, String localPath, String fileName) {
        boolean flag = false;
        FTPClient ftpClient = null;
        OutputStream os = null;
        try {
            FTPProperty property = new FTPProperty();
            property.setFtpHost(ftpHost);
            property.setFtpPassword(ftpPassword);
            property.setFtpPath(ftpPath);
            property.setFtpPort(ftpPort);
            property.setFtpUserName(ftpUserName);
            property.setModel(models.PASSIVE.value());
            ftpClient = getFTPClient(property);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (file.getName().startsWith(fileName)) {
                    File localFile = new File(localPath + File.separatorChar + file.getName());
                    os = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(file.getName(), os);
                }
            }
            ftpClient.logout();
            flag = true;

        } catch (Exception e) {
            log.error("downloadFile fail!", e);
        } finally {
            if (ftpClient != null && ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    log.error("ftpClient disconnect fail! ", e);
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.error("io close fail! ", e);
                }
            }
        }
        return flag;
    }

    public static boolean uploadFile(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort, String ftpPath, String fileName, InputStream input) {
        boolean success = false;
        FTPClient ftpClient = null;
        try {

            FTPProperty property = new FTPProperty();
            property.setFtpHost(ftpHost);
            property.setFtpPassword(ftpPassword);
            property.setFtpPath(ftpPath);
            property.setFtpPort(ftpPort);
            property.setFtpUserName(ftpUserName);
            property.setModel(models.PASSIVE.value());
            ftpClient = getFTPClient(property);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return success;
            }
            success = ftpClient.storeFile(fileName, input);
            input.close();
            ftpClient.logout();
            success = true;
        } catch (Exception e) {
            log.error("upload file fail! ", e);
        } finally {
            if (ftpClient != null && ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    log.error("ftpClient disconnect fail! ", e);
                }
            }

            if (null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    log.error("io close fail! ", e);
                }
            }
        }
        return success;
    }

    public static boolean generateCSVAndUploadFileWithMd5(String fileName, FTPProperty property, List<Map<String, Object>> contentValueList, LinkedHashMap<String, String> headerMap, String tempPath, String fileEncode) throws IOException, FileErrorException {
        String fileNameCSV = fileName + ".csv";
        String fileNameMD5 = fileName + ".md5";
        // generate original file and upload
        OutExcelUtil.generateCSVAndUploadFTP(contentValueList, headerMap, property, tempPath, fileNameCSV, FileUtil.GenerateType.COVER, ",", fileEncode);
        String md5 = FileUtil.getMD5ByFilePath(tempPath + File.separator + fileNameCSV);
        // Generate temp file with md5 inside
        File md5File = FileUtil.generateFile(md5, fileEncode, tempPath, fileNameMD5, FileUtil.GenerateType.COVER);
        // Upload md5 file
        return FTPUtils.upload(property, fileNameMD5, md5File);
    }

    public static boolean generateCSVAndUploadFileWithMd5(String fileName, FTPProperty property, List<Map<String, Object>> contentValueList, String tempPath, String fileEncode) throws IOException, FileErrorException {
        String fileNameCSV = fileName + ".csv";
        String fileNameMD5 = fileName + ".md5";
        // generate original file and upload
        OutExcelUtil.generateCSVAndUploadFTP(contentValueList, property, tempPath, fileNameCSV, FileUtil.GenerateType.COVER, ",", fileEncode);
        String md5 = FileUtil.getMD5ByFilePath(tempPath + File.separator + fileNameCSV);
        // Generate temp file with md5 inside
        File md5File = FileUtil.generateFile(md5, fileEncode, tempPath, fileNameMD5, FileUtil.GenerateType.COVER);
        // Upload md5 file
        return FTPUtils.upload(property, fileNameMD5, md5File);
    }

    public static void closeFtp(FTPClient ftpClient) {
        if(ftpClient != null && ftpClient.isConnected()){
            try {
                ftpClient.logout();
            } catch (IOException e) {
                log.error("关闭FTP服务器异常");
            }finally {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    log.error("关闭FTP服务器连接异常");
                }
            }
        }
    }

}
