package com.zhf.jvm;

public class StringTest {

    public static void main(String[] args) {
        {
            String a = new String("aa") + new String("bb");
            String b = a.intern();
            System.out.println(a == b);     // true
        }

        {
            String c = new String("cc");
            String d = c.intern();
            System.out.println(c == d);     // false
        }

        {
            String e = "ee" + "ff";
            String f = e.intern();
            System.out.println(e == f);     // true

            String g = "eeff";
            System.out.println(e == g);    // true
        }


    }
}
