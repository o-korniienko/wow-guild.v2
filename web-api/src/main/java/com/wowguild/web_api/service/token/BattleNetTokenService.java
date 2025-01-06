package com.wowguild.web_api.service.token;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wowguild.common.entity.Token;
import com.wowguild.common.model.TokenResponse;
import com.wowguild.web_api.sender.HttpSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class BattleNetTokenService implements TokenService {

    @Value("${battle.net.id}")
    private String BNET_ID;
    @Value("${battle.net.secret}")
    private String BNET_SECRET;
    private final Gson gson;

    private final HttpSender httpSender;


    @Override
    public Token getToken() {
        Token tokenToSave = new Token();
        try {
            String url = "https://eu.battle.net/oauth/token";
            String response = httpSender.sendRequest(url, HttpMethod.POST, BNET_ID, BNET_SECRET);

            if (!response.isEmpty()) {
                TokenResponse tokenResponse = gson.fromJson(response, TokenResponse.class);
                tokenToSave.setAccess_token(tokenResponse.getAccess_token());
                tokenToSave.setExpires_in(tokenResponse.getExpires_in());
                tokenToSave.setCreateTime(LocalDateTime.now());
                tokenToSave.setTag("blizzard");
            }
        } catch (JsonSyntaxException e) {
            log.error("Could not get Blizzard token, error {}", e.getMessage());
        }
        return tokenToSave;
    }
}
