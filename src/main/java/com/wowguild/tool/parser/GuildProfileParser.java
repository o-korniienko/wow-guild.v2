package com.wowguild.tool.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowguild.model.blizzard.GuildProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GuildProfileParser implements Parser<GuildProfile> {

    private final ObjectMapper mapper;

    @Override
    public GuildProfile parseTo(String json) {
        try {
            return mapper.readValue(json, GuildProfile.class);
        } catch (JsonProcessingException e) {
            log.error("Could not parse json, error: {}", e.getMessage());
        }
        return null;
    }
}
