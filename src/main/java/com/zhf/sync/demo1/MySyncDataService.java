package com.zhf.sync.demo1;

import com.zhf.util.MyBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MySyncDataService {

    public static final Logger logger = LoggerFactory.getLogger(MySyncDataService.class);

    public void syncData(RequestParamModel dataModel, PreHandler preHandler, PostHandler postHandler) {
        if(null != preHandler){
            preHandler.handle();
        }
        // 根据dataModel 处理具体的业务逻辑
        // .....................

        // 我这边只是简单处理下，模拟效果
        List<Map<String, Object>> ret = new ArrayList<>();
        Map<String, Object> stringObjectMap = MyBeanUtils.objectToMap(dataModel);
        ret.add(stringObjectMap);
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        ret.add(map);



        // 构建同步数据之后的请求结果
        if(null != postHandler){
            // 异步调用 postHandler方法
            new Thread(() -> {
                postHandler.handle(ret);
            }).start();
        }
    }

    public List<Map<String, Object>> mockData(){
        List<Map<String, Object>> ret = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            Map<String, Object> map = new HashMap<>();
            map.put(i + "" + i , i + "" + i);
            ret.add(map);
        }
        return ret;
    }

    public static class RequestParamModel {
        private String resource;
        private String jsonBody;
        private String param;
        private Integer startRow;
        private Integer rowNum;

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }

        public String getJsonBody() {
            return jsonBody;
        }

        public void setJsonBody(String jsonBody) {
            this.jsonBody = jsonBody;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public Integer getStartRow() {
            return startRow;
        }

        public void setStartRow(Integer startRow) {
            this.startRow = startRow;
        }

        public Integer getRowNum() {
            return rowNum;
        }

        public void setRowNum(Integer rowNum) {
            this.rowNum = rowNum;
        }
    }

    /**
     * handle before sync
     */
    @FunctionalInterface
    interface PreHandler {
        void handle();
    }

    /**
     * Handle the whole result set
     */
    @FunctionalInterface
    interface PostHandler {
        void handle(List<Map<String, Object>> resultData);
    }

}
