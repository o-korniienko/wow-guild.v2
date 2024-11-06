package com.wowguild.service.guild;

import com.wowguild.common.entity.wow.Character;
import com.wowguild.common.dto.wow.UpdateStatus;
import com.wowguild.common.model.blizzard.CharacterProfile;
import com.wowguild.common.model.blizzard.GuildProfile;
import com.wowguild.web_api.sender.HttpSender;
import com.wowguild.common.service.impl.CharacterService;
import com.wowguild.web_api.service.wow.BattleNetCharacterService;
import com.wowguild.web_api.service.token.TokenManager;
import com.wowguild.web_api.tool.LogHandler;
import com.wowguild.web_api.tool.parser.CharacterImageDataParser;
import com.wowguild.web_api.tool.parser.CharacterProfileParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static com.wowguild.arguments.CharacterGenerator.generateCharacter;
import static com.wowguild.arguments.GuildAndCharacterGenerator.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BattleNetCharacterServiceTest {

    @Mock
    private TokenManager tokenManager;
    @Mock
    private HttpSender httpSender;
    @Mock
    private LogHandler logHandler;
    @Mock
    private CharacterService characterService;
    @Mock
    private CharacterProfileParser characterProfileParser;
    @Mock
    private CharacterImageDataParser characterImageDataParser;

    //@InjectMocks
    private BattleNetCharacterService battleNetCharacterService;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
        battleNetCharacterService = new BattleNetCharacterService(tokenManager, httpSender, logHandler,
                characterService, characterProfileParser, characterImageDataParser);
    }

    @ParameterizedTest
    @MethodSource("updateCharacterArgs")
    void updateCharacterTest(Character character, GuildProfile guildProfile) {
        UpdateStatus<Character> result = battleNetCharacterService.updateCharacter(character, guildProfile);

        assertNotNull(result.getStatus());
        if (result.getStatus().equals("Successful")) {
            verify(characterService, times(1)).save(character);
        } else {
            verify(characterService, never()).save(any());
        }
    }

    @ParameterizedTest
    @MethodSource("isContainedArgs")
    void isContainedTest(Character character, List<Character> characterList, boolean expected) {
        boolean result = battleNetCharacterService.isContained(character, characterList);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("getCharacterDataArgs")
    void getCharacterDataTest(String token, boolean success, String httpResponse, Character character,
                              CharacterProfile characterProfile) {
        String expectedStatus = "Successful";
        when(tokenManager.getTokenByTag("blizzard")).thenReturn(token);
        if (token != null) {
            when(httpSender.sendRequest(anyString(), any(), anyString())).thenReturn(httpResponse);
        }

        if (success) {
            when(characterProfileParser.parseTo(httpResponse)).thenReturn(characterProfile);
            UpdateStatus<Character> characterData = battleNetCharacterService.getCharacterData("some_url",
                    1, "Liut", character);
            assertEquals(expectedStatus, characterData.getStatus());
        } else {
            UpdateStatus<Character> characterData = battleNetCharacterService.getCharacterData("some_url",
                    1, "Liut", character);
            assertNotEquals(expectedStatus, characterData.getStatus());
        }

    }

    static Stream<Arguments> updateCharacterArgs() {
        Character character = getCharacterList().get(0);

        return Stream.of(
                Arguments.of(character, getBattleNetGuildProfileObject()),
                Arguments.of(character, null)
        );
    }

    static Stream<Arguments> isContainedArgs() {
        List<Character> characterList = getCharacterList();
        Character character1 = characterList.get(0);
        Character character2 = generateCharacter("Liut", LocalDateTime.now());
        return Stream.of(
                Arguments.of(character1, characterList, true),
                Arguments.of(character2, characterList, false)
        );
    }

    static Stream<Arguments> getCharacterDataArgs() {
        CharacterProfile characterProfile = generateCharacterProfileObject();
        Character character = generateCharacter("Liut", LocalDateTime.now());

        return Stream.of(
                Arguments.of(null, false, "some response", null, null),
                Arguments.of("some_token", true, "some response", null, characterProfile),
                Arguments.of("some_token", false, "some response", character, null),
                Arguments.of("some_token", false, "", null, null),
                Arguments.of("some_token", true, "some response", character, characterProfile),
                Arguments.of("some_token", false, null, null, null)
        );
    }

}
