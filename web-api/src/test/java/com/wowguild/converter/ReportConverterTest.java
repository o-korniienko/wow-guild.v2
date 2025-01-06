package com.wowguild.converter;

import com.wowguild.common.converter.ReportConverter;
import com.wowguild.common.entity.wow.rank.Report;
import com.wowguild.common.model.wow_logs.WOWLogsReportData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.wowguild.arguments.ReportGenerator.generateReport;
import static com.wowguild.arguments.ReportGenerator.generateReportDto;
import static org.junit.jupiter.api.Assertions.*;

public class ReportConverterTest {

    private ReportConverter reportConverter;
    private Report expected;

    @BeforeEach
    void setUp() {
        reportConverter = new ReportConverter();
        expected = generateReport("123", 987654321);
    }

    @ParameterizedTest
    @MethodSource("provideReportDtoTestArgs")
    public void testConvertToEntity(WOWLogsReportData.ReportDto reportDto, boolean positiveTest) {
        Report result = reportConverter.convertToEntity(reportDto);
        if (positiveTest) {
            assertEquals(expected, result);
        } else {
            assertNotEquals(expected, result);
        }
    }

    @ParameterizedTest
    @MethodSource("provideReportTestArgs")
    public void testConvertToDto(Report report) {
        WOWLogsReportData.ReportDto result = reportConverter.convertToDto(report);
        assertNull(result);
    }

    static Stream<Arguments> provideReportDtoTestArgs() {
        WOWLogsReportData.ReportDto reportDto1 = generateReportDto("123", 987654321);
        WOWLogsReportData.ReportDto reportDto2 = generateReportDto("1234", 987654321);
        WOWLogsReportData.ReportDto reportDto3 = generateReportDto("123", 987624321);

        return Stream.of(
                Arguments.of(reportDto1, true),
                Arguments.of(reportDto2, false),
                Arguments.of(reportDto3, false)
        );
    }

    static Stream<Arguments> provideReportTestArgs() {
        Report report = generateReport("123", 987654321);

        return Stream.of(
                Arguments.of(report)
        );
    }
}
