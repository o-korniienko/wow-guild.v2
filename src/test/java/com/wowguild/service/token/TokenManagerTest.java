package com.wowguild.service.token;

import com.wowguild.entity.Token;
import com.wowguild.repos.TokenRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TokenManagerTest {


    @MockBean
    private TokenRepo tokenRepo;
    @MockBean
    private BattleNetTokenService battleNetTokenService;
    @MockBean
    private WowLogsTokenService wowLogsTokenService;

    @Autowired
    private TokenManagerImpl tokenManager;


    @BeforeEach
    void setUp() {
    }

    @ParameterizedTest
    @MethodSource("getTokenByTagArgs")
    void getTokenByTag(String tag, Token token, Token blizzardToken, Token wowLogsToken, String expectedToke) {
        when(tokenRepo.findByTag(anyString())).thenReturn(token);
        when(battleNetTokenService.getToken()).thenReturn(blizzardToken);
        when(wowLogsTokenService.getToken()).thenReturn(wowLogsToken);

        String tokenByTag = tokenManager.getTokenByTag(tag);

        assertEquals(expectedToke, tokenByTag);
    }

    static Stream<Arguments> getTokenByTagArgs() {
        LocalDateTime now = LocalDateTime.now();
        Token token = new Token();
        token.setAccess_token("blizzard_token");
        token.setId(1);
        token.setCreateTime(now.minusYears(2));
        token.setExpires_in(1000L);

        Token blizzardToken = new Token();
        blizzardToken.setAccess_token("blizzard_token");
        blizzardToken.setId(1);
        blizzardToken.setTag("blizzard");
        blizzardToken.setCreateTime(now.minusYears(2));
        blizzardToken.setExpires_in(1000L);

        Token wowLogsToken = new Token();
        wowLogsToken.setAccess_token("wow_logs_token");
        wowLogsToken.setId(2);
        wowLogsToken.setTag("wow_logs");
        wowLogsToken.setCreateTime(now.minusYears(2));
        wowLogsToken.setExpires_in(1000L);

        return Stream.of(
                Arguments.of("blizzard", blizzardToken, blizzardToken, wowLogsToken, "blizzard_token"),
                Arguments.of("wow_logs", wowLogsToken, blizzardToken, wowLogsToken, "wow_logs_token"),
                Arguments.of("blizzard", null, blizzardToken, wowLogsToken, "blizzard_token"),
                Arguments.of("wow_logs", null, blizzardToken, wowLogsToken, "wow_logs_token")
        );
    }
}
