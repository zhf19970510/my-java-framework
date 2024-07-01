package com.zhf.java_basic.static1;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * 因为Test类的hello方法是静态的，所以是属于类的，当实例化该类的时候，静态会被优先加载而且只加载一次，所以不受实例化new Test();影响，
 * 只要是使用到了Test类，都会加载静态hello方法！ 另外，在其他类的静态方法中也是可以调用公开的静态方法，
 * 此题hello方法是使用public修饰的所以在MyApplication中调用hello也是可以的。
 * 总结：即使Test test=null;这里也会加载静态方法，所以test数据中包含Test类的初始化数据。（静态的，构造的，成员属性）
 * 因此test.hello是会调用到hello方法的。
 */
class StaticTestPojo {
    public static void hello() {
        System.out.println("hello");
    }
}

public class StaticTest {

    public static void main(String[] args) {
        StaticTestPojo staticTestPojo = null;
        staticTestPojo.hello();
    }
}

