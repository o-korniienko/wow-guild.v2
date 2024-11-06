package com.wowguild.service.guild;

import com.wowguild.common.entity.wow.rank.Boss;
import com.wowguild.common.model.wow_logs.WowLogsWorldData;
import com.wowguild.common.service.impl.BossService;
import com.wowguild.common.service.impl.ZoneService;
import com.wowguild.web_api.WebApi;
import com.wowguild.web_api.sender.HttpSender;
import com.wowguild.web_api.service.token.TokenManager;
import com.wowguild.web_api.service.wow.WowLogsWorldDataService;
import com.wowguild.web_api.tool.parser.WowLogsWorldDataParser;
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

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.wowguild.arguments.WowLogsDataGenerator.getWowLogsWorldDataJson;
import static com.wowguild.arguments.WowLogsDataGenerator.getWowLogsWorldDtaObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = WebApi.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class WowLogsWorldDataServiceTest {

    @MockBean
    private WowLogsWorldDataParser wowLogsWorldDataParser;
    @MockBean
    private TokenManager tokenManager;
    @MockBean
    private HttpSender httpSender;
    @MockBean
    private BossService bossService;
    @MockBean
    private ZoneService zoneService;

    @Autowired
    private WowLogsWorldDataService wowLogsWorldDataService;

    @ParameterizedTest
    @MethodSource("getRaidsDataArgs")
    void getRaidsDataTest(String response, int size) {
        when(tokenManager.getTokenByTag("wow_logs")).thenReturn("some_token");
        when(httpSender.sendRequest(anyString(), any(HashMap.class), eq(HttpMethod.POST), anyString()))
                .thenReturn(response);
        when(wowLogsWorldDataParser.parseTo(any())).thenReturn(getWowLogsWorldDtaObject());

        Set<Boss> raidsData = wowLogsWorldDataService.getRaidsData();

        assertEquals(size, raidsData.size());
    }

    static Stream<Arguments> getRaidsDataArgs() {
        int size = 0;
        List<WowLogsWorldData.Zone> zones = getWowLogsWorldDtaObject().getZones();
        for (WowLogsWorldData.Zone zone : zones) {
            size = size + (zone.getEncounters().size() * 3);
        }

        return Stream.of(
                Arguments.of("429 Too Many Requests", 0),
                Arguments.of(getWowLogsWorldDataJson(), size),
                Arguments.of("", 0)
        );
    }
}
