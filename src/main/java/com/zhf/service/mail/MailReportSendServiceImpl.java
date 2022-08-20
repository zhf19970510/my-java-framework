package com.zhf.service.mail;

import com.zhf.dao.mail.MailConfigMapper;
import com.zhf.dao.mail.MailListMapper;
import com.zhf.dao.mail.MailModelMapper;
import com.zhf.entity.model.mail.AttachedFile;
import com.zhf.entity.model.mail.MailConfig;
import com.zhf.entity.model.mail.MailModel;
import com.zhf.exception.ServiceException;
import com.zhf.util.mail.MailSender;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.StringWriter;
import java.util.*;

/**
 * @author: 曾鸿发
 * @create: 2022-06-13 13:34
 * @description：
 **/
public class MailReportSendServiceImpl implements MailReportSendService {
    public static final Logger logger = LoggerFactory.getLogger(MailReportSendServiceImpl.class);

    @Autowired
    private MailConfigMapper mailConfigMapper;

    @Autowired
    private MailListMapper mailListMapper;

    @Autowired
    private MailModelMapper mailModelMapper;

    private static final String HEADER_STYLE_LENGTH = "<th style=\"width:#{Length}px;\">#{Header}</th>";
    private static final String DATA_UNIT = "<td>#{Data}</td>";
    private static final String SINGLE_LINE = "<tr>#{Line}</tr>";
    private static final String DOUBLE_LINE = "<tr class=\"even\">#{Line}</tr>";

    public static final Configuration FREEMARKER_CFG;

