package com.zhf.design.pattern.proxy.cglib;

import java.util.Collections;
import java.util.List;

public class UserServiceImpl {

    public List<User> findUserList() {
        return Collections.singletonList(new User("张三", 18));
    }
}
