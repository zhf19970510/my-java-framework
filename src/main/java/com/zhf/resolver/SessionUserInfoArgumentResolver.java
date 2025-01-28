package com.zhf.resolver;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zhf.config.HeaderMapRequestWrapper;
import com.zhf.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 自定义参数解析器，解析参数
 */
@Slf4j
@Component
public class SessionUserInfoArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(SessionUserInfoDTO.class) && parameter.hasParameterAnnotation(SessionInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final String op = "SessionUserInfoArgumentResolver.resolveArgument";
        Object user = webRequest.getAttribute("user", NativeWebRequest.SCOPE_SESSION);
        SessionUserInfoDTO sessionUserInfoDTO = new SessionUserInfoDTO();
        if (!Objects.isNull(user)) {
            String token = webRequest.getHeader("Authorization");
            if (StringUtils.isBlank(token)) {
                throw new RuntimeException("token为空");
            }
            token = StrUtil.removePrefixIgnoreCase(token, "Bearer").trim();
            if (StringUtils.isBlank(token)) {
                throw new RuntimeException("token为空");
            }
            String userId = JwtUtils.getUserId(token);
            String username = JwtUtils.getUsername(token);
            String deptId = JwtUtils.getDeptId(token);
            sessionUserInfoDTO.setUserId(userId);
            sessionUserInfoDTO.setUsername(username);
            sessionUserInfoDTO.setDeptId(deptId);
            // 在这里还可以进一步结合使用HeaderMapRequestWrapper获取请求体中的数据
            return sessionUserInfoDTO;
        }
        return user;
    }

    public JSONObject getJsonBody(NativeWebRequest nativeWebRequest) {
        final String op = "SessionUserInfoArgumentResolver.getJsonBody";
        try {
            HttpServletRequest httpServletRequest = ((ServletWebRequest) nativeWebRequest).getRequest();
            if (!httpServletRequest.getClass().isAssignableFrom(HeaderMapRequestWrapper.class)) {
                return null;
            }
            return ((HeaderMapRequestWrapper) httpServletRequest).getJsonBody();
        } catch (Exception e) {
            log.error("op = {} parse request body occurred error.", op, e);
            return null;
        }
    }
}
