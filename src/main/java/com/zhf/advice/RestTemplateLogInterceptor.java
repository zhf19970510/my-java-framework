package com.zhf.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class RestTemplateLogInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateLogInterceptor.class);

    @Autowired
    ObjectMapper mapper;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String urlStr = request.getURI().toString();
        HttpHeaders headers = request.getHeaders();
        String headerStr = mapper.writeValueAsString(headers);
        String bodyStr = StringUtils.toEncodedString(body, StandardCharsets.UTF_8);
        logger.info("URL : {} \n header : {} \n requestBody : {}", urlStr, headerStr, bodyStr);
        ClientHttpResponse execute = execution.execute(request, body);
        int statusCode = execute.getStatusCode().value();
        InputStream responseBody = execute.getBody();
        String responseBodyStr = StreamUtils.copyToString(responseBody, StandardCharsets.UTF_8);
        logger.info("statusCode : {} \n responseBodyStr : {}", statusCode, responseBodyStr);
        return execute;
    }
}
