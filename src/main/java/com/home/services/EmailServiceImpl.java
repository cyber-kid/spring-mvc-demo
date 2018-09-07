package com.home.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${app.mail.registrationConfirmationSubject}")
    private String registrationConfirmationSubject;
    @Value("${app.mail.resetPasswordSubject}")
    private String resetPasswordSubject;

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender, TemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    public void sendRegistrationConfirmEmail(String to, String confirmationURL) {
        String emailBody = buildRegistrationConfirmationMessage(confirmationURL);

        sendEmail(to, registrationConfirmationSubject, emailBody);
    }

    @Override
    public void sendResetPasswdEmail(String to, String randomPasswd) {
        String emailBody = buildResetPasswdMessage(to, randomPasswd);

        sendEmail(to, resetPasswordSubject, emailBody);
    }

    private void sendEmail(String to, String subject, String body) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
        };
        try {
            emailSender.send(messagePreparator);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

    private String buildRegistrationConfirmationMessage(String link) {
        Context context = new Context();
        context.setVariable("confirmationLink", link);
        return templateEngine.process("emails/registration-confirm", context);
    }

    private String buildResetPasswdMessage(String email, String randomPasswd) {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("randomPasswd", randomPasswd);
        return templateEngine.process("emails/reset-passwd", context);
    }
}
