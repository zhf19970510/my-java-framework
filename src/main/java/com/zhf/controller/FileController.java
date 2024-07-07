package com.zhf.controller;

import cn.hutool.core.io.file.FileNameUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/file")
public class FileController {
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileName") String fileName,
            @RequestParam("startPosition") long startPosition) {
        try {            // 建立连接
            Socket socket = new Socket("localhost", 8080);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            // 分块传输文件
            FileInputStream fileInputStream = (FileInputStream) file.getInputStream();
            fileInputStream.skip(startPosition);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            // 计算MD5值
            fileInputStream.getChannel().position(0);
            String md5 = DigestUtils.md5Hex(fileInputStream);
            // 与服务端比较MD5值
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            String serverMd5 = (String) objectInputStream.readObject();
            if (!md5.equals(serverMd5)) {
                throw new RuntimeException("MD5值不匹配");
            }
            // 关闭连接
            objectOutputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    // 存放文件的临时目录
    private static final String DATA_DIR = "D:\\project_file_upload_path\\" + "my_java_frame_work";
    // 文件MD5的缓存容器
    private static final ConcurrentMap<String, File> MD5_CACHE = new ConcurrentHashMap<>();


    /**
     * 大文件分片上传
     *
     * @param name          文件名
     * @param md5           文件MD5值
     * @param size          文件大小
     * @param chunks        总的分片数
     * @param chunk         当前分片数
     * @param multipartFile 分片流
     * @throws IOException
     */
    @PostMapping("/chunk/upload")
    public void chunkUpload(String name,
                            String md5,
                            Long size,
                            Integer chunks,
                            Integer chunk,
                            @RequestParam("file") MultipartFile multipartFile) throws IOException {
        // 是否生成了文件？？？
        File targetFile = MD5_CACHE.get(md5);
        if (targetFile == null) {
            // 没有生成的话就生成一个新的文件，没有做并发控制，多线程上传会出问题
            targetFile = new File(DATA_DIR, UUID.randomUUID().toString() + "." + FileNameUtil.extName(name));
            targetFile.getParentFile().mkdirs();
            MD5_CACHE.put(md5, targetFile);
        }

        // 可以对文件的任意位置进行读写
        RandomAccessFile accessFile = new RandomAccessFile(targetFile, "rw");
        boolean finished = chunk == chunks;//是否最后一片
        if (finished) {
            // 移动指针到指定位置
            accessFile.seek(size - multipartFile.getSize());
        } else {
            accessFile.seek((chunk - 1) * multipartFile.getSize());
        }
        // 写入分片的数据
        accessFile.write(multipartFile.getBytes());
        accessFile.close();

        if (finished) {
            System.out.println("success.");
            // 上传成功
            MD5_CACHE.remove(md5);
        }
    }
}