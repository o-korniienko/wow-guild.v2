package com.wowguild.tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Slf4j
@Service
public class JsonParser {


    private final ObjectMapper mapper;

    public String parse(String jsonIn, String key) {
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
}
