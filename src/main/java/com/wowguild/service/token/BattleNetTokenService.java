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

@RequiredArgsConstructor
@Service
public class BattleNetTokenService implements TokenService {

    @Value("${battle_net_id}")
    private String BNET_ID;
    @Value("${battle_net_secret}")
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
                return tokenToSave;
            }
        } catch (JsonSyntaxException e) {
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - getTokenFromBlizzard got error: " + e.getMessage());
        }
        return null;
    }
}
