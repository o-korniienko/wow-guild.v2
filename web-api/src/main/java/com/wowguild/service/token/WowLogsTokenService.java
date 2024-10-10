package com.wowguild.service.token;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wowguild.entity.Token;
import com.wowguild.model.TokenResponse;
import com.wowguild.sender.HttpSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class WowLogsTokenService implements TokenService {

    @Value("${wow.logs.id}")
    private String WOW_LOGS_ID;
    @Value("${wow.logs.secret}")
    private String WOW_LOGS_SECRET;
    @Value("${wow.logs.token.api}")
    private String wowLogsTokenApi;
    private final Gson gson;

    private final HttpSender httpSender;

    @Override
    public Token getToken() {
        Token token = new Token();
        try {
            String response = httpSender.sendRequest(wowLogsTokenApi, HttpMethod.POST, WOW_LOGS_ID, WOW_LOGS_SECRET);

            if (!response.isEmpty()) {
                TokenResponse tokenResponse = gson.fromJson(response, TokenResponse.class);
                token.setTag("wow_logs");
                token.setAccess_token(tokenResponse.getAccess_token());
                token.setExpires_in(tokenResponse.getExpires_in());
                token.setCreateTime(LocalDateTime.now());
            }
        } catch (JsonSyntaxException e) {
            log.error("Could not get WOWLogs token, error {}", e.getMessage());
        }
        return token;
    }
}
