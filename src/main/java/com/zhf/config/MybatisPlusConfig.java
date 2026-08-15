package com.zhf.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.zhf.interceptor.BatchRewriteInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 曾鸿发
 * @create: 2021-11-03 08:57
 * @description：MyPlus配置类
 **/
@Configuration
public class MybatisPlusConfig {

    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        //设置方言类型
        page.setDialectType("mysql");
        return page;
    }

    /**
     * 乐观锁
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
        return new OptimisticLockerInterceptor();
    }

    /**
     * 批量插入SQL改写拦截器
     * <p>
     * 实现类似 MySQL JDBC 驱动 rewriteBatchedStatements 参数的功能，
     * 在 MyBatis 层面拦截 INSERT 语句进行 SQL 改写。
     * 配合 {@link com.zhf.util.BatchInsertUtil} 使用。
     */
    @Bean
    public Interceptor batchRewriteInterceptor() {
        return new BatchRewriteInterceptor();
    }

}
