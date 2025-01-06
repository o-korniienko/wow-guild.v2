package com.wowguild.tool.parser;

import com.wowguild.common.model.blizzard.CharacterImageData;
import com.wowguild.web_api.WebApi;
import com.wowguild.web_api.tool.parser.CharacterImageDataParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static com.wowguild.arguments.GuildAndCharacterGenerator.getCharacterImageDataObject;
import static com.wowguild.arguments.GuildAndCharacterGenerator.getCharacterProfileJson;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = WebApi.class)
@ActiveProfiles("test")
public class CharacterImageDataParserTest {

    @Autowired
    private CharacterImageDataParser characterImageDataParser;

    @ParameterizedTest
    @MethodSource("parseToArgs")
    void parseToTest(String json, CharacterImageData expected) {
        CharacterImageData result = characterImageDataParser.parseTo(json);

        assertEquals(expected, result);
    }


    static Stream<Arguments> parseToArgs() {

        return Stream.of(
                Arguments.of(getCharacterProfileJson(), getCharacterImageDataObject()),
                Arguments.of("some-json", null),
                Arguments.of("", null)
        );
    }
}
