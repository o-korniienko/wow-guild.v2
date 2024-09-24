package com.wowguild.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class LoginFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String message = exception.getMessage();
        log.error("auth failure {}", message);
        if (message.contains("UserDetailsService returned null")) {
            response.addCookie(new Cookie("message", "DoesNotExist"));
        }
        if (message.contains("Bad credentials")) {
            response.addCookie(new Cookie("message", "DoesNotExist"));
        }

        //super.setDefaultFailureUrl("http://localhost:3000/login");
        super.onAuthenticationFailure(request, response, exception);
    }
}
