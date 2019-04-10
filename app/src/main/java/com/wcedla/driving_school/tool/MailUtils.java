package com.wcedla.driving_school.tool;

import com.orhanobut.logger.Logger;
import com.wcedla.driving_school.listener.EmailSendStatusInterface;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.wcedla.driving_school.constant.Config.DEBUG;

public class MailUtils {

    //发件人邮箱地址（必填自己申请的）
    private final String MAIL_FROM_EMAIL = "wcedla@qq.com";
    //授权码（必填自己申请的）：成功开启IMAP/SMTP服务，在第三方客户端登录时，腾讯提供的授权码。注意不是邮箱密码
    private final String MAIL_FROM_PWD = "ameviecyzpdkbjhf";
    //发件人用户名(随意改写)
    private final String MAIL_FROM_NAME = "wcedla";
    //收件人用户名(随意改写),现方案是根据本应用的用户名设置这个参数
    //private final String MAIL_TO_NAME = "userformyapp";
    //是否打印发送邮件的调试提示信息,现方案是跟是否打印Log的debug模式的标志位一致
    //private boolean isDebug = true;
    //发送邮件服务器：smtp.qq.com
    private final String MAIL_SMTP_HOST = "smtp.qq.com";
    //使用465或587端口
    private final String MAIL_SMTP_PORT = "587";
    //设置使用验证
    private final String MAIL_SMTP_AUTH = "true";
    //使用 STARTTLS安全连接,qq邮箱必须开启
    private final String MAIL_SMTP_STARTTLS_ENABLE = "true";
    //邮箱工具类实例
    private static MailUtils instance;

    public static MailUtils getInstance() {
        if (instance == null) {
            synchronized (MailUtils.class) {
                if (instance == null) {
                    instance = new MailUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 发送邮箱验证码
     *
     * @param emailTo     接收邮件用户的邮箱地址
     * @param emailToName 接收邮件用户的名称
     * @param subject     发送邮件主题
     * @param content     发送邮件内容
     * @return
     */
    public int sendEmail(String emailTo, String emailToName, String subject, String content, EmailSendStatusInterface sendStatusInterface) {
        Properties props = new Properties();
        Address addressTo;
        props.put("mail.smtp.host", MAIL_SMTP_HOST);
        props.put("mail.smtp.port", MAIL_SMTP_PORT);//使用465或587端口
        props.put("mail.smtp.auth", MAIL_SMTP_AUTH);//设置使用验证
        props.put("mail.smtp.starttls.enable", MAIL_SMTP_STARTTLS_ENABLE);//使用 STARTTLS安全连接,qq邮箱必须开启
        try {
            PopupAuthenticator auth = new PopupAuthenticator();
            Session session = Session.getInstance(props, auth);
            session.setDebug(DEBUG);//打印Debug信息
            MimeMessage message = new MimeMessage(session);
            Address addressFrom = new InternetAddress(MAIL_FROM_EMAIL, MAIL_FROM_NAME);//第一个参数为发件人邮箱地址；第二个参数为发件人用户名(随意改写)
            if (!"".equals(emailToName)) {
                addressTo = new InternetAddress(emailTo, emailToName);//第一个参数为接收方电子邮箱地址；第二个参数为接收方用户名
            } else {
                addressTo = new InternetAddress(emailTo);//第一个参数为接收方电子邮箱地址
            }
            message.setSubject(subject, "UTF-8");
//            message.setText(content,"UTF-8");
            message.setContent(content, "text/html;charset=UTF-8");
            //发件方
            message.setFrom(addressFrom);
            // message.addRecipient(Message.RecipientType.CC, addressFrom);
            message.setSentDate(new Date());//设置日期
            /**
             * 伪装成outlook邮箱发送的邮件，一定程度防止放入垃圾邮箱
             */
            message.addHeader("X-Priority", "2");
            message.addHeader("X-MSMail-Priority", "Normal");
            message.addHeader("X-Mailer", "Microsoft Outlook Express 6.00.2900.2869");
            message.addHeader("X-MimeOLE", "Produced By Microsoft MimeOLE V6.00.2900.2869");
            message.addHeader("ReturnReceipt", "1");
            //收件方
            message.addRecipient(Message.RecipientType.TO, addressTo);
            message.saveChanges();
            //为了一定程度防止邮件被放入垃圾箱
            Transport transport = session.getTransport("smtp");
            transport.connect(MAIL_SMTP_HOST, MAIL_FROM_EMAIL, MAIL_FROM_PWD);
            transport.send(message);
            transport.close();
            Logger.d("邮件发送成功！");
            sendStatusInterface.onSendSuccess();
            return -1;
        } catch (Exception e) {
            System.out.println(e.toString());
            Logger.d("邮件发送失败！失败原因：" + e.toString());
            sendStatusInterface.onSendFailed();
            return 0;
        }
    }

    class PopupAuthenticator extends Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(MAIL_FROM_EMAIL, MAIL_FROM_PWD);
        }
    }

}
