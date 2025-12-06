package com.zhf.design.pattern.bridge;

/**
 * 刷脸支付
 */
public class PayFaceMode implements IPayMode{
    @Override
    public boolean security(String uId) {
        System.out.println("人脸支付，风控校验-》人脸信息");
        return true;
    }
}
