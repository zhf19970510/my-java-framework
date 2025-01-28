package com.zhf.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@Component
public class RequestXssFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(RequestXssFilter.class);

    FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();
        logger.info("请求uri: {}", requestURI);
        ServletRequest requestFinal = new XssHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        filterChain.doFilter(requestFinal, servletResponse);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
