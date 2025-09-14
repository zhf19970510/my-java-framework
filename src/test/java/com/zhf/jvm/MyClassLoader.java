package com.zhf.jvm;

import java.io.*;

public class MyClassLoader extends ClassLoader{

    private String root;

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = loadClassData(name);
        if(classData == null) {
            throw new ClassNotFoundException();
        } else {
            // 此方法负责将二进制的字节码转换为Class对象
            return defineClass(name, classData, 0, classData.length);
        }
    }

    private byte[] loadClassData(String className) {
        String fileName = root + File.separatorChar + className.replace('.', File.separatorChar) + ".class";
        try {
            InputStream ins = new FileInputStream(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int bufferSize = 1024;

            byte[] buffer = new byte[bufferSize];

            int length = 0;
            while ((length = ins.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            byte[] byteArray = baos.toByteArray();
            baos.close();
            ins.close();
            return byteArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public static void main(String[] args) {
        MyClassLoader myClassLoader = new MyClassLoader();
//        myClassLoader.setRoot("D:\\");
        myClassLoader.setRoot("E:\\workspace\\LeetCode\\target\\classes");

        Class<?> testClass = null;
        try {
//            testClass = myClassLoader.loadClass("Employee");
            testClass = myClassLoader.loadClass("leetcode02.ListNode");
            System.out.println(testClass);
            Object object = testClass.newInstance();
            System.out.println(object.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
