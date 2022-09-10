package com.zhf.util;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;
import com.zhf.entity.model.mail.MailConfig;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.search.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 邮件接收类
 */
public class EmailUtil {

    private static Logger logger = LoggerFactory.getLogger(EmailUtil.class);
    private static ThreadLocal<Store> store = new ThreadLocal<>();
    private static ThreadLocal<Folder> folder = new ThreadLocal<>();

    private static final String TIME_Z0NE = "Aria/Shanghai";

    /**
     * 邮箱连接
     *
     * @param mailConfig 邮件配置对象
     */
    public static void connect(MailConfig mailConfig) {
        try {
            long start = System.currentTimeMillis();
            // 获取邮箱默认连接session
            Session session = Session.getInstance(mailConfig.getReceiverProperties());
            // 设置为调试模式
            session.setDebug(false);

            // 获取连接的 store
            store.set(session.getStore(mailConfig.getMailProtocol()));
            // 连接目标邮箱
            store.get().connect(mailConfig.getMailUsername(), mailConfig.getMailPassword());

            // 读取邮件内容
            folder.set(store.get().getFolder("INBOX"));
            // 邮件打开方式为读写
            folder.get().open(Folder.READ_WRITE);
            logger.info("扫描邮件总数： " + folder.get().getMessageCount() + ", 扫描时长：" + (System.currentTimeMillis() - start));
            logger.info("邮件连接成功.");
        } catch (Exception ex) {
            logger.error("邮件配置为" + JSONObject.fromObject(mailConfig).toString() + ",连接出现错误： ", ex);
        }

    }

    /**
     * 断开邮箱连接
     */
    public static void disconnect() {
        try {
            // 如果邮件 folder 是打开的，则关闭
            if (folder.get() != null && folder.get().isOpen()) {
                // 关闭folder
                folder.get().close(false);
            }

            // 如果邮件folder是打开，则关闭
            if (store.get() != null && store.get().isConnected()) {
                // 关闭 store
                store.get().close();
            }

            // 打印日常日志
            logger.info("断开邮箱连接成功.");
        } catch (Exception ex) {
            logger.error("断开邮箱连接出现异常：", ex);
        }
    }

    /**
     * 列出所有的邮件
     *
     * @return 邮件列表
     */
    public static Message[] listMessage() {
        // 默认值
        Message[] messages = null;
        try {
            // 如果当前未连接
            if (!store.get().isConnected()) {
                // 打印相关日志
                logger.error("当前store未连接，请检查原因.");
                // 返回默认对象
                return messages;
            }
            // 获取所有的又见对象
            messages = folder.get().getMessages();
            // 打印常规日志
            logger.info("共有" + messages.length + "封邮件.");
        } catch (Exception ex) {
            // 打印错误日志
            logger.error("列出所有的邮件出现异常：", ex);
        }
        return messages;
    }

    /**
     * 获取邮件唯一id
     *
     * @param mimeMessage
     * @return
     */
    public static String getMailUid(MimeMessage mimeMessage) {
        String mailUid = StringUtils.EMPTY;
        try {
            // 如果是pop3协议，进入pop3的分支处理
            if (folder.get() instanceof POP3Folder) {
                POP3Folder pOP3Folder = (POP3Folder) folder.get();
                // 获取邮件唯一id
                mailUid = pOP3Folder.getUID(mimeMessage);
            } else {
                // 如果是 imap 协议，进入pop3的分支处理
                IMAPFolder imapFolder = (IMAPFolder) folder.get();
                // 获取邮件唯一id
                mailUid = String.valueOf(imapFolder.getUID(mimeMessage));
            }
        } catch (Exception ex) {
            // 打印日常日志
            logger.error("获取邮件唯一uid出现异常！", ex);
        }
        return mailUid;
    }

