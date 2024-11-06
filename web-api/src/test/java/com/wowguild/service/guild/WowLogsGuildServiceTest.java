package com.wowguild.service.guild;

import com.wowguild.common.converter.Converter;
import com.wowguild.common.entity.wow.rank.Report;
import com.wowguild.common.model.wow_logs.WOWLogsFightData;
import com.wowguild.common.model.wow_logs.WOWLogsReportData;
import com.wowguild.web_api.WebApi;
import com.wowguild.web_api.sender.HttpSender;
import com.wowguild.common.service.impl.CharacterService;
import com.wowguild.common.service.impl.WowLogsReportService;
import com.wowguild.common.service.impl.ZoneService;
import com.wowguild.web_api.service.wow.WowLogsCharacterService;
import com.wowguild.web_api.service.wow.WowLogsGuildService;
import com.wowguild.web_api.service.token.TokenManager;
import com.wowguild.web_api.tool.parser.ReportDataParser;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import static com.wowguild.arguments.WowLogsDataGenerator.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = WebApi.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class WowLogsGuildServiceTest {

    @MockBean
    private TokenManager tokenManager;
    @MockBean
    private HttpSender httpSender;
    @MockBean
    private WowLogsReportService reportService;
    @MockBean
    private ReportDataParser reportDataParser;
    @MockBean
    private Converter<Report, WOWLogsReportData.ReportDto> reportConverter;

    @Autowired
    private WowLogsGuildService wowLogsGuildService;

    @ParameterizedTest
    @MethodSource("getReportsArgs")
    void getReportsTest(String response, boolean isSuccess) {

        when(tokenManager.getTokenByTag("wow_logs")).thenReturn("some_token");
        when(httpSender.sendRequest(anyString(), any(HashMap.class), eq(HttpMethod.POST), anyString()))
                .thenReturn(response);
        when(reportDataParser.parseTo(any())).thenReturn(getWowLogsReportsObject());
        WOWLogsReportData reportData = wowLogsGuildService.getReports();

        if (isSuccess) {
            assertNotNull(reportData.getData());
        } else {
            assertNull(reportData.getData());
        }
    }

    @ParameterizedTest
    @MethodSource("updateReportsArgs")
    public void updateReportsTest(WOWLogsReportData wowLogsReportsObject, boolean isSuccess,
                                  WOWLogsFightData wowLogsFightData) {
        when(reportConverter.convertToEntity(any(WOWLogsReportData.ReportDto.class))).thenReturn(new Report());

        int size = 0;
        boolean result = wowLogsGuildService.updateReports(wowLogsReportsObject);

        if (isSuccess) {
            size = wowLogsReportsObject.getData().size();
            assertTrue(result);
        } else {
            assertFalse(result);
        }
        verify(reportService, times(size)).save(any(Report.class));
    }

    static Stream<Arguments> getReportsArgs() {
        return Stream.of(
                Arguments.of("429 Too Many Requests", false),
                Arguments.of(getWowLogsReportsJson(), true),
                Arguments.of("", false)
        );
    }

    static Stream<Arguments> updateReportsArgs() {
        return Stream.of(
                Arguments.of(getWowLogsReportsObject(), true, getFightReportDataObject()),
                Arguments.of(new WOWLogsReportData(), false, new WOWLogsFightData())
        );
    }
}
