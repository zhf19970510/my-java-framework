package com.zhf.util.ftpdownload;

import com.jcraft.jsch.ChannelSftp;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author: 曾鸿发
 * @create: 2022-06-24 09:00
 * @description：
 **/
public interface IFtpDownloadService {

    interface FileHandler {
        void handle(String fileName, InputStream in, FTPFile file) throws IOException;

        default void handleFileNotFound() {
            return;
        }

        default String handleNULL(String nullString) {
            if (StringUtils.isEmpty(nullString) || "NULL".equalsIgnoreCase(nullString)) {
                return null;
            }
            return nullString.trim();
        }

        // an all at one function to handle all the files in directory
        default void process(FTPFile[] files, FTPClient client, ChannelSftp sftpChannel) throws IOException{
            return;
        }



    }

}