    /**
     * 获得收件人的地址和姓名
     *
     * @param mimeMessage mimeMessage
     * @return 收件人
     */
    public static String getFrom(MimeMessage mimeMessage) throws Exception {
        // 获取邮件地址对象集合
        InternetAddress[] address = (InternetAddress[]) mimeMessage.getFrom();
        // 获取收件人地址
        String from = address[0].getAddress();
        // 如果收件人地址为空
        if (StringUtils.isEmpty(from)) {
            // 打印错误日志
            logger.error("邮件发送人为空.");
            // 返回默认空串结果
            return StringUtils.EMPTY;
        }

        // 获取收件人名称
        String personal = address[0].getPersonal();
        // 如果收件人姓名为空
        if (StringUtils.isEmpty(personal)) {
            // 打印日志
            logger.info("邮件发送人姓名为空.");
            // 返回空串结果
            return StringUtils.EMPTY;
        }
        // 拼接收件人地址和姓名
        return personal + "<" + from + ">";
    }

    /**
     * 获得邮件主题
     *
     * @param mimeMessage mimeMessage
     * @return subject
     */
    public static String getSubject(MimeMessage mimeMessage) {
        // 设置邮件主题的默认值
        String subject = StringUtils.EMPTY;
        try {
            subject = MimeUtility.decodeText(mimeMessage.getSubject());
        } catch (Exception ex) {
            // 打印错误日志
            logger.error("获取邮件主题出现异常:", ex);
        }
        // 返回邮件主题
        return subject;
    }

    /**
     * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中
     *
     * @param part     part
     * @param bodyText bodyText
     */
    public static void getMailContent(Part part, StringBuffer bodyText) throws Exception {
        // 获取内容类型
        String contentType = part.getContentType();
        // 获取name的位置index
        int nameIndex = contentType.indexOf("name");
        // 设置连接名称为false
        boolean conName = false;
        if (nameIndex != -1) {
            conName = true;
        }
        // 打印日志
        logger.info("CONTENTTYUPE: " + contentType);
        if (part.isMimeType("text/plain") && !conName) {
            // 打印日志
            logger.info("text: " + part.getContent());
        } else if (part.isMimeType("text/html") && !conName) {
            // 添加邮件内容中
            bodyText.append((String) part.getContent());
        } else if (part.isMimeType("multipart/*")) {
            // 获取附件内容
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            // 遍历循环获取
            for (int i = 0; i < counts; i++) {
                getMailContent(multipart.getBodyPart(i), bodyText);
            }
        } else if (part.isMimeType("message/rfc822")) {
            // 获取RFC
            getMailContent((Part) part.getContent(), bodyText);
        }
    }

    /**
     * 判断此邮件是否已读，如果未读取返回false，反之返回true
     *
     * @param mimeMessage mimeMessage
     * @return 是否是新邮件
     * @throws MessagingException MessagingException
     */
    public static boolean isNewEmail(MimeMessage mimeMessage) throws MessagingException {
        // 设置是否为新邮件的标识
        boolean isNewEmail = false;
        // 获取标志集合
        Flags flags = ((Message) mimeMessage).getFlags();
        // 获取系统标识
        Flags.Flag[] flag = flags.getSystemFlags();
        for (int i = 0; i < flag.length; i++) {
            // 如果邮件已经读取过了
            if (flag[i] == Flags.Flag.SEEN) {
                // 设置标识为新邮件
                isNewEmail = true;
                break;
            }
        }
        // 返回标识
        return isNewEmail;
    }

    /**
     * 获得邮件发送时间
     *
     * @param mimeMessage 邮件内容
     * @return 邮件发送时间
     * @throws MessagingException MessagingException
     */
    public static String getSentDate(MimeMessage mimeMessage) throws MessagingException {
        Date receivedDate = mimeMessage.getSentDate();
        if (receivedDate == null) {
            return StringUtils.EMPTY;
        }
        // 返回简单日期格式
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(receivedDate);
    }

    /**
     * 获取邮件中的附件内容
     *
     * @param messages messages
     * @return 获取附件内容
     * @throws MessagingException MessagingException
     * @throws IOException        IOException
     */
    public static List<BodyPart> getAttachment(Message messages) throws MessagingException, IOException {
        List<BodyPart> list = new ArrayList<>();
        if (messages.isMimeType("multipart/*")) {
            // 强转
            Multipart mp = (Multipart) messages.getContent();
            // 获取附件数量
            int count = mp.getCount();
            // 遍历
            for (int i = 0; i < count; i++) {
                BodyPart bp = mp.getBodyPart(i);
                list.add(bp);
            }
        }
        return list;
    }

