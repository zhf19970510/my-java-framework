package com.zhf.dao.mail;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * 邮件列表
 */
public interface MailListMapper {

    /**
     * 查询邮件列表
     *
     * @param receiveType  邮件接收类型
     * @param receiveType2 邮件接收类型2
     * @param receiveType3 邮件接收类型3
     * @return 邮件列表
     */
    ArrayList<String> selectMailListByReceiverType(@Param("receiverType") String receiveType, @Param("receiveType2") String receiveType2, @Param("receiveType3") String receiveType3);

}