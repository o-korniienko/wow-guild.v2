package com.wowguild.tool.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowguild.model.blizzard.CharacterProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("characterProfileParser")
@RequiredArgsConstructor
@Slf4j
public class CharacterProfileParser implements Parser<CharacterProfile> {


    private final ObjectMapper mapper;

    @Override
    public CharacterProfile parseTo(String json) {
        try {
            return mapper.readValue(json, CharacterProfile.class);
        } catch (JsonProcessingException e) {
            log.error("Could not parse json, error: {}", e.getMessage());
        }
        return null;
    }
}
