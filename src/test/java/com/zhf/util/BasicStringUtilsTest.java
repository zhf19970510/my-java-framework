package com.zhf.util;

public class BasicStringUtilsTest {

    public static void main(String[] args) {
        String aa_bb_cc_dd = BasicStringUtils.lineToHump("aa_bb_cc_dd");
        System.out.println(aa_bb_cc_dd);
        System.out.println(BasicStringUtils.humpToLine(aa_bb_cc_dd));
    }
}