    /**
     * 获取pop3解密获取附件名
     *
     * @param bp bp
     * @return 附件文件名
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     * @throws MessagingException           MessagingException
     */
    public static String getAttachmentName(BodyPart bp) throws UnsupportedEncodingException, MessagingException {
        // 文件名
        String fileName = bp.getFileName();
        if (StringUtils.isEmpty(fileName)) {
            return fileName;
        }
        // 文件名解密处理
        fileName = MimeUtility.decodeText(fileName);
        return fileName;
    }

    /**
     * 时间范围内的所有邮件
     *
     * @param mailConfig 邮箱默认配置
     * @param startTime  最早时间
     * @param endTime    最晚时间
     * @return 该时间范围内的邮件
     * @throws MessagingException MessagingException
     */
    public static List<Message> getSpecifiedEmailList0(MailConfig mailConfig, Date startTime, Date endTime) throws MessagingException {
        logger.info("begin to getSpecifiedEmailList, startTime:" + startTime + " ,endTime:" + endTime);
        List<Message> list = new ArrayList<>();
        // 链接邮件并获取信息
        connect(mailConfig);
        Message[] messages = listMessage();
        // 判断邮箱是否为空
        if (messages == null || messages.length < 1) {
            return list;
        }
        logger.info("getSpecifiedEmailList, messages:" + messages.length);
        // 遍历所有邮件
        for (Message message : messages) {
            // 判断邮件是否打开，没有打开的设置为打开
            if (!message.getFolder().isOpen()) {
                // 邮件设置为打开
                message.getFolder().open(Folder.READ_WRITE);
            }
            // 获取邮件内容
            MimeMessage mime = (MimeMessage) message;
            // 获取发送时间
            Date sendDate = mime.getSentDate();
            // 去除在时间取值范围内的所有邮件
            if (sendDate != null) {
                // 判断是否是开始时间
                boolean start = startTime == null || startTime.before(sendDate);
                /// 判断是否有结束时间
                boolean end = endTime == null || sendDate.before(endTime);
                // 取范围内的值
                if (start && end) {
                    // 添加邮件
                    list.add(message);
                }
            }
        }
        logger.info("end to getSpecifiedEmailList, startTime: " + startTime + " ,endTime: " + endTime);
        return list;
    }

    /**
     * 时间范围内的所有邮件
     *
     * @param mailConfig 邮箱默认配置
     * @param startTime  最早时间
     * @param endTime    最晚时间
     * @return
     * @throws MessagingException
     */
    public static List<Message> getSpecifiedEmailList(MailConfig mailConfig, Date startTime, Date endTime) throws MessagingException {
        logger.info("begin to getSpecifiedEmailList, startTime:" + startTime + " ,endTime:" + endTime);
        List<Message> list = new ArrayList<>();
        // 连接邮箱并获取信息
        Session session = Session.getInstance(mailConfig.getProperties(), null);
        Store store = session.getStore(mailConfig.getMailProtocol());
        store.connect(mailConfig.getMailHost(), mailConfig.getMailUsername(), mailConfig.getMailPassword());
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        // 读取日期区间的邮件
        SearchTerm searchTerm = new SentDateTerm(ComparisonTerm.GE, startTime);
        SearchTerm searchTerm1 = new SentDateTerm(ComparisonTerm.LE, endTime);
        SearchTerm term = new AndTerm(searchTerm1, searchTerm);
        Message[] messages = folder.search(term);
        // 判断邮箱是否为空
        if (messages == null || messages.length < 1) {
            return list;
        }
        logger.info(("getSpecifiedEmailList,messages:" + messages.length));
        // 遍历所有邮件
        for (Message message : messages) {
            // 判断邮件是否打开，没有打开·设置为打开
            if (!message.getFolder().isOpen()) {
                // 邮件设置为打开
                message.getFolder().open(Folder.READ_WRITE);
            }
            // 获取邮件内容
            MimeMessage mime = (MimeMessage) message;
            // 获取发件时间
            Date sendDate = mime.getSentDate();
            // 取出在时间屈指范围内的所有邮件
            if (sendDate != null) {
                // 判断是否有开始时间，
                Boolean start = startTime == null || startTime.before(sendDate);
                // 判断是否有结束时间
                Boolean end = endTime == null || sendDate.before(endTime);
                // 取出范围内的值
                if (start && end) {
                    // 添加邮件
                    list.add(message);
                }
            }
        }
        logger.info("end getSpecifiedEmailList, startTime:" + startTime + ",endTime: " + endTime);
        return list;
    }

