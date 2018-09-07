package com.home.dto;

import com.home.entities.Gender;
import com.home.entities.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Date;

public class NewUserDto {
    @NotBlank(message = "This field is mandatory")
    private String firstName;
    @NotBlank(message = "This field is mandatory")
    private String lastName;
    private String gender;
    @NotBlank(message = "This field is mandatory")
    @Email(message = "Wrong e-mail format")
    private String email;
    @NotBlank(message = "This field is mandatory")
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_-])(?=\\S+$).{8,}$",
//            message = "Password should consist of one of a-z, A-Z, @#$%^&+=-_, 0-9 should have more then 8 characters and should not contain whitespaces")
    private String passwd;
    @NotBlank(message = "This field is mandatory")
    private String confirmPasswd;
    @Past(message = "Date of birth should be in the past")
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

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
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

    public String getConfirmPasswd() {
        return confirmPasswd;
    }

    public void setConfirmPasswd(String confirmPasswd) {
        this.confirmPasswd = confirmPasswd;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public User convertToEntity(PasswordEncoder passwordEncoder) {
        User user = new User();

        user.setEmail(getEmail());
        user.setFirstName(getFirstName());
        user.setLastName(getLastName());
        user.setGender(Gender.valueOf(getGender()));
        user.setDob(getDob());
        user.setEncryptedPasswd(passwordEncoder.encode(getPasswd()));

        return user;
    }
}
