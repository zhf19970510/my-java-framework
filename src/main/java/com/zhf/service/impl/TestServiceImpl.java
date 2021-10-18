package com.zhf.service.impl;

import com.zhf.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: 曾鸿发
 * @create: 2021-10-10 21:10
 * @description：测试接口实现类
 **/
@Service
@Slf4j
public class TestServiceImpl implements TestService {

    /**
     * @Transactional 注解讲解
     *  propagation:传播特性：表示不同的事务之间执行的关系
     *      REQUIRED:如果有事务在运行，当前的方法就在这个事务内运行，否则，就启动一个新的事务，并在自己的事务内运行
     *      REQUIRED_NEW:当前的方法必须启动新事务，并在它的事务内运行。如果有事务正在运行，应该将它挂起
     *      SUPPORTS:如果有事务在运行，当前的方法就在这个事务内运行否则它可以不运行在事务中
     *      NOT_SUPPORTED:当前的方法不应该运行在事务中，如果有运行的事务，将它挂起
     *      MANDATORY:当前的方法必须运行在事务内部，如果没有正在运行的事务，就抛出异常
     *      NEVER:当前的方法不应该运行在事务中，如果有运行的事务，就抛出异常
     *      NESTED:如果有事务在运行，当前的方法就应该在这个事务的嵌套事务内运行，否则，就启动一个新的事务，并在它自己的事务内运行
     *  isolation:隔离级别，4种隔离级别，会引发不同的数据错乱问题
     *      读未提交、读已提交、可重复读、序列化
     *  timeout:超时时间，表示执行事务需要花费的时间是多久，单位是秒，一般用的比较少
     *  readonly:只读事务，如果配置了只读事务，那么在事务运行期间，不允许对事务进行修改，否则抛出异常
     *  noRollbackFor:
     *  noRollbackForClassName:
     *  rollbackFor:
     *  rollbackForClassName
     */
    @Override
    @Transactional
    public void test1() {

    }

    @Override
    @Transactional
    public void test2(){

    }
}
