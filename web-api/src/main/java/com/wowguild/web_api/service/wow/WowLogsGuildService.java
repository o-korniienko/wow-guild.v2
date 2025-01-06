package com.wowguild.web_api.service.wow;

import com.google.gson.JsonSyntaxException;
import com.wowguild.common.converter.Converter;
import com.wowguild.common.entity.wow.rank.Boss;
import com.wowguild.common.entity.wow.rank.Report;
import com.wowguild.common.model.wow_logs.WOWLogsFightData;
import com.wowguild.common.model.wow_logs.WOWLogsReportData;
import com.wowguild.common.service.impl.WowLogsReportService;
import com.wowguild.web_api.sender.HttpSender;
import com.wowguild.web_api.service.token.TokenManager;
import com.wowguild.web_api.tool.parser.ReportDataParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class WowLogsGuildService {

    @Value("${wow.logs.api}")
    private String wowLogsApi;
    @Value("${wow.logs.realm}")
    private String realm;
    @Value("${wow.logs.guild.name}")
    private String guildName;
    @Value("${wow.logs.server.region}")
    private String serverRegion;

    private final TokenManager tokenManager;
    private final HttpSender httpSender;
    private final WowLogsReportService reportService;
    private final ReportDataParser reportDataParser;
    private final Converter<Report, WOWLogsReportData.ReportDto> reportConverter;

    public WOWLogsReportData getReports() {
        WOWLogsReportData report = new WOWLogsReportData();
        String token = tokenManager.getTokenByTag("wow_logs");

        try {
            Map<String, String> body = new HashMap<>();
            body.put("query", "{reportData {reports(guildName:\"" + guildName
                    + "\", guildServerSlug:\"" + realm + "\", guildServerRegion:\"" + serverRegion
                    + "\") {data{code,endTime}}}}");
            String response = httpSender.sendRequest(wowLogsApi, body, HttpMethod.POST, token);

            if (!response.isEmpty()) {
                if (response.contains("429 Too Many Requests")) {
                    return report;
                }
                report = reportDataParser.parseTo(response);

                if (report == null) {
                    report = new WOWLogsReportData();
                }
            } else {
                log.info("No reports found, empty response");
            }
        } catch (JsonSyntaxException e) {
            log.error("Could not get report data from WOWLogs, error {}", e.getMessage());
        }
        return report;
    }

    public boolean updateReports(WOWLogsReportData reportData) {
        if (reportData.getData() != null) {
            List<Report> reports = reportData.getData().stream()
                    .map(reportConverter::convertToEntity)
                    .toList();
            if (reports != null) {
                for (Report report : reports) {
                    boolean isReportAlreadyInDb = isReportAlreadyInDB(report);
                    if (!isReportAlreadyInDb) {
                        reportService.save(report);
                    }
                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private WOWLogsFightData getReportFightData(String code) {
        WOWLogsFightData fight = null;
        String token = tokenManager.getTokenByTag("wow_logs");
        Map<String, String> body = new HashMap<>();
        String requestString = "{reportData {report(code:\"" + code + "\" ){fights(killType:Encounters){kill,name, difficulty, encounterID, gameZone{name,id}}zone{name,expansion{name},brackets{type,bucket,min,max}}rankedCharacters{name, canonicalID, server{slug}}}}}";
        body.put("query", requestString);

        String response = httpSender.sendRequest(wowLogsApi, body, HttpMethod.POST, token);

        try {
            if (!response.isEmpty()) {
                if (response.contains("429 Too Many Requests")) {
                    return fight;
                }

                fight = reportDataParser.parseToFightData(response);
            }
        } catch (JsonSyntaxException e) {
            log.error("Could not get report fight data from WOWLogs, error {}", e.getMessage());
        }

        return fight;
    }

    private boolean isReportAlreadyInDB(Report report) {
        Report reportFromDb = reportService.findByCode(report.getCode());
        return reportFromDb != null;
    }
}