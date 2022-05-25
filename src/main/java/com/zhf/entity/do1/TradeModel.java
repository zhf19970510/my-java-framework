package com.zhf.entity.do1;

import java.io.Serializable;

/**
 * @author: 曾鸿发
 * @create: 2022-04-18 17:35
 * @description：
 **/
public class TradeModel extends BaseTradeModel implements Serializable {

    private String orderModel;

    private String totalFee;

    private String money;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOrderModel() {
        return orderModel;
    }

    public void setOrderModel(String orderModel) {
        this.orderModel = orderModel;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }


    @Override
    public String toString() {
        return "TradeModel{" +
                "orderModel='" + orderModel + '\'' +
                ", totalFee='" + totalFee + '\'' +
                ", money='" + money +"\'" +
                super.toString() +
                '}';
    }
}
