package com.zhf.design.pattern.bridge;

import java.math.BigDecimal;

public class PayTest {

    public static void main(String[] args) {
        IPayMode payMode = new PayFaceMode();
        Pay wxPay = new WxPay(payMode);
        wxPay.transfer("1001", "1002", new BigDecimal(100));
    }
}
