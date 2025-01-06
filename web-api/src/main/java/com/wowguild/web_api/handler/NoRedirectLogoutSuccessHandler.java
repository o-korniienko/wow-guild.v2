package com.wowguild.web_api.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class NoRedirectLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                org.springframework.security.core.Authentication authentication)
            throws IOException {
        // Do nothing on logout success (no redirect)
        response.setStatus(HttpServletResponse.SC_OK);  // Send 200 OK status
    }
}
