package com.zhf.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 1. 在前端或者外部发送请求后，动态地在代码中继续往请求中添加请求头
 * 2. 动态地将post请求中传入的参数数据转换为JSONObject对象
 * 使用方法参考 com.zhf.config.RequestXssFilter 对 XssHttpServletRequestWrapper 的引用，在filter中就可以调用该wrapper的addHeader方法
 */
@Slf4j
public class HeaderMapRequestWrapper extends XssHttpServletRequestWrapper {

    /**
     * 定义字节数组，缓存请求体信息
     */
    private byte[] requestBody;

    private HttpServletRequest request;

    public HeaderMapRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    private Map<String, String> headerMap = new HashMap<>();

    public void addHeader(String name, String value) {
        headerMap.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = super.getHeader(name);
        if (headerMap.containsKey(name)) {
            headerValue = headerMap.get(name);
        }
        return headerValue;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        ArrayList<String> names = Collections.list(super.getHeaderNames());
        for (String name : headerMap.keySet()) {
            names.add(name);
        }
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        ArrayList<String> values = Collections.list(super.getHeaders(name));
        if (headerMap.containsKey(name)) {
            values.add(headerMap.get(name));
        }
        return Collections.enumeration(values);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (null == this.requestBody) {
            this.requestBody = StreamUtils.copyToByteArray(request.getInputStream());
        }
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    /**
     * 重写获取字符输入流的方法，从赋值的字节数组中获取
     *
     * @return BufferedReader
     * @throws IOException IOException
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /**
     *
     */
    public JSONObject getJsonBody() {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            BufferedReader reader = getReader();
            String line;
            while (null != (line = reader.readLine())) {
                stringBuilder.append(line);
            }
            // 转换为JSON对象
            return JSONObject.parseObject(stringBuilder.toString());
        } catch (Exception e) {
            log.error("parse request body occurred error..", e);
            return null;
        }
    }


}
