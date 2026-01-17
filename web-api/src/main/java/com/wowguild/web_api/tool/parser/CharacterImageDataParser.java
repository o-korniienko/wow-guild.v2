package com.wowguild.web_api.tool.parser;


import com.wowguild.common.model.blizzard.CharacterImageData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@Service("characterImageDataParser")
public class CharacterImageDataParser implements Parser<CharacterImageData> {

    private final ObjectMapper mapper;

    @Override
    public CharacterImageData parseTo(String json) {
        try {
            return mapper.readValue(json, CharacterImageData.class);
        } catch (JacksonException e) {
            log.error("Could not parse json, error: {}", e.getMessage());
        }
        return null;
    }
}
