package com.after_dark.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginFailHandler extends SimpleUrlAuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String message = exception.getMessage();
        System.out.println(message);

        if (message.contains("UserDetailsService returned null")){
            response.addCookie(new Cookie("message","DoesNotExist"));
        }
        if (message.contains("Bad credentials")){
            response.addCookie(new Cookie("message","WrongPassword"));
        }

        //super.setDefaultFailureUrl("http://localhost:3000/login");
        super.onAuthenticationFailure(request, response, exception);
    }
}
