package com.wowguild.web_api.handler;

import com.wowguild.common.dto.api.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Duration;

import static com.wowguild.web_api.constants.SecurityConstants.SESSION_TIMEOUT_SECONDS;
import static com.wowguild.web_api.constants.SecurityConstants.USER_LOGIN_SUCCESS_MSG;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final ObjectMapper mapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException {
        req.getSession().setMaxInactiveInterval(Math.toIntExact(SESSION_TIMEOUT_SECONDS));;

        clearAuthenticationAttributes(req);
        // Send 200 OK status after login without redirecting
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(mapper.writeValueAsString(new ApiResponse<>(USER_LOGIN_SUCCESS_MSG, 200)));
    }
}
