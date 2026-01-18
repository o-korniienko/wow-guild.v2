package com.wowguild.web_api.config;

import com.wowguild.web_api.handler.LoginFailHandler;
import com.wowguild.web_api.handler.LoginSuccessHandler;
import com.wowguild.web_api.handler.NoRedirectLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableJdbcHttpSession
public class WebSecurityConfig {
    @Value("${app.security.origin.fe-url}")
    private String FE_ORIGIN;

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        return new DefaultHttpFirewall();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http, LoginFailHandler loginFailHandler, LoginSuccessHandler loginSuccessHandler) {
        http
                //.cors(cors -> cors.disable())
                //.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf ->
                        csrf
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                                .ignoringRequestMatchers(
                                        "/api/v1/perform_login",
                                        "/api/v1/user/registration",
                                        "/api/v1/simple_chat_web_socket/**"
                                ))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/index*",
                                "/*.js",
                                "/*.js.map",
                                "/*.html",
                                "/*.css",
                                "/*.css.map",
                                "/*.json",
                                "/*.ico",
                                "/*.jsx",
                                "/static/**",
                                "/error")
                        .permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/",
                                "/api/v1/info/get-about-guild-messages",
                                "/api/v1/user/get-active",
                                "/api/v1/info/get-greeting-message")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/user/registration")
                        .permitAll()
                        .anyRequest().authenticated()
                );
        http
                .formLogin(formLogin -> formLogin
                        .failureHandler(loginFailHandler)
                        .loginProcessingUrl("/api/v1/perform_login")
                        .successHandler(loginSuccessHandler)
                        .permitAll());
        http
                .logout(logout -> logout
                        .logoutUrl("/api/v1/logout")
                        .logoutSuccessHandler(new NoRedirectLogoutSuccessHandler())
                        .permitAll());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Collections.singletonList(FE_ORIGIN));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        List<String> settings = new ArrayList<>();
        settings.add("Access-Control-Allow-Origin");
        settings.add("Access-Control-Allow-Methods");
        settings.add("Access-Control-Allow-Headers");
        settings.add("Access-Control-Max-Age");
        settings.add("Access-Control-Request-Headers");
        settings.add("Access-Control-Request-Method");

        configuration.setExposedHeaders(settings);

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("my_guild_session_id");
        return serializer;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}
