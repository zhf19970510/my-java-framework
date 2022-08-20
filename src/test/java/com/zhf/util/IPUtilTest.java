package com.zhf.util;

import org.junit.Test;

public class IPUtilTest {

    @Test
    public void testGetIpAddress(){
        String ipAddress = IPUtil.getIpAddress();
        System.out.println(ipAddress);
    }
}
