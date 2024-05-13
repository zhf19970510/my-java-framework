package com.zhf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainTest111 {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(9);
        list.add(2);
        list.add(3);
        list.add(5);
        list.add(8);
        list.add(4);
        list.add(7);
        list.add(6);
        Collections.sort(list);
        for (Integer integer : list) {
            System.out.println(integer);
        }
        System.out.println(list.get(5));
    }
}
