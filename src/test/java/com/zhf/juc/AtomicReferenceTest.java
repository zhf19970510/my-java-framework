package com.zhf.juc;

import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicReferenceTest {

    public static void main(String[] args) {
        AtomicStampedReference<String> ref = new AtomicStampedReference<>("AAA", 0);
        String oldValue = ref.getReference();// 获取当前值
        int oldVersion = ref.getStamp();// 获取当前版本号
        ref.compareAndSet(oldValue, "BBB", oldVersion, oldVersion + 1);
        ref.compareAndSet(oldValue, "BBB", 1, 2 + 1);

    }
}
