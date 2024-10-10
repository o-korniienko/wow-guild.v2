package com.wowguild.service.guild;

import com.wowguild.common.converter.Converter;
import com.wowguild.common.entity.rank.Report;
import com.wowguild.common.model.wow_logs.WOWLogsFightData;
import com.wowguild.common.model.wow_logs.WOWLogsReportData;
import com.wowguild.web_api.WebApi;
import com.wowguild.web_api.sender.HttpSender;
import com.wowguild.common.service.entity.impl.CharacterService;
import com.wowguild.common.service.entity.impl.WowLogsReportService;
import com.wowguild.common.service.entity.impl.ZoneService;
import com.wowguild.web_api.service.guild.WowLogsCharacterService;
import com.wowguild.web_api.service.guild.WowLogsGuildService;
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
    private WowLogsCharacterService wowLogsCharacterService;
    @MockBean
    private CharacterService characterService;
    @MockBean
    private WowLogsReportService reportService;
    @MockBean
    private ZoneService zoneService;
    @MockBean
    private ReportDataParser reportDataParser;
    @MockBean
    private Converter<Report, WOWLogsReportData.ReportDto> reportConverter;

    @Autowired
    private WowLogsGuildService wowLogsGuildService;

    @ParameterizedTest
    @MethodSource("getReportDataArgs")
    void getReportDataTest(String response, boolean isSuccess) {

        when(tokenManager.getTokenByTag("wow_logs")).thenReturn("some_token");
        when(httpSender.sendRequest(anyString(), any(HashMap.class), eq(HttpMethod.POST), anyString()))
                .thenReturn(response);
        when(reportDataParser.parseTo(any())).thenReturn(getWowLogsReportsObject());
        WOWLogsReportData reportData = wowLogsGuildService.getReportData();

        if (isSuccess) {
            assertNotNull(reportData.getData());
        } else {
            assertNull(reportData.getData());
        }
    }

    @ParameterizedTest
    @MethodSource("updateReportDataArgs")
    public void updateReportDataTest(WOWLogsReportData wowLogsReportsObject, boolean isSuccess,
                                     WOWLogsFightData wowLogsFightData) {
        when(reportConverter.convertToEntity(any(WOWLogsReportData.ReportDto.class))).thenReturn(new Report());
        when(tokenManager.getTokenByTag("wow_logs")).thenReturn("some_token");
        when(httpSender.sendRequest(anyString(), any(HashMap.class), eq(HttpMethod.POST), anyString()))
                .thenReturn("some_response");
        when(reportDataParser.parseToFightData(anyString())).thenReturn(wowLogsFightData);

        when(characterService.findByName(anyString())).thenReturn(new ArrayList<>());
        when(zoneService.findByCanonicalId(anyInt())).thenReturn(null);
        when(wowLogsCharacterService.updateCharacters(anySet(), anySet())).thenReturn(true);

        int size = 0;
        boolean result = wowLogsGuildService.updateReportData(wowLogsReportsObject);

        if (isSuccess) {
            size = wowLogsReportsObject.getData().size();
            assertTrue(result);
        } else {
            assertFalse(result);
        }
        verify(reportService, times(size)).save(any(Report.class));
        verify(tokenManager, times(size)).getTokenByTag("wow_logs");
        verify(httpSender, times(size)).sendRequest(anyString(), any(), eq(HttpMethod.POST), anyString());
        verify(reportDataParser, times(size)).parseToFightData(anyString());
    }

    static Stream<Arguments> getReportDataArgs() {
        return Stream.of(
                Arguments.of("429 Too Many Requests", false),
                Arguments.of(getWowLogsReportsJson(), true),
                Arguments.of("", false)
        );
    }

    static Stream<Arguments> updateReportDataArgs() {
        return Stream.of(
                Arguments.of(getWowLogsReportsObject(), true, getFightReportDataObject()),
                Arguments.of(new WOWLogsReportData(), false, new WOWLogsFightData())
        );
    }
}
