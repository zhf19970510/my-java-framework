package com.zhf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhf.entity.DecimalTest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DecimalTestDao {
    int deleteByPrimaryKey(Integer id);

    int insert(DecimalTest record);

    int insertSelective(DecimalTest record);

    DecimalTest selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DecimalTest record);

    int updateByPrimaryKey(DecimalTest record);
}