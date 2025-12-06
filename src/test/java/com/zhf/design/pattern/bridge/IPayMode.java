package com.zhf.design.pattern.bridge;

/**
 * 支付模式接口
 */
public interface IPayMode {

    /**
     * 安全校验功能：对各种支付模式惊醒风控校验
     * @param uId
     * @return
     */
    boolean security(String uId);
}
