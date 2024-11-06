package com.wowguild.tool.parser;

import com.wowguild.common.model.wow_logs.WowLogsWorldData;
import com.wowguild.web_api.WebApi;
import com.wowguild.web_api.tool.parser.WowLogsWorldDataParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static com.wowguild.arguments.WowLogsDataGenerator.getWowLogsWorldDataJson;
import static com.wowguild.arguments.WowLogsDataGenerator.getWowLogsWorldDtaObjectForParse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = WebApi.class)
@ActiveProfiles("test")
public class WowLogsWorldDataParserTest {

    @Autowired
    private WowLogsWorldDataParser wowLogsWorldDataParser;

    @ParameterizedTest
    @MethodSource("parseToArgs")
    void parseToTest(String json, WowLogsWorldData expected) {
        WowLogsWorldData result = wowLogsWorldDataParser.parseTo(json);
        assertEquals(expected, result);
    }

    static Stream<Arguments> parseToArgs() {

        return Stream.of(
                Arguments.of(getWowLogsWorldDataJson(), getWowLogsWorldDtaObjectForParse()),
                Arguments.of("some-json", null),
                Arguments.of("", null)
        );
    }
}
