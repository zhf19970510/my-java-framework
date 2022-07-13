package com.zhf.dao.mail;

import com.zhf.entity.model.mail.MailConfig;

/**
 * 邮件配置数据访问层
 */
public interface MailConfigMapper {

    /**
     * 查询邮件配置
     * @return
     */
    MailConfig selectMailConfig();

}