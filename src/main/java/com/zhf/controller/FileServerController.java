package com.zhf.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@RestController
@RequestMapping("/file")
public class FileServerController {
    private final String FILE_PATH = "/tmp/upload/";

    @Deprecated
    // @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(HttpServletRequest request, @RequestParam("fileName") String fileName) {
        try {
            // 建立连接
            ServerSocket serverSocket = new ServerSocket(8080);
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            // 接收文件
            String filePath = FILE_PATH + fileName;
            RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");
            long startPosition = randomAccessFile.length();
            randomAccessFile.seek(startPosition);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                randomAccessFile.write(buffer, 0, len);
            }   // 计算MD5值
            FileInputStream fileInputStream = new FileInputStream(filePath);
            String md5 = DigestUtils.md5Hex(fileInputStream);
            // 返回MD5值
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(md5);        // 关闭连
            objectInputStream.close();
            inputStream.close();
            randomAccessFile.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}