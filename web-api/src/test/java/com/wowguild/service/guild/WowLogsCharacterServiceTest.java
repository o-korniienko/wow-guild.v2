package com.wowguild.service.guild;

import com.wowguild.common.entity.wow.Character;
import com.wowguild.common.entity.wow.rank.Boss;
import com.wowguild.common.dto.wow.UpdateStatus;
import com.wowguild.common.model.wow_logs.WOWLogsCharacterRankData;
import com.wowguild.web_api.WebApi;
import com.wowguild.web_api.sender.HttpSender;
import com.wowguild.common.service.impl.BossService;
import com.wowguild.common.service.impl.CharacterRankService;
import com.wowguild.common.service.impl.CharacterService;
import com.wowguild.common.service.impl.RankService;
import com.wowguild.web_api.service.guild.WowLogsCharacterService;
import com.wowguild.web_api.service.token.TokenManager;
import com.wowguild.web_api.tool.parser.Parser;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.*;
import java.util.stream.Stream;

import static com.wowguild.arguments.BossGenerator.generateBoss;
import static com.wowguild.arguments.CharacterGenerator.generateCharacter;
import static com.wowguild.arguments.WowLogsDataGenerator.getCharacterRankDataObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = WebApi.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class WowLogsCharacterServiceTest {

    @MockBean
    private CharacterService characterService;
    @MockBean
    private BossService bossService;
    @MockBean
    private CharacterRankService characterRankService;
    @MockBean
    private RankService rankService;
    @MockBean
    private TokenManager tokenManager;
    @MockBean
    private HttpSender httpSender;
    @MockBean
    private Parser<WOWLogsCharacterRankData> characterRankDataParser;

    @Autowired
    private WowLogsCharacterService wowLogsCharacterService;

    @BeforeEach
    void setUp() {

    }

    @ParameterizedTest
    @MethodSource("updateCharactersArgs")
    void updateCharactersTest(Set<Character> characters, WOWLogsCharacterRankData wowLogsCharacterRankData,
                              List<Boss> bosses, String httpResponse, boolean isSuccess) {
        when(tokenManager.getTokenByTag("wow_logs")).thenReturn("some_token");
        when(httpSender.sendRequest(anyString(), any(HashMap.class), eq(HttpMethod.POST), anyString()))
                .thenReturn(httpResponse);
        when(characterRankDataParser.parseTo(anyString())).thenReturn(wowLogsCharacterRankData);
        when(bossService.findByEncounterID(anyLong())).thenReturn(bosses);

        boolean result = wowLogsCharacterService.updateCharacters(characters, new HashSet<>(bosses));

        assertEquals(isSuccess, result);
    }

    @ParameterizedTest
    @MethodSource("updateCharacterArgs")
    void updateCharacterTest(Character character, WOWLogsCharacterRankData wowLogsCharacterRankData,
                              List<Boss> bosses, String httpResponse, String status, boolean isSuccess) {
        when(tokenManager.getTokenByTag("wow_logs")).thenReturn("some_token");
        when(httpSender.sendRequest(anyString(), any(HashMap.class), eq(HttpMethod.POST), anyString()))
                .thenReturn(httpResponse);
        when(characterRankDataParser.parseTo(anyString())).thenReturn(wowLogsCharacterRankData);
        when(bossService.findByEncounterID(anyLong())).thenReturn(bosses);
        when(bossService.findAll()).thenReturn(bosses);

        UpdateStatus<Character> result = wowLogsCharacterService.updateCharacter(character, status);

        if (isSuccess){
            assertEquals(status, result.getStatus());
        }else{
            assertNotEquals(status, result.getStatus());
        }


    }

    static Stream<Arguments> updateCharactersArgs() {
        Boss boss1 = generateBoss("Fight 3", 1L, 5, 1003);
        Boss boss2 = generateBoss("Fight 2", 2L, 4, 1002);
        Boss boss3 = generateBoss("Fight 1", 3L, 3, 1001);

        Character character1 = generateCharacter("Liut", LocalDateTime.now());
        Character character2 = generateCharacter("Tom", LocalDateTime.now());
        Character character3 = generateCharacter("Sem", LocalDateTime.now());

        Set<Character> characters = new HashSet<>();
        characters.add(character1);
        characters.add(character2);
        characters.add(character3);

        return Stream.of(
                Arguments.of(characters, getCharacterRankDataObject(), List.of(boss1, boss2, boss3),
                        "some_response", true),
                Arguments.of(characters, getCharacterRankDataObject(), List.of(boss1, boss2, boss3),
                        "", false),
                Arguments.of(characters, getCharacterRankDataObject(), List.of(boss1, boss2, boss3),
                        "429 Too Many Requests", false)
        );
    }

    static Stream<Arguments> updateCharacterArgs() {
        Boss boss1 = generateBoss("Fight 3", 1L, 5, 1003);
        Boss boss2 = generateBoss("Fight 2", 2L, 4, 1002);
        Boss boss3 = generateBoss("Fight 1", 3L, 3, 1001);

        Character character = generateCharacter("Liut", LocalDateTime.now());

        return Stream.of(
                Arguments.of(character, getCharacterRankDataObject(), List.of(boss1, boss2, boss3),
                        "some_response", "Successful", true),
                Arguments.of(character, getCharacterRankDataObject(), List.of(boss1, boss2, boss3),
                        "","", false),
                Arguments.of(character, getCharacterRankDataObject(), List.of(boss1, boss2, boss3),
                        "429 Too Many Requests", null, false)
        );
    }
}
