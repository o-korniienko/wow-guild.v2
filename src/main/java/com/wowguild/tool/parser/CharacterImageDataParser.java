package com.wowguild.tool.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowguild.model.blizzard.CharacterImageData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CharacterImageDataParser implements Parser<CharacterImageData> {

    private final ObjectMapper mapper;

    @Override
    public CharacterImageData parseTo(String json) {
        try {
            return mapper.readValue(json, CharacterImageData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not parse json, error: {}", e.getMessage());
        }
        return null;
    }
}
