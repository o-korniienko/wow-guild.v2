package com.wowguild.service.token;

import com.wowguild.entity.Token;
import com.wowguild.model.TokenResponse;
import com.wowguild.sender.HttpSender;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class WowLogsTokenService implements TokenService {

    @Value("${spring.wow_logs_id}")
    private String WOW_LOGS_ID;
    @Value("${spring.wow_logs_secret}")
    private String WOW_LOGS_SECRET;
    private final Gson gson;

    private final HttpSender httpSender;

    @Override
    public Token getToken() {
        Token token = new Token();
        try {
            String url = "https://ru.warcraftlogs.com/oauth/token";
            String response = httpSender.sendRequest(url, HttpMethod.POST, WOW_LOGS_ID, WOW_LOGS_SECRET);

            if (!response.isEmpty()) {
                TokenResponse tokenResponse = gson.fromJson(response, TokenResponse.class);
                token.setTag("wow_logs");
                token.setAccess_token(tokenResponse.getAccess_token());
                token.setExpires_in(tokenResponse.getExpires_in());
                token.setCreateTime(LocalDateTime.now());
            }
        } catch (JsonSyntaxException e) {
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - getTokenFromWOWLogs got error: " + e.getMessage());
        }
        return token;
    }
}
