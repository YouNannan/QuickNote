package com.younannan.tool;

import android.content.Context;

import com.younannan.data.DataCenter;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailProcessor {
    private static final boolean debug = false;
    private String senderAccount;
    private String senderPassword;
    private String host;
    private String from;

    public static EmailProcessor getDefaultInstance(Context context) {
        return new EmailProcessor(DataCenter.getEmailAccount(),
                DataCenter.getEmailPassword(context),
                "smtp.126.com",
                DataCenter.getEmailAccount());
    }

    public EmailProcessor(String theSenderAccount,String theSenderPassword,String theHost,String theFrom) {
        senderAccount = theSenderAccount;
        senderPassword = theSenderPassword;
        host = theHost;
        from = theFrom;
    }
    public boolean mail(String to,
                     String subject, String msgText1,
                     String filePath) {

        // create some properties and get the default Session
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", host);

        Session session = Session.getInstance(props);
        session.setDebug(debug);

        try {
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from,"笔录软件"));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);

            // create and fill the first message part
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(msgText1);

            // create the second message part
            MimeBodyPart mbp2 = new MimeBodyPart();

            // attach the file to the message
            mbp2.attachFile(filePath);


            // create the Multipart and add its parts to it
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);

            // add the Multipart to the message
            msg.setContent(mp);

            // set the Date: header
            msg.setSentDate(new Date());

            // send the message
            Transport transport = session.getTransport();
            //设置发件人的账户名和密码
            transport.connect(senderAccount, senderPassword);
            //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(msg,msg.getAllRecipients());
            return true;
        } catch (MessagingException | IOException ex) {
            //ex.printStackTrace();
            return false;
        }
    }

}