    /**
     * 指定日期的所有邮件
     *
     * @param config       邮箱默认配置
     * @param receiverDate 收件日期过滤
     * @param startDate    开始日期
     * @return
     * @throws MessagingException
     */
    public static List<Message> getEmailListByDate(MailConfig config, Set<String> receiverDate, Date startDate) throws MessagingException {
        logger.info("begin to getEmailListByDate, receiverDate:" + receiverDate);
        List<Message> list = new ArrayList<>();
        // 连接邮箱并获取信息
        Session session = Session.getInstance(config.getProperties(), null);
        Store store = session.getStore(config.getMailProtocol());
        store.connect(config.getMailHost(), config.getMailUsername(), config.getMailPassword());
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        // 读取大于开始时间的邮件
        SearchTerm searchTerm = new SentDateTerm(ComparisonTerm.GE, startDate);
        Message[] messages = folder.search(searchTerm);
        // 判断邮箱是否为空
        if (messages == null || messages.length < 1) {
            return list;
        }
        logger.info("getEmailListByDate, messages:" + messages.length);
        // 遍历所有邮件
        for (Message message : messages) {
            // 判断邮件是否打开，没有打开·设置为打开
            if (!message.getFolder().isOpen()) {
                // 邮件设置为打开
                message.getFolder().open(Folder.READ_WRITE);
            }
            // 获取邮件内容
            MimeMessage mime = (MimeMessage) message;
            // 获取发件时间
            Date sendDate = mime.getSentDate();
            // 取出在时间屈指范围内的所有邮件
            if (sendDate != null) {
                // 判断是否为指定日期的邮件
                if (sendDate.after(startDate) && receiverDate.contains(DateUtil.dateToString(sendDate, "yyyyMMdd"))) {
                    // 添加邮件
                    list.add(message);
                }
            }
        }
        logger.info("end to getEmailListByDate, receiverDate:" + receiverDate);
        return list;
    }

    /**
     * 根据主题筛选邮件（正则）
     *
     * @param listMsg 所有邮件
     * @param pattern 正则表达式
     * @return 匹配后的邮件
     * @throws MessagingException MessagingException
     */
    public static List<Message> getSpecifiedEmailListBySubject(List<Message> listMsg, String pattern) throws MessagingException {
        // 返回结果
        Pattern r = Pattern.compile(pattern);
        List<Message> list = new ArrayList<>();
        // 判断邮件是否为空
        if (listMsg != null && !listMsg.isEmpty()) {
            // 遍历邮件
            for (Message message : listMsg) {
                // 获取邮件内容
                String subject = message.getSubject();

                // 现在创建 matcher 对象
                Matcher m = r.matcher(subject);
                // 是否匹配
                if (m.matches()) {
                    list.add(message);
                }
            }
        }
        return list;
    }

    public static Message[] listMessagesFromAFixedTime(MailConfig mailConfig, Date startTime, Date endTime) {
        // 链接邮箱并获取信息
        connect(mailConfig);
        // 默认值
        Message[] messages = null;
        try {
            // 如果当前未连接
            if (!store.get().isConnected()) {
                // 打印错误日志
                logger.error("档期store未连接，请检查原因.");
                // 返回默认对象
                return messages;
            }
            SearchTerm[] searchTerms = new SearchTerm[2];
            searchTerms[0] = new SentDateTerm(ComparisonTerm.GE, startTime);
            searchTerms[1] = new SentDateTerm(ComparisonTerm.LE, endTime);
            SearchTerm conditionTerm = new AndTerm(searchTerms);
            messages = folder.get().search(conditionTerm);
            // 打印常规日志
            logger.info("Mails between " + startTime + " and " + endTime + ", 共有" + messages.length + "封邮件.");
        } catch (Exception ex) {
            // 打印错误日志
            logger.error("列出所有邮件出现的异常：", ex);
        }
        return messages;
    }

