package com.zhf.util;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Map;

public class JwtUtils {

    public static String secret = "abcdefghijklmnopqrstuvwxyz";

    private JwtUtils() {
    }

    public static String createToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public static String createToken(Map<String, Object> claims, int minutes) {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MINUTE, minutes);
        String token = Jwts.builder().setClaims(claims).setExpiration(date.getTime()).signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    public static Claims parseToKen(String token) {
        try {
            return (Claims) Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUserId(String token) {
        Claims claims = parseToKen(token);
        return getValue(claims, "userId");
    }

    public static String getUsername(String token) {
        Claims claims = parseToKen(token);
        return getValue(claims, "username");
    }

    public static String getDeptId(String token) {
        Claims claims = parseToKen(token);
        return getValue(claims, "deptId");
    }

    public String getToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                // zhf_framework_token,这个token的name一般是在系统登录之后会new Cookie()，然后response响应给前端的，参考代码如下：
//                Map<String, Object> claims = new HashMap<>();
//                claims.put("userId", "111");
//                claims.put("username", "abc");
//                claims.put("deptId", "1223");
//                Cookie cookie1 = new Cookie("zhf_framework_token", JwtUtils.createToken(claims));
//                cookie1.setMaxAge(10000);
//                cookie1.setHttpOnly(true);
//                cookie1.setPath("/");
//                HttpServletResponse response;
//                response.addCookie(cookie);
                if (StringUtils.equals(cookie.getName(), "zhf_framework_token")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        token = StrUtil.removePrefixIgnoreCase(token, "Bearer").trim();
        return token;
    }

    public static String getValue(Claims claims, String key) {
        return claims == null ? null : MyStringUtil.toStr(claims.get(key), "");
    }


}
