package com.wowguild.converter;

import com.wowguild.common.converter.RaidAndBossConverter;
import com.wowguild.common.converter.CharacterConverter;
import com.wowguild.common.dto.wow.CharacterDto;
import com.wowguild.common.entity.wow.Character;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static com.wowguild.arguments.CharacterGenerator.generateCharacter;
import static com.wowguild.arguments.CharacterGenerator.generateCharacterDto;
import static org.junit.jupiter.api.Assertions.*;


public class CharacterConverterTest {

    private CharacterConverter converter;
    private CharacterDto characterDtoExpected;
    private final static LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        converter = new CharacterConverter(new RaidAndBossConverter());
        characterDtoExpected = generateCharacterDto("Liut", now);
    }

    @ParameterizedTest
    @MethodSource("provideCharacterTestArgs")
    void testConvertToDto(Character character, boolean positiveTest) {
        CharacterDto result = converter.convertToDto(character);
        if (positiveTest) {
            assertEquals(characterDtoExpected, result);
        } else {
            assertNotEquals(characterDtoExpected, result);
        }
    }

    @ParameterizedTest
    @MethodSource("testConvertPartlyToDtoArgs")
    void testConvertPartlyToDto(Character character, CharacterDto expected, boolean isFull) {
        CharacterDto result = converter.convertPartlyToDto(character, isFull);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("provideCharacterDtoTestArgs")
    public void testConvertToEntity(CharacterDto characterDto) {
        Character result = converter.convertToEntity(characterDto);
        assertNull(result);
    }

    static Stream<Arguments> provideCharacterTestArgs() {
        Character character = generateCharacter("Liut", now);
        Character character1 = generateCharacter("Liutb", now);

        return Stream.of(
                Arguments.of(character, true),
                Arguments.of(character1, false)
        );
    }

    static Stream<Arguments> provideCharacterDtoTestArgs() {
        CharacterDto characterDto = generateCharacterDto("Liut", LocalDateTime.now());

        return Stream.of(
                Arguments.of(characterDto)
        );
    }

    static Stream<Arguments> testConvertPartlyToDtoArgs() {
        Character character = generateCharacter("Liut", now);
        CharacterDto characterDto1 = generateCharacterDto("Liut", now);
        CharacterDto characterDto2 = generateCharacterDto("Liut", now);
        characterDto2.setRanks(null);

        return Stream.of(
                Arguments.of(character, characterDto1, true),
                Arguments.of(character, characterDto2, false)
        );
    }
}
