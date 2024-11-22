package com.zhf.redis.redismq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ListVerTest {

    @Autowired
    private ListVer listVer;

    @Test
    void testGet(){
        List<String> result = listVer.get("listmq");
        for(String message : result){
            System.out.println(message);
        }
    }

    @Test
    void testPut(){
        listVer.put("listmq","msgtest");
    }

}