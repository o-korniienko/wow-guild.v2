package com.wowguild.config;

import com.wowguild.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import java.time.Duration;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        return firewall;
    }


    @Autowired
    public LoginFailHandler failHandler;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/index*")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin.failureHandler(failHandler)
                        //.loginPage("/login_in")
                        .loginProcessingUrl("/perform_login")
                        .successHandler(new SessionTimeoutAuthSuccessHandler(Duration.ofHours(6))).permitAll()
                        .permitAll()).cors(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            web.debug(false)
                    .ignoring().requestMatchers("/*/*.js", "/*/*.html", "/*/*.css", "/static/**", "/*.js", "/*.json", "/*.ico", "/*.jsx",
                            "/get_about_us_messages", "/get_user", "/get_greeting_message*", "/save_greeting*", "/registration*");
        };
    }

    @Bean
    protected UserDetailsService userDetailsService() {
        /*try {
            auth.userDetailsService(userService)
                    .passwordEncoder(passwordEncoder);

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        return userService;
    }
}
