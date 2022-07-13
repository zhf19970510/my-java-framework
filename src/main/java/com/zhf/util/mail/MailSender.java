package com.zhf.util.mail;

import com.zhf.entity.model.mail.AttachedFile;
import com.zhf.entity.model.mail.MailConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 曾鸿发
 * @create: 2022-06-13 17:14
 * @description： 邮件发送工具类
 **/
public class MailSender {
    private static final String DEFAULT_ENCODE = "UTF-8";
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);

    public MailSender() {
    }

    private static void setMailConfig(HtmlEmail email, MailConfig mailConfig, List<String> receiverMails, List<String> ccMails, List<String> bccMails) throws EmailException {
        email.setSSLOnConnect(false);
        email.setStartTLSEnabled(false);
        email.setHostName(mailConfig.getMailHost());
        email.setSmtpPort(mailConfig.getMailPort());
        email.setAuthentication(mailConfig.getMailUsername(), mailConfig.getMailPassword());
        email.setFrom(mailConfig.getMailAddress(), mailConfig.getMailName());

        int i;

        for (i = 0; i < receiverMails.size(); ++i) {
            email.addTo(receiverMails.get(i));
        }

        for (i = 0; i < ccMails.size(); ++i) {
            email.addCc(ccMails.get(i));
        }

        for (i = 0; i < bccMails.size(); ++i) {
            email.addBcc(bccMails.get(i));
        }
    }

    private static void setMailContent(HtmlEmail email, String subject, String context, ArrayList<AttachedFile> attachedFiles) throws EmailException, MalformedURLException {
        email.setCharset("UTF-8");
        email.setSubject(subject);
        email.setHtmlMsg(context);
        for (int i = 0; i < attachedFiles.size(); i++) {
            EmailAttachment attachment = new EmailAttachment();
            if (attachedFiles.get(i).getDataSource() == null) {
                if (StringUtils.isEmpty(attachedFiles.get(i).getUrl())) {
                    attachment.setPath(attachedFiles.get(i).getFilePath());
                } else {
                    attachment.setURL(new URL(attachedFiles.get(i).getFilePath()));
                }
                attachment.setDisposition("attachment");
                attachment.setDescription(attachedFiles.get(i).getDescription());
                attachment.setName(attachedFiles.get(i).getNameInEmail());
                email.attach(attachment);
            } else {
                email.attach(attachedFiles.get(i).getDataSource(), attachedFiles.get(i).getNameInEmail(), attachedFiles.get(i).getDescription());
            }
        }

    }

    public static void sendHtmlEmail(MailConfig mailConfig, List<String> receiverMails, List<String> ccMails, List<String> bccMails, String subject, String context, ArrayList<AttachedFile> attachedFiles, String env, boolean reloadEmailSettings) throws EmailException, MalformedURLException {
        if(null != mailConfig & !StringUtils.isEmpty(mailConfig.getMailAddress()) && null != receiverMails && !receiverMails.isEmpty() && !StringUtils.isEmpty(subject)){
            context = StringUtils.isEmpty(context) ? "" : context;
            HtmlEmail email = new HtmlEmail();
            setMailConfig(email, mailConfig, receiverMails, ccMails, bccMails);
            setMailContent(email, subject, context, attachedFiles);
            email.send();
        }else{
            logger.error("Mail send failed, key value is empty. " + (null == mailConfig ? "mailconfig" : "") + (StringUtils.isEmpty(mailConfig.getMailAddress()) ? "Sender address, " : "") + (null == receiverMails ? "Receiver address, " : "") + (StringUtils.isEmpty(subject) ? "Subject, " : "") + "is not set");
            throw new RuntimeException("key value is empty");
        }
    }

}
