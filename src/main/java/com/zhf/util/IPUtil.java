package com.zhf.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class IPUtil {
    private static final Logger logger = LoggerFactory.getLogger(IPUtil.class);

    public static String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (true) {
                NetworkInterface current;
                do {
                    do {
                        do {
                            if (!interfaces.hasMoreElements()) {
                                return ip;
                            }
                            current = interfaces.nextElement();
                        } while (!current.isUp());
                    } while (current.isLoopback());
                } while (current.isVirtual());
                Enumeration<InetAddress> addresses = current.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && !address.getHostAddress().contains(":") && !address.getHostAddress().contains("localhost")) {
                        if (address.getHostAddress().startsWith("168.")) {
                            return address.getHostAddress();
                        }
                        ip = address.getHostAddress();
                    }
                }
            }
        } catch (Exception var5) {
            logger.error("get local ip error.", var5);
            return "";
        }
    }

}
