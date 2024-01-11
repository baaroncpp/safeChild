package com.bwongo.core.notify_mgt.service;

import com.bwongo.core.account_mgt.model.jpa.TAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/20/23
 **/
@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${notification.email.account-balance.subject}")
    private String mailSubject;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${notification.email.account-balance.body}")
    private String mailBody;

    private final JavaMailSender mailSender;

    public void send2Email(/*TAccount account*/) throws MessagingException {

        var toEmail = "baaronlubega1@gmail.com"; //account.getSchool().getEmail();
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message);

        helper.setSubject(String.format(mailSubject, "Aaron", 10000, "0762323"));
        helper.setFrom(fromEmail);
        helper.setTo(toEmail);

        helper.setText(mailBody, Boolean.TRUE);

        mailSender.send(message);
    }

    public void send3Email() throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress("info@nardconcepts.io"));
        message.setRecipients(MimeMessage.RecipientType.TO, "baaronlubega1@gmail.com");
        message.setSubject("Test email from Spring");

        String htmlContent = "<h1>This is a test Spring Boot email</h1>" +
                "<p>It can contain <strong>HTML</strong> content.</p>";
        message.setContent(htmlContent, "text/html; charset=utf-8");

        mailSender.send(message);
    }

    public void sendEmail() throws MessagingException, UnsupportedEncodingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("info@nardconcepts.io", "Your Name");
        helper.setTo("baaronlubega1@gmail.com");

        helper.setSubject(mailSubject);
        helper.setText(mailBody, true);

        mailSender.send(message);
    }
}
