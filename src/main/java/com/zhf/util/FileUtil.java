package com.zhf.util;

import com.zhf.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author: 曾鸿发
 * @create: 2022-03-01 15:43
 * @description：
 **/
@Slf4j
public class FileUtil {

    /**
     * 文档文件上传
     * @param file          上传文件
     * @param targetDir     存储目录
     * @return              存储文件名称
     */
    public String uploaFile(MultipartFile file, String targetDir){
        // 新建文件路径
        File myPath = new File(targetDir);
        // 文件路径不存在则创建
        if(!myPath.exists()){
            // 创建文件
            boolean flag = myPath.mkdirs();
            if(!flag){
                throw new ServiceException("create dir error");
            }
        }
        // 判断文件是否为null
        if (file != null) {
            file.isEmpty();
        }
        // 获取文件名称
        String filename = file.getOriginalFilename();
        // 文件路径 + 文件名
        String fileLocation = targetDir + filename;
        // 记录文件保存位置
        log.info("The uploaded file is saved in " + fileLocation);
        // 获取文件
        File temp = new File(fileLocation);
        try {
            file.transferTo(temp);
        } catch (IOException e) {
            log.error("文件上传失败", e);
        }
        return filename;
    }

    /**
     * 下载文件
     * @param filePath
     * @param response
     * @param request
     */
    public static void fileDownload(String filePath, HttpServletResponse response, HttpServletRequest request){

        try {
            // path 是指欲下载的路径
            File file = new File(filePath);
            // 获取文件名
            String fileName = file.getName();
            fileDownloadByFileName(filePath, response, request, fileName, file);
        } catch (IOException e) {
            log.error(filePath + ": " + "下载失败", e);
        }
    }

    private static void fileDownloadByFileName(String filePath, HttpServletResponse response, HttpServletRequest request, String fileName, File file) throws IOException {
        if(!file.exists()){
            log.warn(filePath + "：" + "文件不存在，无法下载！");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/plain; charset=utf-8");
            try(ServletOutputStream out = response.getOutputStream();){
                out.write((filePath + ":" + "文件不存在，无法下载！").getBytes("utf-8"));
                out.flush();
            }
            return;
        }
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if(userAgent.contains("msie") || userAgent.contains("like gecko")){
            // win10 ie edge 浏览器 和其他系统的ie
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = fileName.replace("*", "%20");
            fileName = fileName.replace("%2B", "*");
        }else {
            fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
        }
        // 以流的形式下载文件
        try(InputStream fis = new BufferedInputStream(new FileInputStream(file))){
            byte[] buffer = new byte[fis.available()];
            while (fis.read(buffer) > 0){
                // 清空 response
                response.reset();
                // 设置 response 的 Header
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
                response.addHeader("Content-Length", "" + file.length());
                try(OutputStream toClient = new BufferedOutputStream(response.getOutputStream())){
                    response.setContentType("application/octet-stream");
                    toClient.write(buffer);
                    toClient.flush();
                }
            }
        }
    }

    public static void fileDownloadAndChangeFileName(String filePath, HttpServletResponse response, HttpServletRequest request, String changedFileName){
        try {
            // path 是指欲下载的文件的路径
            File file = new File(filePath);
            // 获取文件名
            if(StringUtils.isBlank(changedFileName)){
                log.warn(changedFileName + "" + "修改后的文件名不能为空");
                return;
            }
            fileDownloadByFileName(filePath, response, request, changedFileName, file);
        } catch (IOException e) {
            log.warn(filePath + ": " + "下载失败", e);
        }
    }
}
