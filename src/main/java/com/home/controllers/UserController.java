package com.home.controllers;

import com.home.dto.NewUserDto;
import com.home.dto.ResetPasswordDto;
import com.home.dto.UserInfoDto;
import com.home.services.UserService;
import com.home.utils.VerificationTokenState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Locale;

@Controller
public class UserController {
    private final UserService userService;
    private final MessageSource messageSource;


    @Autowired
    public UserController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @GetMapping("/register")
    public String register(@ModelAttribute("user") NewUserDto user) {
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute("user") NewUserDto user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        String message;

        if (userService.isExistingUser(user.getEmail())) {
            message = messageSource.getMessage("duplicated_email", new Object[]{}, Locale.US);
            model.addAttribute("duplicatedEmail", message);

            return "register";
        }

        if (!user.getPasswd().equals(user.getConfirmPasswd())) {
            message = messageSource.getMessage("wrong_confirm_passwd", new Object[]{}, Locale.US);
            model.addAttribute("wrongConfirmPasswd", message);

            return "register";
        }

        userService.createNewUserAccount(user);

        message = messageSource.getMessage("confirm_email", new Object[]{}, Locale.US);
        model.addAttribute("message", message);

        return "info-page";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) boolean error, Model model) {
        if (error) {
            String message = messageSource.getMessage("log_in_error", new Object[]{}, Locale.US);
            model.addAttribute("error", message);
        }

        return "login";
    }

    @GetMapping("/user-info")
    public String userInfo(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserDtoByEmail(principal.getName()));

        return "user-info";
    }

    @GetMapping("/registration-сonfirm")
    public String registrationConfirm(@RequestParam(value = "token") String token, Model model) {
        VerificationTokenState result = userService.confirmRegistration(token);
        String message = "";

        switch (result) {
            case TOKEN_EXPIRED:
                message = messageSource.getMessage("token_expired", new Object[]{token}, Locale.US);
                break;
            case TOKEN_MISSING:
                message = messageSource.getMessage("token_missing", new Object[]{}, Locale.US);
                break;
            case TOKEN_VERIFIED:
                return "index";
        }

        model.addAttribute("message", message);

        return "info-page";
    }

    @GetMapping("/generate-verification-token")
    public String generateVerificationToken(@RequestParam(value = "token") String token, Model model) {
        userService.generateNewVerificationToken(token);

        String message = messageSource.getMessage("confirm_email", new Object[]{}, Locale.US);
        model.addAttribute("message", message);

        return "info-page";
    }

    @GetMapping("/edit-user")
    public String editUser(Model model, Principal principal) {
        UserInfoDto user = userService.getUserDtoByEmail(principal.getName());
        model.addAttribute("user", user);

        return "edit-user";
    }

    @PostMapping("/edit-user")
    public String editUserPost(@Valid @ModelAttribute("user") UserInfoDto user,
                               BindingResult bindingResult,
                               Model model,
                               HttpServletRequest request,
                               Principal principal) {
        if (bindingResult.hasErrors()) {
            return "edit-user";
        }

        if (!principal.getName().equalsIgnoreCase(user.getEmail())) {
            if (userService.isExistingUser(user.getEmail())) {
                String message = messageSource.getMessage("duplicated_email", new Object[]{}, Locale.US);
                model.addAttribute("duplicatedEmail", message);

                return "edit-user";
            }
        }

        userService.updateUserDetails(principal.getName(), user, request);
        return "redirect:/user-info";
    }

    @GetMapping("/change-passwd")
    public String changePasswd(@ModelAttribute("resetPasswordDto") ResetPasswordDto resetPasswordDto) {
        return "change-passwd";
    }

    @PostMapping("/change-passwd")
    public String changePasswdPost(@Valid @ModelAttribute("resetPasswordDto") ResetPasswordDto resetPasswordDto,
                                   BindingResult bindingResult,
                                   Model model,
                                   Principal principal,
                                   HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "change-passwd";
        }

        String message;

        if (resetPasswordDto.getNewPasswd().equals(resetPasswordDto.getConfirmPasswd())) {
            boolean passwdChanged = userService.changeUserPasswd(principal.getName(), resetPasswordDto, request);

            if (!passwdChanged) {
                message = messageSource.getMessage("wrong_old_passwd", new Object[]{}, Locale.US);
                model.addAttribute("wrongOldPassd", message);

                return "change-passwd";
            } else {
                message = messageSource.getMessage("passwd_changed", new Object[]{}, Locale.US);
                model.addAttribute("message", message);

                return "info-page";
            }
        } else {
            message = messageSource.getMessage("wrong_confirm_passwd", new Object[]{}, Locale.US);
            model.addAttribute("wrongConfirmPasswd", message);

            return "change-passwd";
        }
    }

    @GetMapping("/reset-passwd")
    public String resetPasswd(@ModelAttribute("email") String email) {
        return "reset-passwd";
    }

    @PostMapping("/reset-passwd")
    public String resetPasswdPost(@ModelAttribute("email") String email, Model model) {
        String message;

        if (!userService.isExistingUser(email)) {
            message = messageSource.getMessage("no_user", new Object[]{}, Locale.US);
            model.addAttribute("error", message);

            return "reset-passwd";
        }

        userService.resetPasswd(email);

        message = messageSource.getMessage("reset_passwd_done", new Object[]{}, Locale.US);
        model.addAttribute("message", message);

        return "info-page";
    }
}
