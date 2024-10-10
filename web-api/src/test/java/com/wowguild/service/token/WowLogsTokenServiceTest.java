package com.wowguild.service.token;

import com.google.gson.Gson;
import com.wowguild.entity.Token;
import com.wowguild.model.TokenResponse;
import com.wowguild.sender.HttpSender;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class WowLogsTokenServiceTest {

    @MockBean
    private Gson gson;
    @MockBean
    private HttpSender httpSender;

    @Autowired
    private WowLogsTokenService wowLogsTokenService;

    @ParameterizedTest
    @MethodSource("getTokenArgs")
    void getTokenTest(String httpResponse, TokenResponse tokenResponse, Token tokenExpected, boolean isSuccess) {
        when(httpSender.sendRequest(anyString(), eq(HttpMethod.POST), anyString(), anyString()))
                .thenReturn(httpResponse);
        when(gson.fromJson(anyString(), eq(TokenResponse.class))).thenReturn(tokenResponse);

        Token token = wowLogsTokenService.getToken();

        if (isSuccess) {
            assertNotNull(token);
            assertEquals(tokenExpected.getAccess_token(), token.getAccess_token());
            assertEquals(tokenExpected.getExpires_in(), token.getExpires_in());
            assertEquals(tokenExpected.getTag(), token.getTag());
        } else {
            assertNotNull(token);
            assertNull(token.getAccess_token());
            assertNull(token.getExpires_in());
            assertNull(token.getTag());
        }

    }

    static Stream<Arguments> getTokenArgs() {
        LocalDateTime now = LocalDateTime.now();
        TokenResponse token = new TokenResponse();
        token.setAccess_token("wow_logs_token");
        token.setExpires_in(1000L);

        Token wowLogsToken = new Token();
        wowLogsToken.setAccess_token("wow_logs_token");
        wowLogsToken.setId(1);
        wowLogsToken.setTag("wow_logs");
        wowLogsToken.setCreateTime(now.minusYears(2));
        wowLogsToken.setExpires_in(1000L);

        return Stream.of(
                Arguments.of("wow-logs-response", token, wowLogsToken, true),
                Arguments.of("", token, new Token(), false)
        );
    }
}
