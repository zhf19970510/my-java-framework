package com.zhf.util;

import com.zhf.entity.Account;
import com.zhf.entity.do1.TradeModel;
import com.zhf.entity.do1.TradeModelClone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 曾鸿发
 * @create: 2022-04-18 17:42
 * @description：
 **/
@RunWith(SpringRunner.class)
public class BeanUtilCopyTest {

    @Test
    public void testBeanUtilsCopy(){
        TradeModel tradeModel = new TradeModel();
        tradeModel.setOrderModel("tradeModel");
        tradeModel.setTotalFee("totalFee");
        tradeModel.setAmount("amount");
        tradeModel.setOrderId("orderId");
        tradeModel.setQuantity("quantity");
        tradeModel.setTradeDate("tradeDate");
        Account account = new Account();
        account.setBalance(3.2);
        account.setUsername("username");
        account.setId(32);
        account.setCard("card");
        tradeModel.setAccount(account);
        tradeModel.setMoney("100");

        TradeModel tradeModel1 = new TradeModel();
        tradeModel1.setOrderModel("tradeModel1");
        tradeModel1.setTotalFee("totalFee1");
        tradeModel1.setAmount("amount1");
        tradeModel1.setOrderId("orderId1");
        tradeModel1.setQuantity("quantity1");
        tradeModel1.setTradeDate("tradeDate1");
        tradeModel1.setMoney("200");
        Account account1 = new Account();
        account1.setBalance(3.3);
        account1.setUsername("username1");
        account1.setId(33);
        account1.setCard("card1");
        tradeModel1.setAccount(account1);

        List<TradeModel> tradeModels = new ArrayList<>();
        tradeModels.add(tradeModel);
        tradeModels.add(tradeModel1);

        List<TradeModelClone> tradeModels1 = BeanUtilCopy.copyListProperties(tradeModels, TradeModelClone::new,
            (tradeModelSource, tradeModelTarget)->{
                Account account2 = new Account();
                BeanUtils.copyProperties(tradeModelSource.getAccount(), account2);
                tradeModelTarget.setAccount(account2);
                tradeModelTarget.setMoney(Integer.parseInt(tradeModelSource.getMoney()));
            }
        );

        for(TradeModelClone tradeModel2 : tradeModels1){
            System.out.println(tradeModel2);
        }

        for (int i = 0; i < tradeModels1.size(); i++) {
            if(i == 1){
                tradeModels1.get(i).getAccount().setUsername("myusername");
                tradeModels1.get(i).getAccount().setCard("mycard");
            }
        }

        System.out.println("===============原始数据：================");

        for(TradeModel tradeModel2 : tradeModels){
            System.out.println(tradeModel2);
        }

        System.out.println("===============转换后的数据：================");

        for(TradeModelClone tradeModel2 : tradeModels1){
            System.out.println(tradeModel2);
        }
    }

}
