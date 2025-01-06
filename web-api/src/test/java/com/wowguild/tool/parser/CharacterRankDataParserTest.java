package com.wowguild.tool.parser;

import com.wowguild.common.model.wow_logs.WOWLogsCharacterRankData;
import com.wowguild.web_api.WebApi;
import com.wowguild.web_api.tool.parser.CharacterRankDataParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static com.wowguild.arguments.WowLogsDataGenerator.getCharacterRankDataJson;
import static com.wowguild.arguments.WowLogsDataGenerator.getCharacterRankDataObject;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = WebApi.class)
@ActiveProfiles("test")
public class CharacterRankDataParserTest {

    @Autowired
    private CharacterRankDataParser characterRankDataParser;

    @ParameterizedTest
    @MethodSource("parseToArgs")
    void parseToTest(String json, WOWLogsCharacterRankData expected) {
        WOWLogsCharacterRankData result = characterRankDataParser.parseTo(json);

        assertEquals(expected, result);
    }

    static Stream<Arguments> parseToArgs() {

        return Stream.of(
                Arguments.of(getCharacterRankDataJson(), getCharacterRankDataObject()),
                Arguments.of("some-json", null),
                Arguments.of("", null)
        );
    }
}
