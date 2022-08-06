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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
}
