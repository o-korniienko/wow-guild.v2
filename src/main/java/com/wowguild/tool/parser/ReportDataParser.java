package com.wowguild.tool.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowguild.model.wow_logs.WOWLogsFightData;
import com.wowguild.model.wow_logs.WOWLogsReportData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReportDataParser implements Parser<WOWLogsReportData> {


    private final ObjectMapper mapper;

    private String parseByKey(String jsonIn, String key) {
        String result = "";
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(jsonIn);
            JsonNode nodeObject = actualObj.get("data").get("reportData").get(key);
            if (nodeObject != null) {
                return nodeObject.toString();
            }
        } catch (IOException e) {
            log.error("Coule not parse JSON {}, with key {}. Error: {}", jsonIn, key, e.getMessage());
        }
        return result;
    }

    @Override
    public WOWLogsReportData parseTo(String json) {
        try {
            String reportJson = parseByKey(json, "reports");
            return mapper.readValue(reportJson, WOWLogsReportData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not parse json, error: {}", e.getMessage());
        }
        return null;
    }
    public WOWLogsFightData parseToFightData(String json) {
        try {
            String fightJson = parseByKey(json, "report");
            return mapper.readValue(fightJson, WOWLogsFightData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not parse json, error: {}", e.getMessage());
        }
        return null;
    }
}
