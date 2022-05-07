package com.after_dark.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
