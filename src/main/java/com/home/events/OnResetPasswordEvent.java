package com.home.events;

import org.springframework.context.ApplicationEvent;

public class OnResetPasswordEvent extends ApplicationEvent {
    private String email;
    private String randomPasswd;

    public OnResetPasswordEvent(String email, String randomPasswd) {
        super(email);
        this.email = email;
        this.randomPasswd = randomPasswd;
    }

    public String getEmail() {
        return email;
    }

    public String getRandomPasswd() {
        return randomPasswd;
    }
}
