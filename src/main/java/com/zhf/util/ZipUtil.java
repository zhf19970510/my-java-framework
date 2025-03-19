package com.zhf.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipUtil {

    public static void compressToZip(String tempFilePath, @NotNull String zipFileName, HttpServletResponse response) {
        File dir = new File(tempFilePath);
        List<File> fileList = new ArrayList<>();
        if(dir.isDirectory()) {
            getAllFile(dir, fileList);
        } else {
            fileList.add(dir);
        }

        ZipOutputStream zipOut = null;
        OutputStream os = null;
        BufferedInputStream bis = null;
        try {
            os = response.getOutputStream();
            zipOut = new ZipOutputStream(os);
            String filenameEncoder = URLEncoder.encode(zipFileName + ".zip", "utf-8");
            response.setContentType("application/octet-stream;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + filenameEncoder);
            List<String> fileNameList = new ArrayList<>();
            for (File file : fileList) {
                InputStream in = Files.newInputStream(file.toPath());
                bis = new BufferedInputStream(in);
                String newFileName = handleDuplicateFileName(file.getName(), fileNameList);
                fileNameList.add(file.getName());
                ZipEntry entry = new ZipEntry(zipFileName + File.separator + newFileName);
                zipOut.putNextEntry(entry);
                byte[] buffer = new byte[1024];
                int len;
                while((len = bis.read(buffer)) != -1) {
                    zipOut.write(buffer, 0, len);
                }
                zipOut.flush();
                in.close();
            }
        } catch (Exception e) {
            log.error("zip file occurred error.", e);
        } finally {
            if(bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(zipOut != null) {
                try {
                    zipOut.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(StringUtils.isNotBlank(tempFilePath)) {
                deleteFile(new File(tempFilePath));
            }
        }
    }

    public static void getAllFile(File dir, List<File> fileList) {
        File[] files = dir.listFiles();
        if(files != null) {
            for (File file : files) {
                if(file.isDirectory()) {
                    getAllFile(file, fileList);
                } else {
                    fileList.add(file);
                }
            }
        }
    }

    public static String handleDuplicateFileName(String fileName, List<String> fileNameList) {
        int index = 0;
        for (String name : fileNameList) {
            if(name != null && name.equals(fileName)) {
                index++;
            }
        }
        if(index != 0) {
            String[] name = fileName.split("\\.");
            name[0] = "(" + index + ")";
            fileName = String.join(".", name);
        }
        return fileName;
    }

    public static void deleteFile(File file) {
        if(file.isFile()) {
            file.delete();
        } else {
            for (File sonFile : file.listFiles()) {
                deleteFile(sonFile);
            }
        }
        file.delete();
    }
}
