package com.zhf.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;

@Slf4j
public class MDCUtil {

    private MDCUtil() {
        throw new IllegalStateException("JwtUtil utility class");
    }

    public static void clear() {
        MDC.clear();
    }

    public void set(String key, String value) {
        MDC.put(key, value);
    }

    public String get(String key) {
        return MDC.get(key);
    }

    public static MDCInfo getMDCInfo() {
        return new MDCInfo(MDC.getCopyOfContextMap());
    }

    public static void setMDCInfo(MDCInfo mdcInfo) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        if (contextMap == null) {
            contextMap = mdcInfo.getContextMap();
        } else {
            contextMap.putAll(mdcInfo.getContextMap());
        }
        MDC.setContextMap(contextMap);
    }

    public static final class MDCInfo {
        private Map<String, String> contextMap;

        public MDCInfo(Map<String, String> contextMap) {
            this.contextMap = contextMap;
        }

        public Map<String, String> getContextMap() {
            return contextMap;
        }

        public void setContextMap(Map<String, String> contextMap) {
            this.contextMap = contextMap;
        }
    }
}
