package com.zhf.interceptor;

import com.zhf.util.batchinsert.BatchInsertCaptureContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

import java.util.List;
import java.util.Properties;

/**
 * MyBatis批量插入SQL改写拦截器
 * <p>
 * 该拦截器实现类似 MySQL JDBC 驱动 rewriteBatchedStatements 参数的功能，
 * 在 MyBatis 层面拦截 INSERT 语句，将多条单值 INSERT 改写为一条多值 INSERT。
 * <p>
 * 工作流程：
 * <ol>
 *   <li>当 {@link BatchInsertCaptureContext#isCapturing()} 返回 true 时，拦截器进入捕获模式</li>
 *   <li>拦截器从 BoundSql 中提取原始 SQL 和参数值，缓存到 ThreadLocal</li>
 *   <li>拦截器返回 0，不实际执行 SQL(避免产生多次数据库交互)</li>
 *   <li>当 {@link BatchInsertCaptureContext#isCapturing()} 返回 false 时，拦截器正常放行</li>
 * </ol>
 * 参数提取逻辑参考 MyBatis {@code DefaultParameterHandler}，兼容以下场景：
 * <ul>
 *   <li>普通实体参数(无 @Param 注解)</li>
 *   <li>单值参数(有 TypeHandler 注册的基本类型)</li>
 *   <li>动态参数(foreach 等产生的 additionalParameter)</li>
 * </ul>
 *
 * @author: 曾鸿发
 * @create: 2026-08-15
 */
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update",
                args = {MappedStatement.class, Object.class})
})
public class BatchRewriteInterceptor implements Interceptor {

    /**
     * 拦截 Executor.update 方法
     * <p>
     * 当处于捕获模式时，提取 INSERT 语句的 SQL 模板和参数值，
     * 缓存到 {@link BatchInsertCaptureContext}，返回 0 不执行实际 SQL。
     * 不处于捕获模式时，直接放行。
     *
     * @param invocation 拦截器调用上下文
     * @return 捕获模式返回 0，非捕获模式返回原方法执行结果
     * @throws Throwable 执行过程中的异常
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 非捕获模式，正常放行
        if (!BatchInsertCaptureContext.isCapturing()) {
            return invocation.proceed();
        }

        // 获取 MappedStatement 和参数对象
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];

        // 获取 BoundSql(包含 SQL 模板和参数映射)
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        String sql = boundSql.getSql().trim();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        // 提取参数值
        Configuration configuration = ms.getConfiguration();
        Object[] paramValues = new Object[parameterMappings.size()];
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping pm = parameterMappings.get(i);
            String property = pm.getProperty();
            paramValues[i] = resolveParameterValue(boundSql, parameterObject, configuration, property);
        }

        // 缓存捕获的 SQL 和参数
        BatchInsertCaptureContext.addCapturedSql(sql, paramValues);
        log.debug("捕获到第 {} 条 INSERT SQL，参数数量: {}",
                BatchInsertCaptureContext.getCapturedSqls().size(), paramValues.length);

        // 返回 0，不实际执行 SQL
        return 0;
    }

    /**
     * 解析参数值
     * <p>
     * 参照 MyBatis DefaultParameterHandler 的参数解析逻辑：
     * <ol>
     *   <li>优先从 BoundSql 的 additionalParameter 中获取(foreach 等动态参数)</li>
     *   <li>parameterObject 为 null 时返回 null</li>
     *   <li>parameterObject 有注册 TypeHandler 时(基本类型)，返回 parameterObject 本身</li>
     *   <li>其他情况使用 MetaObject 按 property 路径解析实体属性值</li>
     * </ol>
     *
     * @param boundSql        BoundSql 对象
     * @param parameterObject 原始参数对象
     * @param configuration   MyBatis Configuration
     * @param property        参数属性名(如 "pkId" 或 "item.name")
     * @return 解析后的参数值
     */
    private Object resolveParameterValue(BoundSql boundSql, Object parameterObject,
                                         Configuration configuration, String property) {
        // 1. 动态参数(foreach 等产生的)
        if (boundSql.hasAdditionalParameter(property)) {
            return boundSql.getAdditionalParameter(property);
        }
        // 2. 参数为 null
        if (parameterObject == null) {
            return null;
        }
        // 3. 基本类型参数(有注册 TypeHandler)
        if (configuration.getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass())) {
            return parameterObject;
        }
        // 4. 实体属性，通过 MetaObject 解析
        MetaObject metaObject = configuration.newMetaObject(parameterObject);
        return metaObject.getValue(property);
    }

    /**
     * 用 Plugin 包装目标对象，使拦截器生效
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 设置属性(暂无需要)
     */
    @Override
    public void setProperties(Properties properties) {
        // no-op
    }
}
