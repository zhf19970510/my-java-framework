package com.zhf.design.pattern.proxy.cglib;

import java.util.List;

public class UserLogProxyTest {

    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        UserLogProxy userLogProxy = new UserLogProxy();
        UserServiceImpl proxy = (UserServiceImpl) userLogProxy.getLogProxy(userService);
        System.out.println(proxy.getClass());
        List<User> userList = proxy.findUserList();
        System.out.println(userList);
    }
}
