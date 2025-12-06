package com.zhf.design.pattern.proxy;

public class ProxyFactoryTest {

    public static void main(String[] args) {
        IUserDao userDao = new UserDaoImpl();
        System.out.println(userDao.getClass()); // 目标对象的信息

        IUserDao proxy = (IUserDao) new ProxyFactory(userDao).getProxyInstance();

        proxy.save();

    }
}