    static {
        FREEMARKER_CFG = new Configuration(Configuration.VERSION_2_3_31);
        FREEMARKER_CFG.setDefaultEncoding("UTF-8");
        FREEMARKER_CFG.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    @Override
    public void sendExceptionMail(String subject, String description, Map<String, String> header, List<Map<String, Object>> dataList, List<String> receiveTypeList, Integer modelType) {
        this.sendExceptionMail(subject, description, header, dataList, receiveTypeList, modelType, null);
    }

    @Override
    public void sendExceptionMail(String subject, String description, Map<String, String> header, List<Map<String, Object>> dataList, String receiveType, Integer modelType) {
        sendExceptionEmail(subject, description, header, dataList, receiveType, modelType, null);
    }

    @Override
    public void sendExceptionMail(String subject, String description, Map<String, String> header, List<Map<String, Object>> dataList, List<String> receiverTypeList, Integer modelType, ArrayList<AttachedFile> attachedFileArrayList) {
        MailConfig config = mailConfigMapper.selectMailConfig();
        ArrayList<String> receiverMails = new ArrayList<>();
        Set<String> mailsSet = new HashSet<>();

        for (int i = 0; i < receiverTypeList.size(); i++) {
            ArrayList<String> tempList = mailListMapper.selectMailListByReceiverType(receiverTypeList.get(i), null, null);
            if (CollectionUtils.isNotEmpty(tempList)) {
                mailsSet.addAll(tempList);
            }
        }
        receiverMails.addAll(mailsSet);

        if (CollectionUtils.isEmpty(mailsSet)) {
            logger.warn("Not receiver is set, the mial(" + subject + ") will not be sent!");
            return;
        }
        logger.debug("Top receiver : " + receiverMails.get(0));

        try {
            MailModel queryAllMailModel = mailModelMapper.queryMailModelByType(modelType);
            if (queryAllMailModel == null) {
                logger.error("Exception Email Template is not found");
                return;
            }
            String htmlModel = "";
            htmlModel = queryAllMailModel.getEn();
            // 添加邮件开始说明
            if (StringUtils.isNotEmpty(description)) {
                htmlModel.replace("#{Content}", description);
            } else {
                htmlModel = htmlModel.replace("#{Content}", "");
            }

            // 没有异常数据时，直接填空
            if (dataList == null || dataList.isEmpty()) {
                htmlModel = htmlModel.replace("#{Header}", "");
                htmlModel = htmlModel.replace("#{Table}", "");
            } else {
                // 若header信息为空时，自动生成header
                if (header == null || header.isEmpty()) {
                    header = new LinkedHashMap<>();
                    for (int i = 0; i < dataList.size(); i++) {
                        for (String key : dataList.get(0).keySet()) {
                            if (!header.containsKey(key)) {
                                header.put(key, key);
                            }
                        }
                    }
                }

                StringBuffer tableContent = new StringBuffer();
                StringBuffer[] tableContentLine = new StringBuffer[dataList.size()];
                for (int i = 0; i < tableContentLine.length; i++) {
                    tableContentLine[i] = new StringBuffer();
                }
                StringBuffer tableHeader = new StringBuffer();

                for (String key : header.keySet()) {
                    int length = 0;
                    length = header.get(key).toCharArray().length;
                    for (int i = 0; i < dataList.size(); i++) {
                        Object data = dataList.get(i).get(key);
                        if (data != null) {
                            tableContentLine[i].append(DATA_UNIT.replace("#{Data}", data.toString()));
                            int temp = data.toString().toCharArray().length;
                            length = Math.max(length, temp);
                        } else {
                            tableContentLine[i].append(DATA_UNIT.replace("#{Data}", ""));
                        }
                    }
                    // 默认设置一个字节长度为12px
                    tableHeader.append(HEADER_STYLE_LENGTH.replace("#{Length}", length * 12 + "").replace("#{Header}", header.get(key)));
                }

                for (int i = 0; i < tableContentLine.length; i++) {
                    if (i % 2 == 0) {
                        tableContent.append(SINGLE_LINE.replace("#{Line}", tableContentLine[i]));
                    } else {
                        tableContent.append(DOUBLE_LINE.replace("#{Line}", tableContentLine[i]));
                    }
                }
                htmlModel = htmlModel.replace("#{Content}", description).replace("#{Header}", tableHeader.toString()).replace("#{Table}", tableContent.toString());
            }
            logger.debug("Content builded, send email.");
            if (attachedFileArrayList == null) {
                attachedFileArrayList = new ArrayList<>();
            }
            MailSender.sendHtmlEmail(config, receiverMails, null, null, subject, htmlModel, attachedFileArrayList, "exception-email", false);
        } catch (Exception e) {
            logger.error("sendExceptionEmailError: Msg:[" + e.getMessage() + "]", e);
            throw new ServiceException("sendExceptionMailError:Msg[" + e.getMessage() + "]");
        }

    }

    @Override
    public void sendExceptionEmail(String subject, String description, Map<String, String> header, List<Map<String, Object>> dataList, String receiveType, Integer modelType, ArrayList<AttachedFile> attachedFileArrayList) {
        List<String> receiverTypeList = new ArrayList<>();
        receiverTypeList.add(receiveType);
        this.sendExceptionMail(subject, description, header, dataList, receiverTypeList, modelType, attachedFileArrayList);
    }

    @Override
    public void sendEmailByFreeMarkerTemplate(String subject, Map<String, Object> data, List<String> receiveTypeList, Integer modelType) {
        MailConfig config = mailConfigMapper.selectMailConfig();
        ArrayList<String> receiveMails = new ArrayList<>();
        Set<String> mailSet = new HashSet<>();
        for (int i = 0; i < receiveTypeList.size(); i++) {
            ArrayList<String> tempList = mailListMapper.selectMailListByReceiverType(receiveTypeList.get(i), null, null);
            if (tempList != null && !tempList.isEmpty()) {
                mailSet.addAll(tempList);
            }
        }
        receiveMails.addAll(mailSet);
        if(CollectionUtils.isEmpty(receiveMails)){
            logger.warn("Not receiver is set, the email(" + subject +") will not be sent!");
            return;
        }

        logger.debug("Top receiver: {}", receiveMails);

        try{
            MailModel queryAllMailModel = mailModelMapper.queryMailModelByType(modelType);
            if(queryAllMailModel == null){
                logger.error("Exception Email Template is not found!");
                return;
            }

            Template template = new Template(null, queryAllMailModel.getEn(), FREEMARKER_CFG);
            StringWriter out = new StringWriter();
            template.process(data, out);
            out.close();
            // 发送Email
            MailSender.sendHtmlEmail(config, receiveMails, null, null, subject, out.toString(), new ArrayList<>(), null, false);
        }catch (Exception e){
            logger.error("sendExceptionEmailError:Msg:[" + e.getMessage() + "]", e);
            throw new RuntimeException("sendExceptionEmailError:Msg:[" + e.getMessage() + "]");
        }
    }
}
