package com.wowguild.web_api.config;

import com.wowguild.web_api.handler.LoginFailHandler;
import com.wowguild.web_api.handler.LoginSuccessHandler;
import com.wowguild.web_api.handler.NoRedirectLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {


    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        return new DefaultHttpFirewall();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                //.cors(cors -> cors.disable())
                //.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configure(http))
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(
                                "/perform_login",
                                "/registration"
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
                                antMatcher(HttpMethod.GET, "/"),
                                antMatcher(HttpMethod.GET, "/login_in"),
                                antMatcher(HttpMethod.GET, "/get_about_us_messages"),
                                antMatcher(HttpMethod.GET, "/get_user"),
                                antMatcher(HttpMethod.GET, "/get_greeting_message"))
                        .permitAll()
                        .anyRequest().authenticated()
                );
        http
                .formLogin(formLogin -> formLogin
                        .failureHandler(new LoginFailHandler())
                        .loginPage("/login_in")
                        .loginProcessingUrl("/perform_login")
                        .successHandler(new LoginSuccessHandler(Duration.ofHours(6)))
                        .permitAll());
        http
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(new NoRedirectLogoutSuccessHandler())
                        .permitAll());

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            web.debug(false)
                    /*.ignoring()
                    .requestMatchers("/perform_login")*/;
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3006"));
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

    /*@Bean
    protected UserDetailsService userDetailsService() {
*//*        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("alex")
                        .password("123")
                        .roles("USER")
                        .build();
        return new InMemoryUserDetailsManager(user);*//*
        return userService;
    }*/
}
