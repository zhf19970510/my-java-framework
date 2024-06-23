package com.zhf.postConstruct;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private RoleService roleService;

    static {
        System.out.println("UserService static");
    }

    public UserService(){
        System.out.println("userService Constructor");
    }

    @PostConstruct
    public void init(){
        System.out.println("UserService PostConstruct");
    }
    
}
