package com.zhf.controller;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SseController {

    private Map<String, SseEmitter> map = new ConcurrentHashMap<>();



}
