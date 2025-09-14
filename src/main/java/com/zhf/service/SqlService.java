package com.zhf.service;

import java.util.List;
import java.util.Map;

public interface SqlService {

    List<Map<String, Object>> getSqlQueryResult(String sql);


    int updateRecordBySql(String sql);

    int deleteRecordBySql(String sql);

    int insertRecordBySql(String sql);
}
