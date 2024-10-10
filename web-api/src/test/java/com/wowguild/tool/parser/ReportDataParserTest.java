package com.wowguild.tool.parser;

import com.wowguild.model.wow_logs.WOWLogsFightData;
import com.wowguild.model.wow_logs.WOWLogsReportData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static com.wowguild.arguments.WowLogsDataGenerator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class ReportDataParserTest {

    @Autowired
    private ReportDataParser reportDataParser;

    @ParameterizedTest
    @MethodSource("parseToArgs")
    void parseToTest(String json, WOWLogsReportData expected) {
        WOWLogsReportData result = reportDataParser.parseTo(json);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("parseToFightDataArgs")
    void parseToFightDataTest(String json, WOWLogsFightData expected) {
        WOWLogsFightData result = reportDataParser.parseToFightData(json);

        assertEquals(expected, result);
    }

    static Stream<Arguments> parseToArgs() {

        return Stream.of(
                Arguments.of(getWowLogsReportsJson(), getWowLogsReportsObject()),
                Arguments.of("some-json", null),
                Arguments.of("", null)
        );
    }

    static Stream<Arguments> parseToFightDataArgs() {

        return Stream.of(
                Arguments.of(getFightReportDataJson(), getFightReportDataObject()),
                Arguments.of("some-json", null),
                Arguments.of("", null)
        );
    }
}
