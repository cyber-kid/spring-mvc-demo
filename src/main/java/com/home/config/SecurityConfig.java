package com.home.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${app.rememberMe.tokenValiditySeconds}")
    private int tokenValiditySeconds;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureAuthManager(AuthenticationManagerBuilder authenticationManagerBuilder,
                                     UserDetailsService userDetailsService){
        try {
            authenticationManagerBuilder.userDetailsService(userDetailsService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        // /userInfo page requires login as ROLE_USER or ROLE_ADMIN.
        // If no login, it will redirect to /login page.
        http.authorizeRequests()
                .antMatchers("/user-info", "/edit-user", "/change-passwd")
                .access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");

        // For ADMIN only.
        http.authorizeRequests()
                .antMatchers("/admin")
                .access("hasRole('ROLE_ADMIN')");

        // Config for Login Form
        http.authorizeRequests().and().formLogin()
                // Submit URL of login page.
                .loginPage("/login")//
                .defaultSuccessUrl("/user-info")
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                // Config for Logout Page
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID");

        http.authorizeRequests().and() //
                .rememberMe().key("SpringMVCDemoSecretKey")
                .tokenValiditySeconds(tokenValiditySeconds);

    }
}
