package com.home.services;

public interface EmailService {
    void sendRegistrationConfirmEmail(String to, String confirmationURL);
    void sendResetPasswdEmail(String email, String randomPasswd);
}
