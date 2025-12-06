package com.zhf.design.pattern.bridge;

import java.math.BigDecimal;

/**
 * 微信支付
 *
 * @author:zhf
 * @create:2020/7/9
 */
public class WxPay extends Pay{


    public WxPay(IPayMode payMode) {
        super(payMode);
    }

    @Override
    public String transfer(String uId, String tradeId, BigDecimal amount) {
        System.out.println("微信渠道支付划账开始....");
        boolean security = payMode.security(uId);
        System.out.println("微信渠道支付划账结束");
        if(!security) {
            System.out.println("微信渠道支付风控校验失败");
            return "500";
        } else {
            System.out.println("微信渠道支付划账成功");
            return "200";
        }
    }
}
