package com.home.services;

import com.home.dto.NewUserDto;
import com.home.dto.ResetPasswordDto;
import com.home.dto.UserInfoDto;
import com.home.entities.Gender;
import com.home.entities.Role;
import com.home.entities.User;
import com.home.entities.VerificationToken;
import com.home.events.OnRegistrationCompleteEvent;
import com.home.events.OnResetPasswordEvent;
import com.home.repositories.RoleRepository;
import com.home.repositories.UserRepository;
import com.home.repositories.VerificationTokenRepository;
import com.home.utils.VerificationTokenState;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.home.utils.VerificationTokenState.*;

@Service("userDetailsService")
public class UserServiceImpl implements UserDetailsService, UserService {
    private static final String ROLE_USER = "ROLE_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void createNewUserAccount(NewUserDto newUserDto) {
        User user = newUserDto.convertToEntity(passwordEncoder);

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findRoleByRoleName(ROLE_USER));

        user.setRoles(roles);

        userRepository.save(user);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
    }

    @Override
    public boolean isExistingUser(String email) {
        User user = userRepository.findUserByEmail(email);

        return user != null;
    }

    @Override
    public UserInfoDto getUserDtoByEmail(String email) {
        return userRepository.findUserByEmail(email).convertToDto();
    }

    @Override
    public String createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);

        return token;
    }

    @Override
    public VerificationTokenState confirmRegistration(String tokenStr) {
        VerificationToken token = verificationTokenRepository.findByToken(tokenStr);

        if (token != null) {
            if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
                return TOKEN_EXPIRED;
            }

            User user = token.getUser();
            user.setEnabled(1);

            logIn(user);

            verificationTokenRepository.delete(token);

            return TOKEN_VERIFIED;
        } else {
            return TOKEN_MISSING;
        }
    }


    @Override
    public void generateNewVerificationToken(String tokenStr) {
        VerificationToken token = verificationTokenRepository.findByToken(tokenStr);
        User user = token.getUser();

        verificationTokenRepository.delete(token);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
    }

    @Override
    public void updateUserDetails(String email, UserInfoDto userInfoDto, HttpServletRequest request) {
        User user = userRepository.findUserByEmail(email);
        boolean reLogIn = false;

        user.setFirstName(userInfoDto.getFirstName());
        user.setLastName(userInfoDto.getLastName());
        user.setDob(userInfoDto.getDob());
        user.setGender(Gender.valueOf(userInfoDto.getGender()));

        if (!user.getEmail().equalsIgnoreCase(userInfoDto.getEmail())) {
            user.setEmail(userInfoDto.getEmail());
            reLogIn = true;
        }

        userRepository.save(user);

        if (reLogIn) {
            logOut(request);
            logIn(user);
        }
    }

    @Override
    public boolean changeUserPasswd(String email, ResetPasswordDto passwordDto, HttpServletRequest request) {
        User user = userRepository.findUserByEmail(email);

        if (passwordEncoder.matches(passwordDto.getOldPasswd(), user.getEncryptedPasswd())) {
            user.setEncryptedPasswd(passwordEncoder.encode(passwordDto.getNewPasswd()));

            userRepository.save(user);

            logOut(request);
            logIn(user);

            return true;
        } else {
            return false;
        }

    }

    @Override
    public void resetPasswd(String email) {
        User user = userRepository.findUserByEmail(email);
        String randomPasswd = generateRandomPassword();

        user.setEncryptedPasswd(passwordEncoder.encode(randomPasswd));

        userRepository.save(user);

        eventPublisher.publishEvent(new OnResetPasswordEvent(email, randomPasswd));
    }

    private void logIn(User user) {
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void logOut(HttpServletRequest request) {
        try {
            request.logout();
        } catch (ServletException e) {
            //ToDo log exception
        }
    }

    private String generateRandomPassword() {
        List<CharacterRule> rules = new ArrayList<>();
        rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
        rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
        rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));
        rules.add(new CharacterRule(EnglishCharacterData.Special, 1));

        PasswordGenerator generator = new PasswordGenerator();

        return generator.generatePassword(8, rules);
    }
}
