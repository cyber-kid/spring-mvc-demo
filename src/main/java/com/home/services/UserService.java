package com.home.services;

import com.home.dto.NewUserDto;
import com.home.dto.ResetPasswordDto;
import com.home.dto.UserInfoDto;
import com.home.entities.User;
import com.home.utils.VerificationTokenState;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    void createNewUserAccount(NewUserDto newUserDto);
    boolean isExistingUser(String email);
    UserInfoDto getUserDtoByEmail(String email);
    String createVerificationToken(User user);
    VerificationTokenState confirmRegistration(String tokenStr);
    void generateNewVerificationToken(String tokenStr);
    void updateUserDetails(String email, UserInfoDto userInfoDto, HttpServletRequest request);
    boolean changeUserPasswd(String email, ResetPasswordDto passwordDto, HttpServletRequest request);
    void resetPasswd(String email);
}
