package com.wowguild.service.guild;

import com.wowguild.common.entity.wow.Character;
import com.wowguild.common.dto.wow.UpdateStatus;
import com.wowguild.common.service.impl.CharacterService;
import com.wowguild.web_api.service.guild.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.wowguild.arguments.CharacterGenerator.generateCharacter;
import static com.wowguild.arguments.GuildAndCharacterGenerator.getBattleNetGuildProfileObject;
import static com.wowguild.arguments.GuildAndCharacterGenerator.getCharacterList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GuildManagerTest {

    @Mock
    private BattleNetGuildService battleNetGuildService;
    @Mock
    private WowLogsGuildService wowLogsGuildService;
    @Mock
    private BattleNetCharacterService battleNetCharacterService;
    @Mock
    private WowLogsCharacterService wowLogsCharacterService;
    @Mock
    private CharacterService characterService;

    @InjectMocks
    private GuildManager guildManager;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("updateMembersFromBlizzardDBArgs")
    void updateMembersFromBlizzardDBTest(List<Character> characters) {
        when(battleNetGuildService.getGuildData()).thenReturn(getBattleNetGuildProfileObject());
        when(battleNetGuildService.parseGuildData(any())).thenReturn(getCharacterList());
        when(characterService.findAll()).thenReturn(characters);

        assertDoesNotThrow(() -> {
            guildManager.updateMembersFromBlizzardDB();
        });
    }

    @ParameterizedTest
    @MethodSource("updateRankingDataArgs")
    void updateRankingDataTest(boolean noErrors) {
        String expectedSuccess = "Successful";
        when(wowLogsGuildService.updateReportData(any())).thenReturn(noErrors);

        String result = guildManager.updateRankingData();

        if (noErrors) {
            assertEquals(expectedSuccess, result);
        } else {
            assertNotEquals(expectedSuccess, result);
        }
    }

    @ParameterizedTest
    @MethodSource("updateCharacterDataArgs")
    void updateCharacterDataTest(Character character, boolean isFound, UpdateStatus<Character> updateStatus) {
        when(characterService.findById(1L)).thenReturn(character);

        if (isFound) {
            when(battleNetGuildService.getGuildData()).thenReturn(getBattleNetGuildProfileObject());
            when(battleNetCharacterService.updateCharacter(any(), any())).thenReturn(updateStatus);
            assertDoesNotThrow(() -> {
                guildManager.updateCharacterData(1L);
            });
        } else {
            assertThrows(EntityNotFoundException.class, () -> {
                guildManager.updateCharacterData(1L);
            });
        }
    }

    @ParameterizedTest
    @MethodSource("getRankedMembersArgs")
    void getRankedMembersTest(List<Character> characters, boolean isRanked) {
        when(characterService.findAll()).thenReturn(characters);
        List<Character> rankedMembers = guildManager.getRankedMembers();

        if (!isRanked) {
            assertTrue(rankedMembers.isEmpty());
        } else {
            assertFalse(rankedMembers.isEmpty());
        }
    }

    static Stream<Arguments> updateMembersFromBlizzardDBArgs() {
        Character character = generateCharacter("Liut", LocalDateTime.now());
        Character character2 = generateCharacter("Ted", LocalDateTime.now());
        Character character3 = generateCharacter("Som", LocalDateTime.now());

        return Stream.of(
                Arguments.of(List.of(character, character2, character3)),
                Arguments.of(Collections.emptyList())
        );
    }

    static Stream<Arguments> updateRankingDataArgs() {
        return Stream.of(
                Arguments.of(true),
                Arguments.of(false)
        );
    }

    static Stream<Arguments> updateCharacterDataArgs() {
        Character character = generateCharacter("Liut", LocalDateTime.now());
        UpdateStatus<Character> updateStatus = new UpdateStatus<>();
        updateStatus.setStatus("Success");
        updateStatus.setResult(character);
        return Stream.of(
                Arguments.of(character, true, updateStatus),
                Arguments.of(null, false, updateStatus)
        );
    }

    static Stream<Arguments> getRankedMembersArgs() {
        Character character = generateCharacter("Liut", LocalDateTime.now());
        Character character2 = generateCharacter("Ted", LocalDateTime.now());
        Character character3 = generateCharacter("Som", LocalDateTime.now());

        return Stream.of(
                Arguments.of(getCharacterList(), false),
                Arguments.of(List.of(character, character2, character3), true)
        );
    }
}
