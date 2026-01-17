package com.wowguild.web_api.tool.parser;


import com.wowguild.common.model.blizzard.CharacterProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service("characterProfileParser")
@RequiredArgsConstructor
@Slf4j
public class CharacterProfileParser implements Parser<CharacterProfile> {


    private final ObjectMapper mapper;

    @Override
    public CharacterProfile parseTo(String json) {
        try {
            return mapper.readValue(json, CharacterProfile.class);
        } catch (JacksonException e) {
            log.error("Could not parse json, error: {}", e.getMessage());
        }
        return null;
    }
}
