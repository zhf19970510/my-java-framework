package com.zhf.dao.mail;

import com.zhf.entity.model.mail.MailModel;

/**
 * @author: 曾鸿发
 * @create: 2022-06-13 11:27
 * @description： 邮件回复发送数据访问层
 **/
public interface MailModelMapper {

    /**
     * 根据modelType查询对应邮件模型
     * @param modelType 模板类型
     * @return 邮件模型对象
     */
    MailModel queryMailModelByType(Integer modelType);
}
