package com.wowguild.web_api.handler;

import com.wowguild.common.dto.api.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

import static com.wowguild.web_api.constants.SecurityConstants.USER_LOGIN_FAIL_MSG;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginFailHandler extends SimpleUrlAuthenticationFailureHandler {
    private final ObjectMapper mapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String message = exception.getMessage();
        log.error("auth failure {}", message);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(new ApiResponse<>(USER_LOGIN_FAIL_MSG, 401)));
    }
}
