package com.bwongo.core.notify_mgt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

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

    public void sendEmail() throws MessagingException {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message);

        helper.setSubject(mailSubject);
        helper.setFrom(fromEmail);
        helper.setTo("");

        helper.setText(mailBody, Boolean.TRUE);

        mailSender.send(message);
    }
}
