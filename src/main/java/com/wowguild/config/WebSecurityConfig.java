package com.wowguild.config;

import com.wowguild.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import java.time.Duration;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        return firewall;
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Autowired
    public LoginFailHandler failHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/**/*.{js,html,css}", "/index*", "/static/**", "/*.js", "/*.json", "/*.ico", "/*.jsx",
                        "/get_about_us_messages","/get_user", "/get_greeting_message*","/save_greeting*", "/registration*")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .failureHandler(failHandler)
                //.loginPage("/login_in")
                .loginProcessingUrl("/perform_login")
                .successHandler(new SessionTimeoutAuthSuccessHandler(Duration.ofHours(6))).permitAll()
                .and()
                .logout()
                .permitAll();


    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        try {
            auth.userDetailsService(userService)
                    .passwordEncoder(passwordEncoder);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
