package com.zhf.jvm;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ClazzTest {

    public static void main(String[] args) {
        // 对于包装类型为原生类型，或者 String类型，按值传递

        Worker worker = new Worker();
        Worker worker1 = new Worker();
        System.out.println(worker == worker1);
        System.out.println(worker.getClass() == worker1.getClass());

        System.out.println(Worker.class == worker.getClass());

        Map<Class<Worker>, Integer> workerCount = new HashMap<>();
        workerCount.put((Class<Worker>) worker1.getClass(), 1);
        System.out.println(workerCount.containsKey(worker.getClass()));

        // 有序表 TreeMap，java中TreeMap用红黑树实现

        worker.setId("1");
        worker.setName("aa");

        worker1.setId("1");
        worker1.setName("aa");
        System.out.println(worker == worker1);

        Map<Worker, Integer> workerIntegerMap = new HashMap<>();
        workerIntegerMap.put(worker, 1);
        System.out.println(workerIntegerMap.containsKey(worker1));

        TreeMap<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(1, "我是1");
        treeMap.put(2, "我是1");
        treeMap.put(3, "我是1");
        treeMap.put(4, "我是1");
        treeMap.put(5, "我是1");
//        treeMap.put(6, "我是1");
        treeMap.put(7, "我是1");
        treeMap.put(8, "我是1");
        treeMap.put(9, "我是1");

        System.out.println("treeMap新鲜用法：");
        System.out.println(treeMap.firstKey());
        System.out.println(treeMap.lastKey());
        System.out.println(treeMap.floorKey(6));
        System.out.println(treeMap.ceilingKey(6));
    }
}
