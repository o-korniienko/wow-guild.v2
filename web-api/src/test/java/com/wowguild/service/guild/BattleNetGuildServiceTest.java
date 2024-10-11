package com.wowguild.service.guild;

import com.wowguild.common.entity.Character;
import com.wowguild.common.dto.UpdateStatus;
import com.wowguild.web_api.sender.HttpSender;
import com.wowguild.web_api.service.guild.BattleNetCharacterService;
import com.wowguild.web_api.service.guild.BattleNetGuildService;
import com.wowguild.web_api.service.token.TokenManager;
import com.wowguild.web_api.tool.parser.GuildProfileParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static com.wowguild.arguments.GuildAndCharacterGenerator.getBattleNetGuildProfileObject;
import static com.wowguild.arguments.GuildAndCharacterGenerator.getCharacterList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BattleNetGuildServiceTest {

    @Mock
    private TokenManager tokenManager;
    @Mock
    private HttpSender httpSender;
    @Mock
    private BattleNetCharacterService battleNetCharacterService;
    @Mock
    private GuildProfileParser guildProfileParser;

    @InjectMocks
    private BattleNetGuildService battleNetGuildService;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("getGuildDataArgs")
    void getGuildDataTest(String token) {
        when(tokenManager.getTokenByTag("blizzard")).thenReturn(token);

        assertDoesNotThrow(() -> battleNetGuildService.getGuildData());
    }

    @ParameterizedTest
    @MethodSource("parseGuildDataArgs")
    void parseGuildDataTest(List<Character> characterList) {
        for (Character character : characterList) {
            UpdateStatus<Character> updateStatus = new UpdateStatus<>();
            updateStatus.setResult(character);
            when(battleNetCharacterService.getCharacterData(anyString(), anyInt(), eq(character.getName()), isNull()))
                    .thenReturn(updateStatus);
        }

        List<Character> characters = battleNetGuildService.parseGuildData(getBattleNetGuildProfileObject());
        assertFalse(characters.isEmpty());
    }

    static Stream<Arguments> getGuildDataArgs() {
        return Stream.of(
                Arguments.of("some_token"),
                Arguments.of((Object) null)
        );
    }

    static Stream<Arguments> parseGuildDataArgs() {
        return Stream.of(
                Arguments.of(getCharacterList())
        );
    }
}
