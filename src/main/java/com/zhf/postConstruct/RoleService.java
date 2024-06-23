package com.zhf.postConstruct;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RoleService {

    static {
        System.out.println("RoleService static");
    }

    public RoleService(){
        System.out.println("RoleService Constructor");
    }

    @PostConstruct
    public void init(){
        System.out.println("RoleService PostConstruct");
    }

}