    public static Message[] listCertainMessage(MailConfig mailConfig, String subject, Date startTime, Date endTime) {
        // 链接邮箱并获取信息
        connect(mailConfig);
        // 默认值
        Message[] messages = null;
        try {
            // 如果当前未连接
            if (!store.get().isConnected()) {
                // 打印相关日志
                logger.error("当前store未连接，请检查原因.");
                // 返回默认对象
                return messages;
            }
            // 根据条件搜索
            int size = 3;
            if (StringUtils.isEmpty(subject)) {
                size = 2;
            }
            SearchTerm[] searchTerms = new SearchTerm[size];
            searchTerms[0] = new SentDateTerm(ComparisonTerm.GE, startTime);
            searchTerms[1] = new SentDateTerm(ComparisonTerm.LE, endTime);
            if (StringUtils.isNotEmpty(subject)) {
                searchTerms[2] = new SubjectTerm(subject);
            }
            SearchTerm conditionTerm = new AndTerm(searchTerms);
            messages = folder.get().search(conditionTerm);
            // 打印日常日志
            logger.info("subject: " + subject + ", 共有 " + messages.length + " 封邮件.");
        } catch (Exception ex) {
            // 打印错误日志
            logger.error("列出所有的邮件出现异常：", ex);
        }
        return messages;
    }

