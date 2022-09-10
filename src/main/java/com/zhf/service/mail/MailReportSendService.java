package com.zhf.service.mail;

import com.zhf.entity.model.mail.AttachedFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: 曾鸿发
 * @create: 2022-06-13 13:28
 * @description： 发送异常邮件，也可以参考：hutool 里面的 MailUtil ： cn.hutool.extra.mail.MailUtil
 **/
public interface MailReportSendService {

    /**
     * 异常邮件发送（使用的是异常邮件模板，type=0）
     *
     * @param subject         邮件主题
     * @param description     邮件内容标语描述
     * @param header          异常数据表头 （key为异常数据的属性值，value为表头名）
     * @param dataList        异常数据值
     * @param receiveTypeList receiveTypeList
     * @param modelType       邮件类型
     */
    void sendExceptionMail(String subject, String description, Map<String, String> header, List<Map<String, Object>> dataList, List<String> receiveTypeList, Integer modelType);

    /**
     * 异常邮件发送（使用的是异常邮件模板，type=0）
     *
     * @param subject     邮件主题
     * @param description 邮件内容标语描述
     * @param header      异常数据表头 （key为异常数据的属性值，value为表头名）
     * @param dataList    异常数据值
     * @param receiveType receiveType
     * @param modelType   邮件类型
     */
    void sendExceptionMail(String subject, String description, Map<String, String> header, List<Map<String, Object>> dataList, String receiveType, Integer modelType);

    /**
     * 异常邮件发送（使用的是异常邮件模板，type=0）
     *
     * @param subject               邮件主题
     * @param description           邮件内容标语描述
     * @param header                异常数据表头（key为异常数据的属性值，value为表头名）
     * @param dataList              异常数据值
     * @param receiverTypeList      receiverTypeList
     * @param modelType             邮件类型
     * @param attachedFileArrayList 附件
     */
    void sendExceptionMail(String subject, String description, Map<String, String> header, List<Map<String, Object>> dataList, List<String> receiverTypeList, Integer modelType, ArrayList<AttachedFile> attachedFileArrayList);

    /**
     * 异常邮件发送（使用的是异常邮件模板，type=0）
     *
     * @param subject               邮件主题
     * @param description           邮件内容标语描述
     * @param header                异常数据表头（key为异常数据的属性值，value为表名）
     * @param dataList              异常数据值
     * @param receiveType           receiveType
     * @param modelType             邮件类型
     * @param attachedFileArrayList 附件
     */
    void sendExceptionEmail(String subject, String description, Map<String, String> header, List<Map<String, Object>> dataList, String receiveType, Integer modelType, ArrayList<AttachedFile> attachedFileArrayList);

    /**
     * 通过FreeMarker模板发送邮件数据
     *
     * @param subject         邮件主题
     * @param data            异常数据值
     * @param receiveTypeList receiveTypeList
     * @param modelType       邮件类型
     */
    void sendEmailByFreeMarkerTemplate(String subject, Map<String, Object> data, List<String> receiveTypeList, Integer modelType);
}
