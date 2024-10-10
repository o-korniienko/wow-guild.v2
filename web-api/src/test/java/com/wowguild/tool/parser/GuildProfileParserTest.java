package com.wowguild.tool.parser;

import com.wowguild.common.model.blizzard.GuildProfile;
import com.wowguild.web_api.WebApi;
import com.wowguild.web_api.tool.parser.GuildProfileParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static com.wowguild.arguments.GuildAndCharacterGenerator.getBattleNetGuildProfileJson;
import static com.wowguild.arguments.GuildAndCharacterGenerator.getBattleNetGuildProfileObject;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = WebApi.class)
@ActiveProfiles("test")
public class GuildProfileParserTest {

    @Autowired
    private GuildProfileParser guildProfileParser;

    @ParameterizedTest
    @MethodSource("parseToArgs")
    void parseToTest(String json, GuildProfile expected) {
        GuildProfile result = guildProfileParser.parseTo(json);

        assertEquals(expected, result);
    }


    static Stream<Arguments> parseToArgs() {

        return Stream.of(
                Arguments.of(getBattleNetGuildProfileJson(), getBattleNetGuildProfileObject()),
                Arguments.of("some-json", null),
                Arguments.of("", null)
        );
    }
}
