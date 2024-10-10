package com.wowguild.tool.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowguild.model.wow_logs.WOWLogsCharacterRankData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterRankDataParser implements Parser<WOWLogsCharacterRankData> {

    private final ObjectMapper mapper;

    private String parseByKey(String jsonIn, String key) {
        String result = "";
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(jsonIn);
            JsonNode nodeObject = actualObj.get("data").get("characterData").get(key);
            if (nodeObject != null) {
                return nodeObject.toString();
            }
        } catch (Exception e) {
            log.error("Coule not parse JSON {}, with key {}. Error: {}", jsonIn, key, e.getMessage());
        }
        return result;
    }

    @Override
    public WOWLogsCharacterRankData parseTo(String json) {
        try {
            String characterRank = parseByKey(json, "character");
            return mapper.readValue(characterRank, WOWLogsCharacterRankData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not parse json, error: {}", e.getMessage());
        }
        return null;
    }
}
