package com.wowguild.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;


import java.io.IOException;
import java.time.Duration;

public class SessionTimeoutAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public final Duration sessionTimeout;

    public SessionTimeoutAuthSuccessHandler(Duration sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws ServletException, IOException {
        req.getSession().setMaxInactiveInterval(Math.toIntExact(sessionTimeout.getSeconds()));
        res.addCookie(new Cookie("message","Successful"));
        super.onAuthenticationSuccess(req, res, auth);
    }
}
