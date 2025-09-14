package com.zhf.service.impl;

import com.zhf.dao.SqlDao;
import com.zhf.service.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SqlServiceImpl implements SqlService {

    @Autowired
    SqlDao sqlDao;

    @Override
    public List<Map<String, Object>> getSqlQueryResult(String sql) {
        return sqlDao.getSqlQueryResult(sql);
    }

    @Override
    public int updateRecordBySql(String sql) {
        return sqlDao.updateRecordBySql(sql);
    }

    @Override
    public int deleteRecordBySql(String sql) {
        return sqlDao.deleteRecordBySql(sql);
    }

    @Override
    public int insertRecordBySql(String sql) {
        return sqlDao.insertRecordBySql(sql);
    }
}
