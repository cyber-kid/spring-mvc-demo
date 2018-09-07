package com.home.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ResetPasswordDto {
    @NotBlank(message = "This field is mandatory")
    private String oldPasswd;
    @NotBlank(message = "This field is mandatory")
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_-])(?=\\S+$).{8,}$",
//            message = "Password should consist of one of a-z, A-Z, @#$%^&+=-_, 0-9 should have more then 8 characters and should not contain whitespaces")
    private String newPasswd;
    @NotBlank(message = "This field is mandatory")
    private String confirmPasswd;

    public String getOldPasswd() {
        return oldPasswd;
    }

    public void setOldPasswd(String oldPasswd) {
        this.oldPasswd = oldPasswd;
    }

    public String getNewPasswd() {
        return newPasswd;
    }

    public void setNewPasswd(String newPasswd) {
        this.newPasswd = newPasswd;
    }

    public String getConfirmPasswd() {
        return confirmPasswd;
    }

    public void setConfirmPasswd(String confirmPasswd) {
        this.confirmPasswd = confirmPasswd;
    }
}
