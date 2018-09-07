package com.home.events;

import com.home.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OnResetPasswordListener implements ApplicationListener<OnResetPasswordEvent> {
    private final EmailService emailService;

    @Autowired
    public OnResetPasswordListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(OnResetPasswordEvent event) {
        emailService.sendResetPasswdEmail(event.getEmail(), event.getRandomPasswd());
    }
}
