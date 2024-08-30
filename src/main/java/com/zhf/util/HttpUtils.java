package com.zhf.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
public class HttpUtils {

    public static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    public static final Charset UTF_8 = StandardCharsets.UTF_8;

    private static CloseableHttpClient httpClient;

    @PostConstruct
    public void initHttpClient() {
        httpClient = HttpClientBuilder.create().build();
    }

    public static String sendPost(String url, String jsonParam) throws IOException, URISyntaxException {
        URI uri = HttpUtils.constructUri(url, null);
        HttpPost post = new HttpPost();
        // set uri
        post.setURI(uri);
        // set normal header
        post.setHeader("Content-type", "application/json");
        // set request entity
        StringEntity requestEntity = new StringEntity(jsonParam, UTF_8);
        requestEntity.setContentEncoding(UTF_8.toString());
        // do post
        logger.info("Request entity {} when do poost {}. ", requestEntity, uri);
        CloseableHttpResponse response = httpClient.execute(post);
        logger.info("Receive http response code: {} ", response.getStatusLine());
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity, UTF_8);
    }

    public static URI constructUri(String url, List<NameValuePair> params) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(url);
        builder.setCharset(UTF_8);
        if (params != null) {
            builder.addParameters(params);
        }
        return builder.build();
    }


    /**
     * 请求URL，返回String
     * @param url
     * @param params
     * @return
     */
    public static String request(String url, Map<String, String> params) {
        URL u = null;
        HttpURLConnection con = null;
        // 构建请求参数
        StringBuffer sb = new StringBuffer();
        if (params != null) {
            for (Map.Entry<String, String> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
            sb.substring(0, sb.length() - 1);
        }
        // 尝试发送请求
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            //// POST 只能为大写，严格限制，post会不识别
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(sb.toString());
            osw.flush();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        // 读取返回内容
        StringBuffer buffer = new StringBuffer();
        try {
            // 一定要有返回值，否则无法把请求发送给server端。
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    public static String get(String urlPath) throws IOException{
        URL url = new URL(urlPath);
        //调用URL对象的openConnection( )来获取HttpURLConnection对象实例
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //请求方法为GET
        conn.setRequestMethod("GET");
        //设置连接超时为5秒
        conn.setConnectTimeout(5000);

        StringBuffer buffer = new StringBuffer();

        //服务器返回东西了，先对响应码判断
        if (conn.getResponseCode() == 200) {
            // 读取返回内容
            try {
                // 一定要有返回值，否则无法把请求发送给server端。
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String temp;
                while ((temp = br.readLine()) != null) {
                    buffer.append(temp);
                    buffer.append("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }

}
