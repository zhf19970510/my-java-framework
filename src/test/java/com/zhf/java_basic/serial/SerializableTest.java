package com.zhf.java_basic.serial;

public class SerializableTest {

    public static void main(String[] args) {
        DataObject object = new DataObject();
        object.setWord("123");
        object.setI(2);
        // 将此对象序列化为文件，并在另外一个JVM中读取文件，进行反序列化，请问此时读出的Data0bject对象中的word和i的值分别为：

        // 结果应该为 "123", 0
        // 理由：序列化保存的是对象的状态，静态变量属于类的状态，因此，序列化并不保存静态变量。所以i是没有改变的
    }
}
