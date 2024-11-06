package com.wowguild.web_api.tool.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowguild.common.model.wow_logs.WowLogsWorldData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WowLogsWorldDataParser implements Parser<WowLogsWorldData> {

    private final ObjectMapper mapper;

    @Override
    public WowLogsWorldData parseTo(String json) {
        try {
            String reportJson = parseByKey(json, "worldData");
            return mapper.readValue(reportJson, WowLogsWorldData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not parse json, error: {}", e.getMessage());
        }
        return null;
    }

    private String parseByKey(String jsonIn, String key) {
        String result = "";
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(jsonIn);
            JsonNode nodeObject = actualObj.get("data").get(key);
            if (nodeObject != null) {
                return nodeObject.toString();
            }
        } catch (Exception e) {
            log.error("Coule not parse JSON {}, with key {}. Error: {}", jsonIn, key, e.getMessage());
        }
        return result;
    }
}