    public static void sendMailWithAttach(MailConfig mailConfig, List<String> receivers, String subject, String context, List<File> files) {
        try {
            if (StringUtils.isBlank(mailConfig.getMailAddress())) {
                throw new RuntimeException("发件箱为空");
            }
            if (StringUtils.isBlank(mailConfig.getMailUsername()) || StringUtils.isBlank(mailConfig.getMailPassword())) {
                throw new RuntimeException("用户名或密码为空");
            }
            if (receivers.isEmpty()) {
                throw new RuntimeException("收件箱列表为空");
            }
            if (StringUtils.isEmpty(subject)) {
                subject = "来自" + mailConfig.getMailAddress() + "的邮件";
            }
            if (null == context) {
                context = "";
            }
            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailConfig.getMailUsername(), mailConfig.getMailPassword());
                }
            };
            Session session = Session.getInstance(mailConfig.getProperties(), auth);
            MimeMessage message = new MimeMessage(session);
            InternetAddress[] toAddress = new InternetAddress[receivers.size()];

            for (int i = 0; i < receivers.size(); i++) {
                toAddress[i] = new InternetAddress(receivers.get(i));
            }

            message.setFrom(new InternetAddress(mailConfig.getMailAddress()));
            message.setRecipients(MimeMessage.RecipientType.TO, toAddress);
            message.setSubject(subject, "UTF-8");

            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart body0 = new MimeBodyPart();
            body0.setContent(context, "text/html;charset=UTF-8");
            multipart.addBodyPart(body0);
            if (CollectionUtils.isNotEmpty(files)) {
                for (int i = 0; i < files.size(); i++) {
                    MimeBodyPart body = new MimeBodyPart();
                    body.setDataHandler(new DataHandler(new FileDataSource(files.get(i))));
                    body.setFileName(MimeUtility.encodeText(files.get(0).getName()));

                    multipart.addBodyPart(body);
                }
            }

            message.setContent(multipart);
            message.setSentDate(getCurrentTime());

            Transport.send(message);

            logger.info("sendMailWithAttach: 邮件发送成功");

        } catch (Exception ex) {
            logger.error("sendMailWithAttach error: ", ex);
            throw new RuntimeException("sendMailWithAttach error: ", ex);
        }
    }

    public static void saveDraftMailWithAttach(MailConfig mailConfig, List<String> receivers,
                                               String subject, String context, List<File> files, List<String> ccList) {
        logger.info("saveDraftMailWithAttach: mailAddress = " + mailConfig.getMailAddress() + " , user = " + mailConfig.getMailUsername() + ",password = " + mailConfig.getMailPassword()
                + " , receivers = " + receivers + ", subject = " + subject + ", ccList = " + ccList);

        Store store = null;

        try {
            if (StringUtils.isBlank(mailConfig.getMailAddress())) {
                throw new RuntimeException("发件箱为空");
            }
            if (StringUtils.isBlank(mailConfig.getMailUsername()) || StringUtils.isBlank(mailConfig.getMailPassword())) {
                throw new RuntimeException("用户名或密码为空");
            }
            if (receivers.isEmpty()) {
                throw new RuntimeException("收件箱列表为空");
            }
            if (StringUtils.isEmpty(subject)) {
                subject = "来自" + mailConfig.getMailAddress() + "的邮件";
            }

            if (null == context) {
                context = "";
            }

            Session session = Session.getInstance(mailConfig.getProperties(), null);
            store = session.getStore("imap");
            store.connect(mailConfig.getMailHost(), mailConfig.getMailUsername(), mailConfig.getMailPassword());
            MimeMessage message = new MimeMessage(session);
            List<Address> toAddressList = Lists.newArrayList();

            for (int i = 0; i < receivers.size(); i++) {
                if (StringUtils.isNotBlank(receivers.get(i))) {
                    toAddressList.add(new InternetAddress(receivers.get(i)));
                }
            }

            Address[] toAddress = new Address[toAddressList.size()];

            for (int i = 0; i < toAddressList.size(); i++) {
                toAddress[i] = toAddressList.get(i);
            }

            message.setFrom(new InternetAddress(mailConfig.getMailAddress()));
            message.setRecipients(MimeMessage.RecipientType.TO, toAddress);
            message.setSubject(subject, "UTF-8");

            // 设置抄送人
            if (ccList != null) {
                List<Address> toCcList = Lists.newArrayList();
                for (int i = 0; i < ccList.size(); ++i) {
                    if (!StringUtils.isBlank(ccList.get(i))) {
                        toCcList.add(new InternetAddress(ccList.get(i)));
                    }
                }

                if (!toCcList.isEmpty()) {
                    Address[] toCcAddress = new Address[toCcList.size()];
                    for (int i = 0; i < toCcList.size(); i++) {
                        toCcAddress[i] = toCcList.get(i);
                    }
                    message.setRecipients(MimeMessage.RecipientType.CC, toCcAddress);
                }
            }

            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart body0 = new MimeBodyPart();
            body0.setContent(context, "text/html;charset=UTF-8");
            multipart.addBodyPart(body0);

            int size = files.size();
            for (int i = 0; i < size; i++) {
                logger.info("saveDraftMailWithAttach: file" + i + " " + files.get(i).getAbsolutePath());
                MimeBodyPart body = new MimeBodyPart();
                body.setDataHandler(new DataHandler(new FileDataSource(files.get(i).getAbsolutePath())));
                body.setFileName(MimeUtility.encodeText(files.get(i).getName()));
                multipart.addBodyPart(body);
            }

            message.setContent(multipart);
            message.setSentDate(getCurrentTime());
            message.setFlag(Flags.Flag.DRAFT, true);
            message.saveChanges();
            Folder defaultFolder = store.getDefaultFolder();
            Folder[] allFolder = defaultFolder.list();
            Folder folder = null;
            for (int i = 0; i < allFolder.length; i++) {
                if (StringUtils.equals(allFolder[i].getFullName(), "草稿")) {
                    folder = folder.getFolder("草稿");
                } else if (StringUtils.equals(allFolder[i].getFullName(), "Drafts")) {
                    folder = store.getFolder("Drafts");
                }
            }

            if (folder != null) {
                folder.appendMessages(new Message[]{message});
                logger.info("saveDraftMailWithAttach: 邮件保存成功");
            } else {
                logger.error("open the folder failed, cannot find 'Drafts' or '草稿'");
                throw new RuntimeException("open the folder failed, cannot find 'Drafts' or '草稿'");
            }
        } catch (Exception ex) {
            logger.error("saveDraftMailWithAttach error: ", ex);
            throw new RuntimeException("saveDraftMailWithAttach error: ", ex);
        } finally {
            if (store != null && store.isConnected()) {
                try {
                    store.close();
                } catch (MessagingException ex) {
                    logger.error("saveDraftMailWithAttach close store error: ", ex);
                }
            }
        }

    }

    public static Date getCurrentTime() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.of(TIME_Z0NE)).toInstant());
    }

    /**
     * 忽略，不用，太啰嗦了。。。。。。。。。。。
     *
     * @param listMsg
     * @param config
     * @param dateConfig
     * @param path
     * @param data
     * @return
     * @throws MessagingException
     */
    public static Map<String, String> downLoadBySubjectAttachment(List<Message> listMsg, List<Map<String, String>> config, Map<String, String> dateConfig, String path, String data) throws MessagingException {
        Map<String, String> dataMap = new HashMap<>();
        if (CollectionUtils.isEmpty(listMsg)) {
            return dataMap;
        }
        // 给邮件按时间倒序排序
        Collections.sort(listMsg, (o1, o2) -> {
            try {
                return o1.getSentDate().before(o2.getSentDate()) ? 1 : -1;
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return 0;
        });
        // 遍历邮件
        for (Message message : listMsg) {
            String subject = message.getSubject().trim();
            // 先筛选出主题符合规则的配置
            List<Map<String, String>> filterConfig = config.stream().filter(item -> subject.matches(item.get("subjectRex"))).collect(Collectors.toList());
            // 有符合规则的则处理
            if (filterConfig.size() == 1) {
                String key = filterConfig.get(0).get("emailDownloadErrorKey");
                // 单主题附件的配置， 已经处理过的 不再处理， 修改下 otherMessage
                if (dataMap.containsKey(key)) {
                    String other = dataMap.get(key);
                    if (StringUtils.isBlank(other)) {
                        dataMap.put(key, "Received the same attachment for the same subject multiple times");
                    }
                    if (!other.contains("Received the same attachment for the same subject multiple times")) {
                        dataMap.put(key, other + ";Received the same attachment for the same subject multiple times");
                    }
                    continue;
                }
            }
        }
        return null;
    }

    /**
     * 忽略。。。。。。。。。。。。。。
     *
     * @param configMap
     * @param dateConfig
     * @param subject
     * @param sendDate
     * @return
     */
    public static String checkDate(Map<String, String> configMap, Map<String, String> dateConfig, String subject, Date sendDate) {
        // 邮件主题日期校验
        String format = configMap.get("subjectDateFormat");
        if (StringUtils.isNotBlank(format)) {
            // 获取发件时间
            String subjectDate = dateConfig.get(DateUtil.dateToString(sendDate, "yyyyMMdd"));
        }
        return "";
    }

    public static List<BodyPart> filterAttachment(List<BodyPart> bodyPartList, Map<String, String> configMap) {
        String attachmentRex = configMap.get("attachmentRex");
        if (StringUtils.isBlank(attachmentRex)) {
            return bodyPartList;
        }
        List<BodyPart> filterList = bodyPartList.stream().filter(bodyPart -> {
            try {
                return StringUtils.isNotBlank(bodyPart.getFileName()) && MimeUtility.decodeText(bodyPart.getFileName().trim()).matches(attachmentRex);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        return filterList;
    }

    /**
     * 下载邮件附件
     * @param bodyPartList  bodyPartList
     * @param downloadPath  下载路径
     * @param clear 是否清除路径文件
     * @return
     */
    public static String emailAttachmentDownLoad(List<BodyPart> bodyPartList, String downloadPath, Boolean clear) {
        try {
            File file = new File(downloadPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            // 下载前先清空目录
            if (clear) {
                File[] listFiles = file.listFiles();
                for (File temp : listFiles) {
                    temp.delete();
                }
            }
            for (BodyPart bodyPart : bodyPartList) {
                InputStream inputStream = bodyPart.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(new File(downloadPath + File.separator + bodyPart.getFileName()).toPath()));
                int len = -1;
                while ((len = bis.read()) != -1){
                    bos.write(len);
                    bos.flush();
                }
                bos.close();
                bis.close();
            }
            return "";
        } catch (Exception ex) {
            logger.error("邮件附件下载失败：", ex);
            return "邮件附件下载失败：" + ex.getMessage();
        }
    }


}
