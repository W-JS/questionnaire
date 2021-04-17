package com.wjs.questionnaire.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailClient {

    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public boolean sendMail(String to, String subject, String content) {
        boolean flag = false;
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);//邮件发送者
            helper.setTo(to);//邮件接收者
            helper.setSubject(subject);//邮件主题
            helper.setText(content, true);//邮件内容
            mailSender.send(helper.getMimeMessage());
            flag = true;
//        } catch (MessagingException e) {// 发送给不存在的邮箱地址会报错
        } catch (Exception e) {
            logger.error("发送邮件失败: " + e.getMessage());
        }
        return flag;
    }

}
