package com.home.events;

import com.home.entities.User;
import com.home.services.UserService;
import com.home.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OnRegistrationCompleteListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    @Value("${app.mail.hostDomain}")
    private String hostDomain;

    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public OnRegistrationCompleteListener(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = userService.createVerificationToken(user);

        String confirmationUrl = hostDomain + "/registration-сonfirm?token=" + token;

        emailService.sendRegistrationConfirmEmail(user.getEmail(), confirmationUrl);
    }
}
