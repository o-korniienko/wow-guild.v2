package com.wowguild.tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
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
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - parse got error: " + e.getMessage());
            System.out.println("income Json: " + jsonIn);
            System.out.println("key: " + key);
        }
        return result;
    }
}
