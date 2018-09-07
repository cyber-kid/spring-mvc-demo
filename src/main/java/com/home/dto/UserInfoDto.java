package com.home.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.util.Date;

public class UserInfoDto {
    @NotBlank(message = "This field is mandatory")
    private String firstName;
    @NotBlank(message = "This field is mandatory")
    private String lastName;
    private String gender;
    @NotBlank(message = "This field is mandatory")
    @Email(message = "Wrong e-mail format")
    private String email;
    @Past(message = "Date of birth should be in the past")
    @DateTimeFormat(pattern="dd-MMM-YYYY")
    private Date dob;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
}
