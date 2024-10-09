package com.wowguild.tool.parser;

import com.wowguild.model.blizzard.CharacterProfile;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static com.wowguild.arguments.GuildAndCharacterGenerator.generateCharacterProfileJson;
import static com.wowguild.arguments.GuildAndCharacterGenerator.generateCharacterProfileObject;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class CharacterProfileParserTest {

    @Autowired
    private CharacterProfileParser characterProfileParser;

    @ParameterizedTest
    @MethodSource("parseToArgs")
    void parseToTest(String json, CharacterProfile expected) {
        CharacterProfile result = characterProfileParser.parseTo(json);

        assertEquals(expected, result);
    }


    static Stream<Arguments> parseToArgs() {

        return Stream.of(
                Arguments.of(generateCharacterProfileJson(), generateCharacterProfileObject()),
                Arguments.of("some-json", null),
                Arguments.of("", null)
        );
    }
}
