package com.zhf.controller;

import cn.hutool.core.io.IoUtil;
import com.zhf.executor.CommandExecutor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Controller
@RequestMapping("/file")
public class FileController {

    @Value("${spring.profiles.active}")
    private String profile;

    @ResponseBody
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
    @ResponseBody
    @PostMapping("/chunk/upload")
    public void chunkUpload(String name,
                            String md5,
                            Long size,
                            Integer chunks,
                            Integer chunk,
                            String dirName,
                            @RequestParam("file") MultipartFile multipartFile) throws IOException {
        String targetDir = null;
        if ("dev".equals(profile) || (StringUtils.isBlank(dirName) || File.separator.equals(dirName.trim()))) {
            targetDir = DATA_DIR;
        } else {
            dirName = dirName.trim();
            if (dirName.startsWith(File.separator)) {
                targetDir = DATA_DIR + dirName;
            } else {
                targetDir = DATA_DIR + File.separator + dirName;
            }
        }

        // 是否生成了文件？？？
        File targetFile = MD5_CACHE.get(md5);
        if (targetFile == null) {
            targetFile = new File(DATA_DIR, name);
            targetFile.getParentFile().mkdirs();
            if (targetFile.exists()) {
                targetFile.delete();
            }
            MD5_CACHE.put(md5, targetFile);
        }

        // 可以对文件的任意位置进行读写
        RandomAccessFile accessFile = new RandomAccessFile(targetFile, "rw");
        boolean finished = Objects.equals(chunk, chunks);//是否最后一片
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
            System.out.println("================upload file successfully================");
            // 上传成功
            MD5_CACHE.remove(md5);
            // linux 可能需要执行以下几行代码，对文件进行重命名，实际以测试情况为准，主要是结合搭建nginx文件服务器的时间会显示乱码
            if (!StringUtils.equals("dev", profile)) {
                String command = getCommand(targetDir, name, dirName);
                CommandExecutor executor = new CommandExecutor();
                executor.executeCommand(command);
            }
        }
    }

    public String getCommand(String targetDir, String name, String dirName) {
        String command = "convmv -f UTF8 -t GBK --notest";
        if (DATA_DIR.equals(targetDir)) {
            command += targetDir + "/" + name;
        } else {
            // 上传文件支持上传到子层目录

            command = "convmv -f UTF8 -t GBK -r --notest";
            int targetIndex = dirName.startsWith("/") ? 1 : 0;
            StringBuilder sb = new StringBuilder();
            for (int i = targetIndex; i < dirName.length(); i++) {
                char ch = dirName.charAt(i);
                if (ch == '/') {
                    break;
                }
                sb.append(ch);
            }
            command = DATA_DIR + File.separator + sb;
        }
        return command;
    }

    @ResponseBody
    @GetMapping("/download")
    public void downloadFileByFileName(@RequestParam("file") String fileName, HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("content-Disposition", "attachment;filename==" + fileName);
        ServletOutputStream outputStream = response.getOutputStream();
        InputStream inputStream = new FileInputStream(new File(DATA_DIR, fileName));
        IoUtil.copy(inputStream, outputStream);
        outputStream.flush();
        inputStream.close();
    }

    @RequestMapping("/shardUpload")
    public String shardUpload() {
        return "Shard_upload";
    }
}