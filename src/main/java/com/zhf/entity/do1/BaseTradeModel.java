package com.zhf.entity.do1;

import com.zhf.entity.Account;

import java.io.Serializable;

/**
 * @author: 曾鸿发
 * @create: 2022-04-18 17:35
 * @description：
 **/
public class BaseTradeModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderId;

    private String tradeDate;

    private String quantity;

    private String amount;

    private Account account;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "BaseTradeModel{" +
                "orderId='" + orderId + '\'' +
                ", tradeDate='" + tradeDate + '\'' +
                ", quantity='" + quantity + '\'' +
                ", amount='" + amount + '\'' +
                ", account=" + account +
                '}';
    }
}
