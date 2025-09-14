package com.zhf.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper

public interface SqlDao {

    List<Map<String, Object>> getSqlQueryResult(@Param("sql") String sql);

    int updateRecordBySql(@Param("sql") String sql);

    int deleteRecordBySql(@Param("sql") String sql);

    int insertRecordBySql(@Param("sql") String sql);
}
